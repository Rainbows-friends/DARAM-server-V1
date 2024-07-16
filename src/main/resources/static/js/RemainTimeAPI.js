document.addEventListener("DOMContentLoaded", function () {
    fetch('http://localhost:5000/times/remaintime')
        .then(response => response.json())
        .then(data => {
            if (data.success === "성공") {
                const timeElement = document.getElementById('time');
                const {hours, minutes, seconds} = data.data;
                timeElement.textContent = `Hours: ${hours}, Minutes: ${minutes}, Seconds: ${seconds}`;
            } else {
                console.error('Error fetching time:', data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            const timeElement = document.getElementById('time');
            timeElement.textContent = 'Failed to load time.';
        });
});