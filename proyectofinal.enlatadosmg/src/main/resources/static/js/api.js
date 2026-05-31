const API_BASE = '/api';

const api = {
    getHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        };
        const usuarioId = sessionStorage.getItem('usuarioId');
        if (usuarioId) {
            headers['X-Usuario-Id'] = usuarioId;
        }
        return headers;
    },

    async request(url, options = {}) {
        options.headers = {
            ...this.getHeaders(),
            ...options.headers
        };

        try {
            const response = await fetch(`${API_BASE}${url}`, options);
            
            const contentType = response.headers.get('Content-Type') || '';
            let data;
            if (contentType.includes('application/json')) {
                data = await response.json();
            } else {
                data = await response.text();
            }

            if (!response.ok) {
                if (response.status === 401 && !url.includes('/auth/login')) {
                    sessionStorage.clear();
                    window.location.href = '/login.html';
                    return;
                }
                // Si la respuesta no es OK, intentar leer el error
                let errorMsg = 'Error en el servidor';
                if (typeof data === 'object' && data.error) {
                    errorMsg = data.error;
                } else if (typeof data === 'string' && data.trim().startsWith('{')) {
                    try {
                        const parsed = JSON.parse(data);
                        if (parsed.error) errorMsg = parsed.error;
                    } catch(e) {}
                } else if (typeof data === 'string' && data.length > 0) {
                    errorMsg = data;
                }
                throw new Error(errorMsg);
            }
            return data;
        } catch (error) {
            console.error('API Error:', error);
            showToast(error.message || 'Error de conexión', 'danger');
            throw error;
        }
    },

    get(url) {
        return this.request(url, { method: 'GET' });
    },

    post(url, body) {
        return this.request(url, {
            method: 'POST',
            body: typeof body === 'string' ? body : JSON.stringify(body)
        });
    },

    put(url, body) {
        return this.request(url, {
            method: 'PUT',
            body: typeof body === 'string' ? body : JSON.stringify(body)
        });
    },

    delete(url) {
        return this.request(url, { method: 'DELETE' });
    },

    postCarga(url, textContent) {
        return this.request(url, {
            method: 'POST',
            headers: { 'Content-Type': 'text/plain;charset=UTF-8' },
            body: textContent
        });
    }
};

function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toastId = 'toast_' + Date.now();
    const bgClass = type === 'danger' ? 'bg-danger' : (type === 'warning' ? 'bg-warning text-dark' : 'bg-success');
    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white ${bgClass} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', toastHtml);

    const toastElement = document.getElementById(toastId);
    const bsToast = new bootstrap.Toast(toastElement, { delay: 4000 });
    bsToast.show();

    toastElement.addEventListener('hidden.bs.toast', () => {
        toastElement.remove();
    });
}
