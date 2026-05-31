let editandoDpi = null;

document.addEventListener('DOMContentLoaded', () => {
    cargarClientes();
    inicializarEventos();
});

async function cargarClientes() {
    const tbody = document.getElementById('tabla-clientes');
    try {
        const clientes = await api.get('/clientes');
        tbody.innerHTML = '';

        if (!clientes || clientes.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center text-secondary py-4">No hay clientes registrados en el árbol AVL.</td>
                </tr>
            `;
            return;
        }

        clientes.forEach(c => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><code class="text-info">${c.dpi}</code></td>
                <td>${c.nombre} ${c.apellidos}</td>
                <td>${c.telefono}</td>
                <td>${c.direccion}</td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="editarCliente('${c.dpi}', '${c.nombre}', '${c.apellidos}', '${c.telefono}', '${c.direccion}')">Editar</button>
                    <button class="btn btn-sm btn-outline-danger" onclick="eliminarCliente('${c.dpi}')">Eliminar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) {
        console.error('Error cargando clientes:', e);
    }
}

function inicializarEventos() {
    // Formulario de creación/edición
    document.getElementById('cliente-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const dpi = document.getElementById('dpi').value;
        const nombre = document.getElementById('nombre').value;
        const apellidos = document.getElementById('apellidos').value;
        const telefono = document.getElementById('telefono').value;
        const direccion = document.getElementById('direccion').value;

        const body = { dpi, nombre, apellidos, telefono, direccion };

        try {
            if (editandoDpi) {
                // Modificar cliente
                await api.put(`/clientes/${editandoDpi}`, body);
                showToast('Cliente modificado correctamente.', 'success');
                cancelarEdicion();
            } else {
                // Crear cliente
                await api.post('/clientes', body);
                showToast('Cliente registrado correctamente.', 'success');
                limpiarFormulario();
            }
            cargarClientes();
        } catch(err) {
            // El error lo maneja api.js automáticamente
        }
    });

    // Zona de Carga Masiva (Click para seleccionar archivo)
    const uploadArea = document.getElementById('upload-area');
    const fileInput = document.getElementById('csv-file');

    uploadArea.addEventListener('click', () => fileInput.click());

    fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            procesarCSV(file);
        }
    });

    // Soporte para arrastrar y soltar archivos (Drag and Drop)
    uploadArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        uploadArea.style.borderColor = 'var(--accent-purple)';
    });

    uploadArea.addEventListener('dragleave', () => {
        uploadArea.style.borderColor = 'rgba(255, 255, 255, 0.15)';
    });

    uploadArea.addEventListener('drop', (e) => {
        e.preventDefault();
        uploadArea.style.borderColor = 'rgba(255, 255, 255, 0.15)';
        const file = e.dataTransfer.files[0];
        if (file) {
            procesarCSV(file);
        }
    });
}

function procesarCSV(file) {
    const reader = new FileReader();
    reader.onload = async (e) => {
        const text = e.target.result;
        try {
            await api.postCarga('/clientes/carga', text);
            showToast('Carga masiva finalizada con éxito.', 'success');
            cargarClientes();
        } catch(err) {
            // Manejado
        }
    };
    reader.readAsText(file, 'UTF-8');
}

function editarCliente(dpi, nombre, apellidos, telefono, direccion) {
    editandoDpi = dpi;
    
    document.getElementById('dpi').value = dpi;
    // Bloqueamos el campo DPI en modo edición para evitar que cambie la llave primaria del cliente.
    // Nuestra API permite el cambio (elimina e inserta), pero por experiencia de usuario es mejor dejarlo editable.
    document.getElementById('nombre').value = nombre;
    document.getElementById('apellidos').value = apellidos;
    document.getElementById('telefono').value = telefono;
    document.getElementById('direccion').value = direccion;

    document.getElementById('form-title').innerText = 'Modificar Cliente';
    document.getElementById('btn-submit').innerText = 'Actualizar Cambios';
    document.getElementById('btn-cancel').classList.remove('d-none');
}

function cancelarEdicion() {
    editandoDpi = null;
    limpiarFormulario();
    document.getElementById('form-title').innerText = 'Registrar Cliente';
    document.getElementById('btn-submit').innerText = 'Guardar Cliente';
    document.getElementById('btn-cancel').classList.add('d-none');
}

function limpiarFormulario() {
    document.getElementById('dpi').value = '';
    document.getElementById('nombre').value = '';
    document.getElementById('apellidos').value = '';
    document.getElementById('telefono').value = '';
    document.getElementById('direccion').value = '';
}

async function eliminarCliente(dpi) {
    if (confirm(`¿Está seguro de eliminar al cliente con DPI ${dpi}?`)) {
        try {
            await api.delete(`/clientes/${dpi}`);
            showToast('Cliente eliminado del árbol AVL.', 'success');
            cargarClientes();
        } catch(e) {}
    }
}
