let editandoPlaca = null;

document.addEventListener('DOMContentLoaded', () => {
    cargarVehiculos();
    inicializarEventos();
});

async function cargarVehiculos() {
    const tbody = document.getElementById('tabla-vehiculos');
    try {
        const vehiculos = await api.get('/vehiculos');
        tbody.innerHTML = '';

        if (!vehiculos || vehiculos.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-secondary py-4">No hay vehículos en la cola.</td>
                </tr>
            `;
            return;
        }

        vehiculos.forEach((v, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><span class="badge bg-secondary">${index + 1}</span></td>
                <td><code class="text-info">${v.placa}</code></td>
                <td>${v.marca} ${v.modelo}</td>
                <td>${v.anio} - <span style="display:inline-block; width:12px; height:12px; border-radius:50%; background-color:${v.color.toLowerCase()}; border:1px solid #fff; margin-right:5px; vertical-align:middle;"></span>${v.color}</td>
                <td><span class="badge bg-info text-dark">${v.tipoTransmision}</span></td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="editarVehiculo('${v.placa}', '${v.marca}', '${v.modelo}', '${v.color}', ${v.anio}, '${v.tipoTransmision}')">Editar</button>
                    <button class="btn btn-sm btn-outline-danger" onclick="eliminarVehiculo('${v.placa}')">Eliminar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) {
        console.error('Error cargando vehículos:', e);
    }
}

function inicializarEventos() {
    // Formulario de creación/edición de vehículo
    document.getElementById('vehiculo-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const placa = document.getElementById('placa').value;
        const marca = document.getElementById('marca').value;
        const modelo = document.getElementById('modelo').value;
        const color = document.getElementById('color').value;
        const anio = parseInt(document.getElementById('anio').value);
        const tipoTransmision = document.getElementById('tipoTransmision').value;

        const body = { placa, marca, modelo, color, anio, tipoTransmision };

        try {
            if (editandoPlaca) {
                await api.put(`/vehiculos/${editandoPlaca}`, body);
                showToast('Vehículo modificado correctamente.', 'success');
                cancelarEdicion();
            } else {
                await api.post('/vehiculos', body);
                showToast('Vehículo agregado a la cola FIFO.', 'success');
                limpiarFormulario();
            }
            cargarVehiculos();
        } catch(err) {
            // El error lo maneja api.js automáticamente
        }
    });

    // Zona de carga masiva desde archivo CSV
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
            await api.postCarga('/vehiculos/carga', text);
            showToast('Vehículos cargados masivamente.', 'success');
            cargarVehiculos();
        } catch(err) {
            // El error lo maneja api.js automáticamente
        }
    };
    reader.readAsText(file, 'UTF-8');
}

function editarVehiculo(placa, marca, modelo, color, anio, tipoTransmision) {
    editandoPlaca = placa;
    
    document.getElementById('placa').value = placa;
    document.getElementById('placa').disabled = true; // Bloquear campo placa para evitar cambiar la llave primaria del vehículo
    document.getElementById('marca').value = marca;
    document.getElementById('modelo').value = modelo;
    document.getElementById('color').value = color;
    document.getElementById('anio').value = anio;
    document.getElementById('tipoTransmision').value = tipoTransmision;

    document.getElementById('form-title').innerText = 'Modificar Vehículo';
    document.getElementById('btn-submit').innerText = 'Actualizar Cambios';
    document.getElementById('btn-cancel').classList.remove('d-none');
}

function cancelarEdicion() {
    editandoPlaca = null;
    document.getElementById('placa').disabled = false;
    limpiarFormulario();
    document.getElementById('form-title').innerText = 'Registrar Vehículo';
    document.getElementById('btn-submit').innerText = 'Guardar Vehículo';
    document.getElementById('btn-cancel').classList.add('d-none');
}

function limpiarFormulario() {
    document.getElementById('placa').value = '';
    document.getElementById('marca').value = '';
    document.getElementById('modelo').value = '';
    document.getElementById('color').value = '';
    document.getElementById('anio').value = '';
    document.getElementById('tipoTransmision').value = '';
}

async function eliminarVehiculo(placa) {
    if (confirm(`¿Está seguro de eliminar al vehículo con placa ${placa} de la cola?`)) {
        try {
            await api.delete(`/vehiculos/${placa}`);
            showToast('Vehículo eliminado de la cola.', 'success');
            cargarVehiculos();
        } catch(e) {}
    }
}
