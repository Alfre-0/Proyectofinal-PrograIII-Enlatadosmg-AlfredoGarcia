document.addEventListener('DOMContentLoaded', () => {
    cargarAlmacen();
});

async function cargarAlmacen() {
    const tbody = document.getElementById('tabla-almacen');
    const totalEl = document.getElementById('total-cajas');
    try {
        const cajas = await api.get('/almacen');
        tbody.innerHTML = '';
        totalEl.innerText = `${cajas.length} caja${cajas.length === 1 ? '' : 's'}`;

        if (!cajas || cajas.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="3" class="text-center text-secondary py-4">El almacén está vacío. Agrega stock arriba.</td>
                </tr>
            `;
            return;
        }

        cajas.forEach((c, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index === 0 ? '<span class="badge bg-warning text-dark animate-pulse">CIMA</span>' : ''}</td>
                <td><code class="text-info">#${c.correlativo}</code></td>
                <td>${c.fechaIngreso}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) {
        console.error('Error cargando almacén:', e);
    }
}

async function generarCajas() {
    const cantInput = document.getElementById('cantidad-cajas');
    const cantidad = parseInt(cantInput.value);

    if (!cantidad || cantidad <= 0) {
        showToast('Ingrese una cantidad válida mayor que cero.', 'warning');
        return;
    }

    try {
        const res = await api.post(`/almacen/generar/${cantidad}`);
        showToast(res.mensaje || `Se generaron ${cantidad} cajas con éxito.`, 'success');
        cantInput.value = '';
        cargarAlmacen();
    } catch(e) {}
}

async function apilarCaja() {
    try {
        const c = await api.post('/almacen/push');
        showToast(`Caja #${c.correlativo} apilada correctamente.`, 'success');
        cargarAlmacen();
    } catch(e) {}
}

async function desapilarCaja() {
    try {
        const c = await api.post('/almacen/pop');
        
        // Mostrar alerta de la caja desapilada
        const alertBox = document.getElementById('alert-popped');
        alertBox.classList.remove('d-none');
        document.getElementById('popped-correlativo').innerText = `#${c.correlativo}`;
        document.getElementById('popped-fecha').innerText = c.fechaIngreso;

        showToast(`Caja #${c.correlativo} extraída del almacén.`, 'success');
        cargarAlmacen();
    } catch(e) {}
}
