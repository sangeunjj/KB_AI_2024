function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    const content = document.getElementById("content");
    sidebar.classList.toggle("active");
    content.classList.toggle("expanded");
}

document.getElementById('user-input').addEventListener('keydown', function (event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault(); // 기본 동작(줄바꿈) 방지
        sendMessage();
    }
});

function autoGrow(element) {
    element.style.height = "auto"; // 초기 높이를 자동으로 설정
    const maxHeight = parseInt(window.getComputedStyle(element).lineHeight) * 5; // 최대 5줄 높이 설정
    element.style.height = Math.min(element.scrollHeight, maxHeight) + "px";

    if (element.scrollHeight > maxHeight) {
        element.style.overflowY = "scroll"; // 스크롤 활성화
    } else {
        element.style.overflowY = "hidden"; // 스크롤 비활성화
    }

    // 부모 요소의 높이를 입력 칸의 높이에 맞춰 조정
    const parent = element.parentElement;
    parent.style.height = element.style.height;

    // 입력란이 위로 확장되도록 스크롤 위치 조정
    const chatContainer = document.querySelector('.chat-container');
    chatContainer.scrollTop = chatContainer.scrollHeight - chatContainer.clientHeight;
}

function sendMessage() {
    const userInput = document.getElementById('user-input');
    const chatBox = document.getElementById('chat-box');
    const message = userInput.value.trim();

    if (message) {
        // 사용자 메시지 표시
        const userMessage = document.createElement('div');
        userMessage.classList.add('message', 'user-message');
        userMessage.textContent = message;
        chatBox.appendChild(userMessage);

        // 입력 필드 초기화
        userInput.value = '';
        chatBox.scrollTop = chatBox.scrollHeight;

        // 봇 메시지 자리 표시자 생성
        const botMessage = document.createElement('div');
        botMessage.classList.add('message', 'bot-message');
        const botImage = document.createElement('img');
        botImage.src = "/images/kb-logo.png";
        botImage.alt = "Bot Profile Image";
        botMessage.appendChild(botImage);
        const botText = document.createElement('span');
        botText.textContent = 'GPT 응답 중...'; // 자리 표시자 텍스트
        botMessage.appendChild(botText);
        chatBox.appendChild(botMessage);

        chatBox.scrollTop = chatBox.scrollHeight;

        // 프롬프트를 백엔드로 전송
        fetch(`/bot/chat?prompt=${encodeURIComponent(message)}`)
            .then(response => response.text())
            .then(data => {
                // 자리 표시자 텍스트를 실제 GPT 응답으로 교체
                botText.textContent = data;
            })
            .catch(error => {
                botText.textContent = "오류가 발생했습니다. 다시 시도해 주세요.";
                console.error("Error:", error);
            });
    }
}
