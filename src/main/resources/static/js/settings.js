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