let activePedidoId = null;
let modalDetalle = null;

document.addEventListener('DOMContentLoaded', () => {
    modalDetalle = new bootstrap.Modal(document.getElementById('modal-detalle-pedido'));
    cargarListas();
    cargarPedidos();
    cargarEstadoEstructuras();
    inicializarEventos();
});

async function cargarListas() {
    const clienteSelect = document.getElementById('cliente-select');
    try {
        const clientes = await api.get('/clientes');
        clienteSelect.innerHTML = '<option value="" disabled selected>Selecciona cliente...</option>';
        if (clientes && clientes.length > 0) {
            clientes.forEach(c => {
                const opt = document.createElement('option');
                opt.value = c.dpi;
                opt.innerText = `${c.nombre} ${c.apellidos} (DPI: ${c.dpi})`;
                clienteSelect.appendChild(opt);
            });
        } else {
            clienteSelect.innerHTML = '<option value="" disabled>No hay clientes registrados</option>';
        }
    } catch (e) {
        console.error('Error al cargar clientes en dropdown:', e);
    }
}

async function cargarEstadoEstructuras() {
    try {
        const repartidores = await api.get('/repartidores');
        document.getElementById('status-repartidores').innerText = repartidores.length;

        const vehiculos = await api.get('/vehiculos');
        document.getElementById('status-vehiculos').innerText = vehiculos.length;

        const almacen = await api.get('/almacen/cantidad');
        document.getElementById('status-almacen').innerText = almacen.cantidad;
    } catch(e) {
        console.error('Error cargando estado de recursos:', e);
    }
}

async function cargarPedidos() {
    const tbody = document.getElementById('tabla-pedidos');
    try {
        const pedidos = await api.get('/pedidos');
        tbody.innerHTML = '';

        if (!pedidos || pedidos.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-secondary py-4">No hay pedidos registrados en el sistema.</td>
                </tr>
            `;
            return;
        }

        pedidos.forEach(p => {
            const tr = document.createElement('tr');
            const colorBadge = p.estado === 'PENDIENTE' ? 'badge-pending' : 'badge-completed';
            tr.innerHTML = `
                <td><span class="fw-bold text-white">#${p.numeroPedido}</span></td>
                <td>${p.cliente.nombre} ${p.cliente.apellidos}</td>
                <td>${p.departamentoOrigen} &rarr; ${p.departamentoDestino}</td>
                <td><span class="badge bg-secondary">${p.numeroCajas} cajas</span></td>
                <td><span class="badge-custom ${colorBadge}">${p.estado}</span></td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-info me-1" onclick="verDetallePedido(${p.numeroPedido})">Ver Detalle</button>
                    ${p.estado === 'PENDIENTE' ? `<button class="btn btn-sm btn-outline-success" onclick="completarPedido(${p.numeroPedido})">Completar</button>` : ''}
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) {
        console.error('Error cargando pedidos:', e);
    }
}

function inicializarEventos() {
    document.getElementById('pedido-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const dpiCliente = document.getElementById('cliente-select').value;
        const departamentoOrigen = document.getElementById('departamento-origen').value;
        const departamentoDestino = document.getElementById('departamento-destino').value;
        const cantidadCajas = parseInt(document.getElementById('cantidad-cajas-pedido').value);

        if (!dpiCliente) {
            showToast('Por favor, selecciona un cliente.', 'warning');
            return;
        }

        const body = { dpiCliente, departamentoOrigen, departamentoDestino, cantidadCajas };

        try {
            await api.post('/pedidos', body);
            showToast('Pedido creado y procesado exitosamente.', 'success');
            document.getElementById('pedido-form').reset();
            cargarPedidos();
            cargarEstadoEstructuras();
        } catch(err) {
            // Manejado
        }
    });
}

async function verDetallePedido(numeroPedido) {
    activePedidoId = numeroPedido;
    try {
        const p = await api.get(`/pedidos/${numeroPedido}`);
        
        document.getElementById('modal-titulo').innerText = `Detalles del Pedido #${p.numeroPedido} [${p.estado}]`;
        
        // Cliente
        document.getElementById('det-cliente-nombre').innerText = `${p.cliente.nombre} ${p.cliente.apellidos}`;
        document.getElementById('det-cliente-dpi').innerText = p.cliente.dpi;
        document.getElementById('det-cliente-telefono').innerText = p.cliente.telefono;
        document.getElementById('det-cliente-direccion').innerText = p.cliente.direccion;

        // Repartidor
        document.getElementById('det-repartidor-nombre').innerText = `${p.repartidor.nombre} ${p.repartidor.apellidos}`;
        document.getElementById('det-repartidor-licencia').innerText = `Licencia ${p.repartidor.licencia}`;

        // Vehículo
        document.getElementById('det-vehiculo-marca').innerText = `${p.vehiculo.marca} ${p.vehiculo.modelo} (${p.vehiculo.anio})`;
        document.getElementById('det-vehiculo-placa').innerText = p.vehiculo.placa;
        document.getElementById('det-vehiculo-trans').innerText = p.vehiculo.tipoTransmision;

        // Cajas
        document.getElementById('det-cajas-cantidad').innerText = p.numeroCajas;
        
        const cajas = await api.get(`/pedidos/${numeroPedido}/cajas`);
        const tbodyCajas = document.getElementById('det-cajas-tabla');
        tbodyCajas.innerHTML = '';
        if (cajas && cajas.length > 0) {
            cajas.forEach(c => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td><code class="text-info">#${c.correlativo}</code></td>
                    <td>${c.fechaIngreso}</td>
                `;
                tbodyCajas.appendChild(tr);
            });
        } else {
            tbodyCajas.innerHTML = '<tr><td colspan="2" class="text-center text-secondary">No hay cajas asignadas.</td></tr>';
        }

        // Botón completar en modal
        const btnCompletar = document.getElementById('det-btn-completar');
        if (p.estado === 'PENDIENTE') {
            btnCompletar.classList.remove('d-none');
        } else {
            btnCompletar.classList.add('d-none');
        }

        modalDetalle.show();
    } catch(e) {
        console.error('Error cargando detalles del pedido:', e);
    }
}

async function completarPedido(numeroPedido) {
    if (confirm(`¿Está seguro de completar el pedido #${numeroPedido}? El repartidor y el vehículo volverán a sus colas.`)) {
        try {
            await api.put(`/pedidos/${numeroPedido}/completar`, {});
            showToast(`Pedido #${numeroPedido} completado con éxito.`, 'success');
            cargarPedidos();
            cargarEstadoEstructuras();
        } catch(e) {}
    }
}

async function completarPedidoDesdeModal() {
    if (activePedidoId) {
        try {
            await api.put(`/pedidos/${activePedidoId}/completar`, {});
            showToast(`Pedido #${activePedidoId} completado con éxito.`, 'success');
            modalDetalle.hide();
            cargarPedidos();
            cargarEstadoEstructuras();
        } catch(e) {}
    }
}
