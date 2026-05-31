let editandoDpi = null;

document.addEventListener('DOMContentLoaded', () => {
    cargarRepartidores();
    inicializarEventos();
});

async function cargarRepartidores() {
    const tbody = document.getElementById('tabla-repartidores');
    try {
        const repartidores = await api.get('/repartidores');
        tbody.innerHTML = '';

        if (!repartidores || repartidores.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-secondary py-4">No hay repartidores en la cola.</td>
                </tr>
            `;
            return;
        }

        repartidores.forEach((r, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><span class="badge bg-secondary">${index + 1}</span></td>
                <td><code class="text-info">${r.dpi}</code></td>
                <td>${r.nombre} ${r.apellidos}</td>
                <td><span class="badge bg-primary">Licencia ${r.licencia}</span></td>
                <td>${r.telefono}</td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="editarRepartidor('${r.dpi}', '${r.nombre}', '${r.apellidos}', '${r.licencia}', '${r.telefono}')">Editar</button>
                    <button class="btn btn-sm btn-outline-danger" onclick="eliminarRepartidor('${r.dpi}')">Eliminar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) {
        console.error('Error cargando repartidores:', e);
    }
}

function inicializarEventos() {
    // Formulario
    document.getElementById('repartidor-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const dpi = document.getElementById('dpi').value;
        const nombre = document.getElementById('nombre').value;
        const apellidos = document.getElementById('apellidos').value;
        const licencia = document.getElementById('licencia').value;
        const telefono = document.getElementById('telefono').value;

        const body = { dpi, nombre, apellidos, licencia, telefono };

        try {
            if (editandoDpi) {
                await api.put(`/repartidores/${editandoDpi}`, body);
                showToast('Repartidor modificado correctamente.', 'success');
                cancelarEdicion();
            } else {
                await api.post('/repartidores', body);
                showToast('Repartidor agregado a la cola FIFO.', 'success');
                limpiarFormulario();
            }
            cargarRepartidores();
        } catch(err) {
            // Manejado
        }
    });

    // CSV
    const uploadArea = document.getElementById('upload-area');
    const fileInput = document.getElementById('csv-file');

    uploadArea.addEventListener('click', () => fileInput.click());

    fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            procesarCSV(file);
        }
    });

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
            await api.postCarga('/repartidores/carga', text);
            showToast('Repartidores cargados masivamente.', 'success');
            cargarRepartidores();
        } catch(err) {
            // Manejado
        }
    };
    reader.readAsText(file, 'UTF-8');
}

function editarRepartidor(dpi, nombre, apellidos, licencia, telefono) {
    editandoDpi = dpi;
    
    document.getElementById('dpi').value = dpi;
    document.getElementById('dpi').disabled = true; // Bloqueamos DPI en edicion de la cola
    document.getElementById('nombre').value = nombre;
    document.getElementById('apellidos').value = apellidos;
    document.getElementById('licencia').value = licencia;
    document.getElementById('telefono').value = telefono;

    document.getElementById('form-title').innerText = 'Modificar Repartidor';
    document.getElementById('btn-submit').innerText = 'Actualizar Cambios';
    document.getElementById('btn-cancel').classList.remove('d-none');
}

function cancelarEdicion() {
    editandoDpi = null;
    document.getElementById('dpi').disabled = false;
    limpiarFormulario();
    document.getElementById('form-title').innerText = 'Registrar Repartidor';
    document.getElementById('btn-submit').innerText = 'Guardar Repartidor';
    document.getElementById('btn-cancel').classList.add('d-none');
}

function limpiarFormulario() {
    document.getElementById('dpi').value = '';
    document.getElementById('nombre').value = '';
    document.getElementById('apellidos').value = '';
    document.getElementById('licencia').value = '';
    document.getElementById('telefono').value = '';
}

async function eliminarRepartidor(dpi) {
    if (confirm(`¿Está seguro de eliminar al repartidor con DPI ${dpi} de la cola?`)) {
        try {
            await api.delete(`/repartidores/${dpi}`);
            showToast('Repartidor eliminado de la cola.', 'success');
            cargarRepartidores();
        } catch(e) {}
    }
}
