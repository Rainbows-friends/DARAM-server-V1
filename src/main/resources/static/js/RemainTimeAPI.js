document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("fetchTimeButton").addEventListener("click", function () {
        console.log('버튼 클릭됨 - 남은 시간 가져오기 API 호출 시작');
        fetch('http://localhost:5000/times/remaintime')
            .then(response => {
                console.log('API 응답 수신');
                if (!response.ok) {
                    throw new Error('네트워크 응답에 문제가 있습니다: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (data.success === "성공") {
                    console.log('API 호출 성공:', data);
                    const {hours, minutes, seconds} = data.data;
                    startCountdown(hours, minutes, seconds);
                } else {
                    console.error('시간을 불러오는 중 오류 발생:', data.massage);
                    document.getElementById('time').textContent = '시간을 불러오는 중 오류가 발생했습니다.';
                }
            })
            .catch(error => {
                console.error('오류:', error);
                document.getElementById('time').textContent = '시간을 불러오는 중 오류가 발생했습니다.';
            });
    });
});

function startCountdown(hours, minutes, seconds) {
    const hoursElement = document.getElementById('hours');
    const minutesElement = document.getElementById('minutes');
    const secondsElement = document.getElementById('seconds');

    let totalSeconds = hours * 3600 + minutes * 60 + seconds;

    const updateCountdown = () => {
        if (totalSeconds <= 0) {
            clearInterval(interval);
            return;
        }
        totalSeconds--;

        const hrs = Math.floor(totalSeconds / 3600);
        const mins = Math.floor((totalSeconds % 3600) / 60);
        const secs = totalSeconds % 60;

        hoursElement.textContent = String(hrs).padStart(2, '0');
        minutesElement.textContent = String(mins).padStart(2, '0');
        secondsElement.textContent = String(secs).padStart(2, '0');
    };

    updateCountdown();
    const interval = setInterval(updateCountdown, 1000);
}