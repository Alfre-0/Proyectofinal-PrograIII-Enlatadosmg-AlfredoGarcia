document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;
    const usuarioId = sessionStorage.getItem('usuarioId');

    const esPaginaLogin = path.endsWith('login.html') || path.endsWith('login');
    const esRaiz = path === '/' || path.endsWith('index.html') || path.endsWith('index');

    if (!usuarioId) {
        if (!esPaginaLogin && !esRaiz) {
            window.location.href = '/login.html';
            return;
        }
    } else {
        if (esPaginaLogin || esRaiz) {
            window.location.href = '/dashboard.html';
            return;
        }
        cargarPerfilNavbar();
    }
});

function logout() {
    sessionStorage.clear();
    window.location.href = '/login.html';
}

async function cargarPerfilNavbar() {
    const usuarioId = sessionStorage.getItem('usuarioId');
    if (!usuarioId) return;

    try {
        const u = await api.get(`/auth/perfil/${usuarioId}`);
        const el = document.getElementById('navbar-user-name');
        if (el && u) {
            el.innerText = `${u.nombre} ${u.apellidos} (ID: ${u.id})`;
        }
    } catch(e) {
        console.error('Error cargando perfil del navbar:', e);
    }
}
