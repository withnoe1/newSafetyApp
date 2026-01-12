/**
 * Triggers when the Lookup button is clicked
 */
// Clean version of permit-lookup.js
function lookupPermit() {
    console.log("Lookup function triggered!"); // This confirms the function is found

    const permitInput = document.getElementById('permitNumber');
    if (!permitInput) {
        console.error("HTML Error: Could not find 'permitNumber' input.");
        return;
    }

    const permitValue = permitInput.value;

    fetch(`/api/permit-lookup?number=${permitValue}`)
        .then(response => {
            if (!response.ok) throw new Error("Not found");
            return response.json();
        })
        .then(data => {
            console.log("Success! Data received:", data);

            // Safe helper to fill fields
            const setField = (id, value) => {
                const el = document.getElementById(id);
                if (el) el.value = value || '';
            };

            // Map MySQL fields (Using the names you confirmed worked before)
            setField('v_plate', data.plate);
            setField('v_vin', data.vin);
            setField('v_state', data.state);
            setField('v_make', data.make);
            setField('v_color', data.color);

            // Map Oracle fields
            if (data.id) {
                setField('p_id', data.id);
                setField('p_name', data.fullName);
                setField('p_dept', data.depart);
                setField('p_title', data.title);
                setField('p_loc', data.location);
                setField('p_phone', data.WorkPhone);

                const section = document.getElementById('personSection');
                if (section) section.classList.remove('d-none');
            }
        })
        .catch(err => {
            console.error("Error during lookup:", err);
            alert("Permit lookup failed.");
        });
}
function validateMilitaryTime(input) {
    const val = input.value;
    const errorDiv = document.getElementById('time_error');

    // Regex explanation:
    // ^([01]\d|2[0-3]) -> First two digits: 00-19 or 20-23
    // ([0-5]\d)$        -> Last two digits: 00-59
    const militaryTimeRegex = /^([01]\d|2[0-3])([0-5]\d)$/;

    if (val === "") {
        errorDiv.classList.add('d-none');
        input.classList.remove('is-invalid');
        return;
    }

    if (!militaryTimeRegex.test(val)) {
        errorDiv.classList.remove('d-none');
        input.classList.add('is-invalid');
        // Optional: clear the field if it's wrong
        // input.value = "";
    } else {
        errorDiv.classList.add('d-none');
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
    }
}