document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Simulate a login process (replace with actual authentication logic)
    if (username === 'om' && password === 'passcode') {
        // Add a success message and redirect after a short delay
        showMessage('Login successful! Redirecting...', 'success');
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
    } else {
        showMessage('Invalid username or password', 'error');
    }
});

function showMessage(message, type) {
    const messageBox = document.createElement('div');
    messageBox.className = `message ${type}`;
    messageBox.textContent = message;
    document.body.appendChild(messageBox);

    // Remove the message after 3 seconds
    setTimeout(() => {
        messageBox.remove();
    }, 3000);
}
