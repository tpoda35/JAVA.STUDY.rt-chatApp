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
                toastr.error('Invalid credentials.', 'Error');
            }
            throw new Error('Login failed');
        }

        console.log('Login successful');
        window.location.href = '/home';
    } catch (error) {
        console.error('Login error:', error);
    }
});

document.getElementById('google-login-btn').addEventListener('click', () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
});