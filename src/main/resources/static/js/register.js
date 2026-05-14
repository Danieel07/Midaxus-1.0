// Registration page JavaScript

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
    notification.innerHTML = '<i class="fas fa-check-circle"></i> ' + message;
  } else if (type === 'error') {
    notification.style.backgroundColor = '#930606';
    notification.innerHTML = '<i class="fas fa-exclamation-circle"></i> ' + message;
  } else {
    notification.style.backgroundColor = '#2196F3';
    notification.innerHTML = '<i class="fas fa-info-circle"></i> ' + message;
  }
  
  document.body.appendChild(notification);
  
  setTimeout(() => {
    notification.style.animation = 'slideOut 0.3s ease-out';
    setTimeout(() => notification.remove(), 300);
  }, 3000);
}

document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');

    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const name = document.getElementById('name').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            // Basic validation
            if (!name || !email || !password || !confirmPassword) {
                showNotification('LLena todos los campos', 'error');
                return;
            }

            if (password !== confirmPassword) {
                showNotification('Las contraseñas no coinciden', 'error');
                return;
            }

            if (password.length < 6) {
                showNotification('La contraseña debe tener 6 caracteres', 'error');
                return;
            }

            showNotification('Cuenta creada satistafctoriamente! Bienvenido a Midaxus   ...', 'success');
            
            // Redirect to login page
            setTimeout(() => {
                window.location.href = '/login';
            }, 1500);
        });
    }
});

// Add animations
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