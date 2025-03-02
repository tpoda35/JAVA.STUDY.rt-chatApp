document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".settings-nav-link");
    const sections = document.querySelectorAll(".settings-content-section");

    buttons.forEach(button => {
        button.addEventListener("click", function () {
            // Hide all sections
            sections.forEach(section => section.classList.add("d-none"));

            // Show the selected section
            const targetSection = document.getElementById(button.dataset.section);
            targetSection.classList.remove("d-none");

            // Optional: Add active class to the clicked button
            buttons.forEach(btn => btn.classList.remove("active"));
            button.classList.add("active");
        });
    });

    // Show the first section by default
    if (buttons.length > 0) {
        buttons[0].click();
    }
});

document.getElementById('saveDNameForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const newDisplayName = document.querySelector('#displayName').value;

    fetch('http://localhost:8080/api/v1/settings/changeDisplayName?newDisplayName=' + encodeURIComponent(newDisplayName), {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    const errorMessage = errorData.message;
                    toastr.error(errorMessage, 'Error');
                    throw new Error(errorMessage);
                });
            }
            return response;
        })
        .then(() => {
            sessionStorage.setItem('displayNameChanged', 'true');
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });
});