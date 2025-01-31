"use strict";

document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Get input values
    const email = document.getElementById('email');
    const password = document.getElementById('password');

    // Basic validation
    if (email.value.trim() === '') {
        email.classList.add('is-invalid');
        return;
    } else {
        email.classList.remove('is-invalid');
    }

    if (password.value.trim() === '') {
        password.classList.add('is-invalid');
        return;
    } else {
        password.classList.remove('is-invalid');
    }

    console.log(email.value, password.value)

    try {
        const response = await fetch('http://localhost:8080/api/v1/auth/authenticate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                 email: email.value,
                 password: password.value
            })
        });

        if (!response.ok) {
             throw new Error('Login failed');
        }

        window.location.href = 'index.html'
        const data = await response.json();
        console.log('Login successful', data);
    } catch (error) {
        console.error('Login error:', error);
    }
});

function redirectToGoogle() {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
}