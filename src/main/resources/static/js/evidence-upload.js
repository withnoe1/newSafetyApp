// js/evidence-upload.js

async function handleEvidenceUpload(files) {
    if (files.length === 0) {
        alert("Please select at least one image.");
        return;
    }

    const formData = new FormData();

    // Append each file to the 'images' key
    for (let i = 0; i < files.length; i++) {
        formData.append('images', files[i]);
    }

    try {
        const response = await fetch('/evidence', {
            method: 'POST',
            body: formData,
            // Do NOT set headers manually; FormData sets the boundary for you
        });

        if (response.ok) {
            const data = await response.json();
            alert("Evidence uploaded successfully!");
            console.log("Server response:", data);
        } else {
            alert("Upload failed. Please try again.");
        }
    } catch (error) {
        console.error("Error uploading evidence:", error);
        alert("A connection error occurred.");
    }
}

// The Trigger: Event listener for the specific button in your form
document.addEventListener('DOMContentLoaded', () => {
    const uploadBtn = document.getElementById('uploadBtn');
    const evidenceInput = document.getElementById('evidenceInput');

    if (uploadBtn && evidenceInput) {
        uploadBtn.addEventListener('click', (e) => {
            // Prevent form submission if the button is inside a form tag
            e.preventDefault();
            handleEvidenceUpload(evidenceInput.files);
        });
    }
});