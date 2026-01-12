// 1. THE FUNCTION (The logic of the upload)
async function uploadEvidence(files) {
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
        formData.append('images', files[i]);
    }

    try {
        const response = await fetch('/evidence', {
            method: 'POST',
            body: formData
        });
        if (response.ok) alert('Upload successful!');
    } catch (error) {
        console.error('Upload error:', error);
    }
}

// 2. THE TRIGGER (The Event Listener)
// This "listens" for the click on the button in your HTML
document.getElementById('uploadBtn').addEventListener('click', () => {
    const fileInput = document.getElementById('evidenceInput');
    const files = fileInput.files;

    if (files.length > 0) {
        uploadEvidence(files);
    } else {
        alert("Please select a file first.");
    }
});