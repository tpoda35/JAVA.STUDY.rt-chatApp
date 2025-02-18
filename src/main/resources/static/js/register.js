"use strict";

document.getElementById('registerForm').addEventListener('submit', async function (e) {
    e.preventDefault(); // Prevent default submission

    // Get input values
    const dName = document.getElementById('dName');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const cPassword = document.getElementById('cpassword');

    let isValid = true; // Track validation status

    // Password match check
    if (password.value !== cPassword.value) {
        toastr.error('Passwords does not match.', 'Error');
        isValid = false;
    }

    if (dName.value.trim() === '') {
        dName.classList.add('is-invalid');
        isValid = false;
    } else {
        dName.classList.remove('is-invalid');
    }

    if (email.value.trim() === '') {
        email.classList.add('is-invalid');
        isValid = false;
    } else {
        email.classList.remove('is-invalid');
    }

    if (password.value.trim() === '') {
        password.classList.add('is-invalid');
        isValid = false;
    } else {
        password.classList.remove('is-invalid');
    }

    if (!isValid) {
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                displayName: dName.value,
                email: email.value,
                password: password.value,
                confirmPassword: cPassword.value
            })
        });

        const contentType = response.headers.get('Content-Type');
        let data;
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        if (!response.ok) {
            toastr.error(data.error, 'Error');
            throw new Error(data.error);
        }

        toastr.success('Redirecting to login page.', 'Successful registration.');
        setTimeout(function() {
            window.location.href = '/';
        }, 2000);
    } catch (error) {
        console.error('Registration error:', error);
    }
});
