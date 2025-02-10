"use strict";

document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Get input values
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const invalid = document.getElementById('invalid-cred');

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
            if (response.status == 400){
                console.log('Invalid credentials.')
                invalid.style.display = 'block';
            }
            throw new Error('Login failed');
        }

        window.location.href = '/'
        const data = await response.json();
        console.log('Login successful');
    } catch (error) {
        console.error('Login error:', error);
    }
});

document.getElementById('google-login-btn').addEventListener('click', () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
});