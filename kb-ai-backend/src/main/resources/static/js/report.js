// 모달 열기
function openModal() {
    document.getElementById("companyModal").style.display = "block";
    loadCompanies();
}

// 모달 닫기
function closeModal() {
    document.getElementById("companyModal").style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target === document.getElementById("companyModal")) {
        closeModal();
    }
}

// API에서 회사 목록을 가져와서 모달에 표시
function loadCompanies() {
    fetch('/api/company/ABC')
        .then(response => response.json())
        .then(data => {
            console.log(data); // 응답 데이터 확인

            const companyList1 = document.getElementById("companyList1");
            const companyList2 = document.getElementById("companyList2");
            const companyList3 = document.getElementById("companyList3");

            // 리스트 초기화
            companyList1.innerHTML = "";
            companyList2.innerHTML = "";
            companyList3.innerHTML = "";

            let currentGroup = ''; // 현재 자음 그룹

            data.forEach((companyName, index) => {
                const firstLetter = getFirstLetter(companyName); // 첫 글자 또는 자음 추출

                // 새로운 그룹이 시작되면 그룹 헤더를 추가
                if (firstLetter !== currentGroup) {
                    currentGroup = firstLetter;

                    // 그룹 헤더를 각 리스트에 추가
                    const groupHeader = document.createElement("div");
                    groupHeader.textContent = currentGroup;
                    groupHeader.classList.add("group-header");

                    if (index % 3 === 0) companyList1.appendChild(groupHeader);
                    else if (index % 3 === 1) companyList2.appendChild(groupHeader);
                    else companyList3.appendChild(groupHeader);
                }

                // 기업명 항목 추가
                const li = document.createElement("div");
                li.textContent = companyName;
                li.classList.add("company-item");

                // 회사 선택 시 버튼에 이름 표시
                li.onclick = function() {
                    selectCompany(companyName);
                };

                // 기업명을 3개의 리스트에 나누어 추가
                if (index % 3 === 0) companyList1.appendChild(li);
                else if (index % 3 === 1) companyList2.appendChild(li);
                else companyList3.appendChild(li);
            });
        })
        .catch(error => console.error('Error:', error));
}

// 한글의 첫 자음 또는 영어의 첫 글자를 추출하는 함수
function getFirstLetter(str) {
    const firstChar = str.charAt(0);
    const unicode = firstChar.charCodeAt(0) - 44032;

    if (unicode >= 0 && unicode <= 11171) { // 한글 초성 범위 확인
        const consonants = ["ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"];
        const firstConsonantIndex = Math.floor(unicode / 588);
        return consonants[firstConsonantIndex];
    } else {
        return firstChar.toUpperCase(); // 영어의 경우 첫 글자를 반환 (대문자로 변환)
    }
}

// 회사 선택 시 버튼에 이름 표시
// 처음으로 + 표시가 있는 버튼을 찾아 그 버튼의 텍스트를 선택한 회사 이름으로 변경

function selectCompany(companyName) {
    const buttons = document.querySelectorAll('.button');
    const emptyButton = Array.from(buttons).find(button => button.textContent.trim() === '+');
    if (emptyButton) {
        emptyButton.textContent = companyName;
        closeModal();
    }
}

// 피처 선택 버튼 (여러 개 선택 가능)
document.querySelectorAll('.feature-button').forEach(button => {
    button.addEventListener('click', function() {
        // 현재 클릭된 버튼에 'selected' 클래스가 있으면 제거, 없으면 추가
        this.classList.toggle('selected');
    });
});

