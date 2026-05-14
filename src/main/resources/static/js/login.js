// Login Page JavaScript

function showNotification(message, type = 'info') {

  const notification = document.createElement('div');

  notification.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 1rem 1.5rem;
    border-radius: 6px;
    font-weight: 600;
    color: white;
    z-index: 1000;
    animation: slideIn 0.3s ease-out;
  `;

  if (type === 'success') {
    notification.style.backgroundColor = '#3b9a95';
  } else if (type === 'error') {
    notification.style.backgroundColor = '#930606';
  } else {
    notification.style.backgroundColor = '#2196F3';
  }

  notification.innerHTML = message;

  document.body.appendChild(notification);

  setTimeout(() => {
    notification.style.animation = 'slideOut 0.3s ease-out';
    setTimeout(() => notification.remove(), 300);
  }, 3000);
}


document.addEventListener('DOMContentLoaded', () => {

  const loginForm = document.getElementById('loginForm');

  if (!loginForm) return;

  loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const rememberEl = document.getElementById('remember');
    const remember = rememberEl ? rememberEl.checked : false;

    if (!email || !password) {
      showNotification('Por favor complete todos los campos', 'error');
      return;
    }

    try {

      const urlParams = new URLSearchParams(window.location.search);
      const urlRole = urlParams.get('role');
      let expectedRole = 'STUDENT';
      if (urlRole === 'teacher') expectedRole = 'TEACHER';
      else if (urlRole === 'admin') expectedRole = 'ADMIN';

      const response = await fetch('/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: email,
          password: password,
          expectedRole: expectedRole
        })
      });

      const data = await response.json();

      // Validación REAL del éxito
      if (!data.success) {
        showNotification(data.message, 'error');
        return;
      }

      showNotification('Login exitoso, redirigiendo...', 'success');

      if (remember) {
        localStorage.setItem("user", JSON.stringify(data));
      } else {
        sessionStorage.setItem("user", JSON.stringify(data));
      }

      setTimeout(() => {
        window.location.href = "/dashboard";
      }, 1200);

    } catch (error) {
      console.error("Error login:", error);
      showNotification('Error en el servidor o red', 'error');
    }
  });

});


// Animaciones de notificación

const style = document.createElement('style');

style.textContent = `
@keyframes slideIn {
  from {
    transform: translateX(400px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes slideOut {
  from {
    transform: translateX(0);
    opacity: 1;
  }
  to {
    transform: translateX(400px);
    opacity: 0;
  }
}
`;

document.head.appendChild(style);