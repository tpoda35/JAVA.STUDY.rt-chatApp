"use strict";

document.getElementById('registerForm').addEventListener('submit', async function (e) {
    e.preventDefault(); // Prevent default submission

    // Get input values
    const fName = document.getElementById('fname');
    const lName = document.getElementById('lname');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const cPassword = document.getElementById('cpassword');
    const message = document.getElementById('message');

    // Reset previous error messages
    message.style.display = 'none';
    message.textContent = "";

    let isValid = true; // Track validation status

    // Password match check
    if (password.value !== cPassword.value) {
        message.style.display = 'block';
        message.textContent = "Passwords do not match!";
        isValid = false;
    }

    // Basic validation
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
        return; // Stop execution if validation fails
    }

    try {
        const response = await fetch('http://localhost:8080/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                firstname: fName.value,
                lastname: lName.value,
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
            data = await response.text(); // Handle non-JSON responses
        }

        if (!response.ok) {
            message.textContent = data.error;
            message.style.display = 'block';
            throw new Error(data.error);
        }

        console.log('Registration successful');
    } catch (error) {
        console.error('Registration error:', error);
    }
});
