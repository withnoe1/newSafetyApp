async function submitCitation() {
    // 1. Collect form data
    const citationData = {
        // Vehicle Info (MySQL)
        plate: document.getElementById('v_plate').value,
        vin: document.getElementById('v_vin').value,
        state: document.getElementById('v_state').value,
        make: document.getElementById('v_make').value,
        color: document.getElementById('v_color').value,

        // Personnel Info (Oracle/Banner)
        pidm: document.getElementById('p_id').value, // Assuming ID stores PIDM or related reference

        // Ticket Specifics (New Info)
        chalkTime: document.getElementById('chalk_time').value,
        location: document.getElementById('location').value, // The location of the violation
        violationCode: document.getElementById('violation_code').value,
        officerId: "123", // Replace with actual logged-in user ID
        timestamp: new Date().getTime()
    };

    // 2. Wrap it in the structure the Servlet expects
    // Based on your Servlet: MutableJsonCitation citation = (MutableJsonCitation) JSONObject.toBean(...)
    // Ensure the JSON keys match the fields in MutableJsonCitation.java
    const payload = {
        citation: citationData
    };

    try {
        const response = await fetch('/ws/publicsafety/ticketing/citation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Citation Submitted Successfully!");
            window.location.reload(); // Reset form
        } else {
            const error = await response.text();
            alert("Submission failed: " + error);
        }
    } catch (err) {
        console.error("Submission Error:", err);
        alert("Network error occurred during submission.");
    }
}