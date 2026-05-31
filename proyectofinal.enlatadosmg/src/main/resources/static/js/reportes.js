let reporteActual = 'usuarios';
let vizInstance = null;

document.addEventListener('DOMContentLoaded', () => {
    vizInstance = new Viz();
    recargarReporteActual();
    cargarPedidosDropdown();
});

async function cargarPedidosDropdown() {
    const select = document.getElementById('select-pedido-cajas');
    try {
        const pedidos = await api.get('/pedidos');
        select.innerHTML = '<option value="" disabled selected>Selecciona un pedido...</option>';
        if (pedidos && pedidos.length > 0) {
            pedidos.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.numeroPedido;
                opt.innerText = `Pedido #${p.numeroPedido} (${p.cliente.nombre})`;
                select.appendChild(opt);
            });
        } else {
            select.innerHTML = '<option value="" disabled>No hay pedidos creados</option>';
        }
    } catch(e) {
        console.error('Error cargando pedidos en dropdown reportes:', e);
    }
}

function cambiarReporte(tipo, btnElement) {
    // Manejo de clase activa en botones
    const listGroup = btnElement.closest('.list-group-custom');
    if (listGroup) {
        listGroup.querySelectorAll('button').forEach(btn => btn.classList.remove('active'));
    }
    btnElement.classList.add('active');

    reporteActual = tipo;
    
    // Titulo
    const titulos = {
        usuarios: 'Reporte: Lista Enlazada de Usuarios',
        almacen: 'Reporte: Pila de Cajas (Almacén LIFO)',
        clientes: 'Reporte: Árbol AVL de Clientes',
        repartidores: 'Reporte: Cola de Repartidores (FIFO)',
        vehiculos: 'Reporte: Cola de Vehículos (FIFO)',
        pedidos: 'Reporte: Lista Enlazada de Pedidos'
    };
    document.getElementById('reporte-titulo').innerText = titulos[tipo] || 'Visualización de Estructura';

    // Panel de subreporte de pedidos
    const subPanel = document.getElementById('subreport-panel');
    if (tipo === 'pedidos') {
        subPanel.classList.remove('d-none');
        cargarPedidosDropdown();
    } else {
        subPanel.classList.add('d-none');
    }

    recargarReporteActual();
}

async function recargarReporteActual() {
    mostrarLoader(true);
    const canvas = document.getElementById('canvas-reporte');
    
    try {
        // Petición a la API de reportes
        const dotContent = await api.get(`/reportes/${reporteActual}`);
        
        // Renderizar DOT a SVG mediante Viz.js
        vizInstance.renderSVGElement(dotContent)
            .then(element => {
                canvas.innerHTML = '';
                canvas.appendChild(element);
                mostrarLoader(false);
            })
            .catch(error => {
                console.error('Viz.js error:', error);
                canvas.innerHTML = `<span class="text-danger">Error de renderizado gráfico: ${error.message}</span>`;
                mostrarLoader(false);
            });
    } catch(err) {
        canvas.innerHTML = '<span class="text-danger">No se pudo cargar el código DOT del servidor.</span>';
        mostrarLoader(false);
    }
}

async function verCajasPedidoReporte() {
    const select = document.getElementById('select-pedido-cajas');
    const numPedido = select.value;

    if (!numPedido) {
        showToast('Seleccione un pedido del listado.', 'warning');
        return;
    }

    mostrarLoader(true);
    const canvas = document.getElementById('canvas-reporte');
    document.getElementById('reporte-titulo').innerText = `Reporte: Cajas del Pedido #${numPedido}`;

    try {
        const dotContent = await api.get(`/reportes/pedidos/${numPedido}/cajas`);
        
        vizInstance.renderSVGElement(dotContent)
            .then(element => {
                canvas.innerHTML = '';
                canvas.appendChild(element);
                mostrarLoader(false);
            })
            .catch(error => {
                console.error(error);
                canvas.innerHTML = '<span class="text-danger">Error de renderizado de cajas de pedido.</span>';
                mostrarLoader(false);
            });
    } catch(err) {
        canvas.innerHTML = '<span class="text-danger">Error cargando estructura de cajas del pedido.</span>';
        mostrarLoader(false);
    }
}

function mostrarLoader(show) {
    const loader = document.getElementById('loader');
    const canvas = document.getElementById('canvas-reporte');
    if (show) {
        loader.classList.remove('d-none');
        canvas.classList.add('d-none');
    } else {
        loader.classList.add('d-none');
        canvas.classList.remove('d-none');
    }
}

function abrirPantallaCompleta() {
    const canvas = document.getElementById('canvas-reporte');
    const svgEl = canvas.querySelector('svg');
    if (!svgEl) {
        showToast('Primero genera un reporte para verlo en pantalla completa.', 'warning');
        return;
    }

    // Clonar el SVG y ponerlo en el modal
    const canvasFullscreen = document.getElementById('canvas-fullscreen');
    canvasFullscreen.innerHTML = '';
    const clone = svgEl.cloneNode(true);
    // Hacer el SVG escalable a toda la pantalla
    clone.style.width = '100%';
    clone.style.height = 'auto';
    clone.style.maxWidth = '100%';
    clone.removeAttribute('width');
    clone.removeAttribute('height');
    canvasFullscreen.appendChild(clone);

    // Actualizar titulo del modal
    const tituloReporte = document.getElementById('reporte-titulo').innerText;
    document.getElementById('modal-fs-titulo').innerText = tituloReporte;

    const modal = new bootstrap.Modal(document.getElementById('modal-fullscreen-graph'));
    modal.show();
}
