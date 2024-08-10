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
window.onclick = function (event) {
    if (event.target === document.getElementById("companyModal")) {
        closeModal();
    }
}

// 회사 선택: API에서 회사 목록을 가져와서 모달에 표시
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

            data.forEach((company, index) => {
                const firstLetter = getFirstLetter(company.companyName); // 첫 글자 또는 자음 추출

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
                li.textContent = company.companyName;
                li.classList.add("company-item");

                // 회사 선택 시 버튼에 이름 표시
                li.onclick = function () {
                    selectCompany(company.companyName, company.companyCode); // 이름과 코드를 넘김
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

// 피처 선택 버튼 (선택시 노란색으로 변함, 여러 개 선택 가능)
document.querySelectorAll('.feature-button').forEach(button => {
    button.addEventListener('click', function () {
        if (this.id === "allButton") {
            // "전체" 버튼 클릭 시 전체 노란색으로 선택됨
            const allSelected = this.classList.contains('selected');
            document.querySelectorAll('.feature-button').forEach(btn => {
                if (allSelected) {
                    btn.classList.remove('selected'); // 모두 선택 해제
                } else {
                    btn.classList.add('selected'); // 모두 선택
                }
            });
        } else {
            // 개별 버튼 클릭 시
            this.classList.toggle('selected');
        }
    });
});

// 선택한 회사 알아내기
function getSelectedCompanies() {
     // 회사 코드로 변경
    return Array.from(document.querySelectorAll('.button'))
        .filter(button => button.textContent.trim() !== '+')
        .map(button => button.dataset.companyCode);
}

// 선택한 피처 알아내기
function getSelectedFeatures() {
    return Array.from(document.querySelectorAll('.feature-button.selected'))
        .map(button => button.textContent.trim());
}

function fetchCompanyData() {
    const companies = getSelectedCompanies();
    const features = getSelectedFeatures();

    fetch(`/api/company/features?companyCodes=${companies.join(',')}&features=${features.join(',')}`)
        .then(response => response.json())
        .then(data => {
            console.log(data); // 데이터를 사용하여 비교 결과를 표시
            visualizeESGData(data);
        })
        .catch(error => console.error('Error:', error));
}

// 회사 선택 시 버튼에 이름 표시
// 처음으로 + 표시가 있는 버튼을 찾아 그 버튼의 텍스트를 선택한 회사 이름으로 변경

function selectCompany(companyName, companyCode) {
    const buttons = document.querySelectorAll('.button');
    const emptyButton = Array.from(buttons).find(button => button.textContent.trim() === '+');
    if (emptyButton) {
        emptyButton.textContent = companyName;
        emptyButton.dataset.companyCode = companyCode; // 회사 코드 저장
        closeModal();
    }
}

/* 💡 ESG 데이터 💡*/
function gradeToNumber(grade) {
    const gradeMapping = {
        "A+": 4.3, "A": 4.0, "B+": 3.3, "B": 3.0,
        "C+": 2.3, "C": 2.0, "D+": 1.3, "D": 1.0
        // 필요한 경우 더 많은 매핑 추가 가능
    };
    return gradeMapping[grade] || 0; // 매핑에 없는 경우 0으로 처리
}

function visualizeESGData(data) {
    const ctx1 = document.getElementById('chart1').getContext('2d');
    const ctx2 = document.getElementById('chart2').getContext('2d');
    const ctx3 = document.getElementById('chart3').getContext('2d');

    const labels = ['2023 E', '2023 S', '2023 G', '2023 ESG 통합', '2022 E', '2022 S', '2022 G', '2022 ESG 통합'];

    // 차트 데이터를 준비합니다.
    const chartData = data.map(company => ({
        label: company.companyName,
        data: [
            gradeToNumber(company.ESG_23_e), gradeToNumber(company.ESG_23_s), gradeToNumber(company.ESG_23_g), gradeToNumber(company.ESG_23),
            gradeToNumber(company.ESG_22_e), gradeToNumber(company.ESG_22_s), gradeToNumber(company.ESG_22_g), gradeToNumber(company.ESG_22)
        ],
        backgroundColor: 'rgba(251, 197, 49, 0.8)',
        borderColor: 'rgb(255, 255, 255)',
        borderWidth: 1
    }));

    // 첫 번째 차트: 막대 그래프
    new Chart(ctx1, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: chartData
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    max: 5 // 등급 스케일에 맞게 최대값 설정
                }
            },
            animation: false, // 애니메이션 제거
            responsive: true
        }
    });

    // 두 번째 차트: 레이더 차트
    new Chart(ctx2, {
        type: 'radar',
        data: {
            labels: labels.slice(0, 3).concat(labels.slice(4, 7)), // E, S, G만 포함
            datasets: chartData.map(companyData => ({
                ...companyData,
                data: companyData.data.slice(0, 3).concat(companyData.data.slice(4, 7))
            }))
        },
        options: {
            responsive: true,
            scales: {
                r: {
                    angleLines: {
                        display: false
                    },
                    suggestedMin: 0,
                    suggestedMax: 5 // 등급 스케일에 맞게 최대값 설정
                }
            },
            animation: false // 애니메이션 제거
        }
    });

    // 세 번째 차트: 파이 차트로 통합 ESG 지표 시각화
    const esg2023Data = data.map(company => gradeToNumber(company.ESG_23));
    const companyNames = data.map(company => company.companyName);

    new Chart(ctx3, {
        type: 'pie',
        data: {
            labels: companyNames,
            datasets: [{
                data: esg2023Data,
                backgroundColor: [
                    'rgba(251, 197, 49, 0.8)',
                    'rgba(54, 162, 235, 0.8)',
                    'rgba(255, 99, 132, 0.8)'
                ],
                borderColor: 'rgb(255, 255, 255)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            animation: false // 애니메이션 제거
        }
    });
}
