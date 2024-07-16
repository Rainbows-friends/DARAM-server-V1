document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("fetchTimeButton").addEventListener("click", function () {
        fetch('/times/remaintime')
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답에 문제가 있습니다: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (data.success === "성공") {
                    const timeElement = document.getElementById('time');
                    const {hours, minutes, seconds} = data.data;
                    timeElement.textContent = `남은 시간 - 시: ${hours}, 분: ${minutes}, 초: ${seconds}`;
                } else {
                    console.error('시간을 불러오는 중 오류 발생:', data.message);
                    document.getElementById('time').textContent = '시간을 불러오는 중 오류가 발생했습니다.';
                }
            })
            .catch(error => {
                console.error('오류:', error);
                document.getElementById('time').textContent = '시간을 불러오는 중 오류가 발생했습니다.';
            });
    });
});