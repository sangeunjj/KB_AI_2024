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
    fetch('/api/company')
        .then(response => response.json())
        .then(data => {
            console.log(data); // 응답 데이터 확인
            const companyList = document.getElementById("companyList");
            companyList.innerHTML = ""; // 기존 리스트 초기화

            data.forEach(company => {
                const li = document.createElement("li");

                const companyInfo = document.createElement("div");
                companyInfo.classList.add("company-info");
                companyInfo.innerHTML = `
                    <strong>${company.corp_name}</strong><br>
                    <p>주소: ${company.adres}</p>
                    <p>대표: ${company.ceo_nm} | 설립일: ${company.est_dt}</p>
                    <p>여성 임원 수: ${company.femaleExecutives}명</p>
                    <p>ESG 지표: ${company.esg}</p>
                    <p>긍정/부정 점수: ${company.sentimentScore}</p>
                    <p>기업 코드: ${company.companyCode}</p>
                `;

                const selectBtn = document.createElement("span");
                selectBtn.classList.add("company-select");
                selectBtn.textContent = "선택";
                selectBtn.onclick = function() {
                    selectCompany(company.corp_name);
                };

                li.appendChild(companyInfo);
                li.appendChild(selectBtn);
                companyList.appendChild(li);
            });
        })
        .catch(error => console.error('Error:', error));
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
