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

// 선택한 회사의 기업코드를 가져오는 함수
function getSelectedCompanies() {
    return Array.from(document.querySelectorAll('.button'))
        .filter(button => button.textContent.trim() !== '+')
        .map(button => button.dataset.companyCode); // 선택한 버튼의 데이터 속성에서 기업코드 가져옴
}

// 선택한 피처 알아내기
function getSelectedFeatures() {
    return Array.from(document.querySelectorAll('.feature-button.selected'))
        .map(button => button.textContent.trim());
}

let fetchedData = []; // 데이터를 저장할 변수

// 선택한 기업과 피처에 따라 데이터를 가져와서 시각화하는 함수
function fetchCompanyData() {
    const companies = getSelectedCompanies();
    const features = getSelectedFeatures();

    fetch(`/api/company/features?companyCodes=${companies.join(',')}&features=${features.join(',')}`)
        .then(response => response.json())
        .then(data => {
            fetchedData = data; // 데이터를 저장
            console.log(data); // 데이터를 사용하여 비교 결과를 표시
            visualizeData(data, features);
        })
        .catch(error => console.error('Error:', error));
}

// 데이터 시각화 어디로 할지 정하는 갈림길 같은거
function visualizeData(data, features) {
    const ctx1 = document.getElementById('activityChart').getContext('2d');

    if (features.includes('ESG')) {
        visualizeESGData(data, ctx1);
    }
    if (features.includes('활동성 지표')) {
        visualizeActivityMetrics(data, ctx1);
    }
    if (features.includes('성장성 지표')) {
        visualizeGrowthMetrics(data, ctx1);
    }
    if (features.includes('안정성 지표')) {
        visualizeStabilityMetrics(data, ctx1);
    }
    if (features.includes('수익성 지표')) {
        visualizeProfitabilityMetrics(data, ctx1); // 차트를 추가적으로 할당하거나 교체할 수 있음
    }
}

/* 💡 ESG 데이터 -> 수치로 변경 💡*/
function gradeToNumber(grade) {
    const gradeMapping = {
        "A+": 4.3, "A": 4.0, "B+": 3.3, "B": 3.0,
        "C+": 2.3, "C": 2.0, "D+": 1.3, "D": 1.0
        // 필요한 경우 더 많은 매핑 추가 가능
    };
    return gradeMapping[grade] || 0; // 매핑에 없는 경우 0으로 처리
}

// ESG 지표 시각화
function visualizeESGData(data, ctx) {
    const labels = ['환경(Environmental)', '사회(Social)', '지배구조(Governance)', 'ESG 통합'];
    const pastelColors = [
        'rgba(0, 102, 204, 0.8)',   // Blue
        'rgba(34, 139, 34, 0.8)',   // Dark Green
        'rgba(178, 34, 34, 0.8)'    // Dark Red
    ];
    const chartData = data.map((company, index) => ({
        label: company.companyName,
        data: [
            gradeToNumber(company.ESG_23_e), gradeToNumber(company.ESG_23_s), gradeToNumber(company.ESG_23_g), gradeToNumber(company.ESG_23)
        ],
        backgroundColor: pastelColors[index],
        borderColor: pastelColors[index].replace('0.8', '1'),
        borderWidth: 1
    }));

    // 첫 번째 차트: 막대 그래프
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: chartData
        },
        options: {
            scales: {
                x: {
                    ticks: {
                        color: 'black', // 축 글씨 색상
                        font: {
                            size: 14 // 축 글씨 크기
                        }
                    },
                    barPercentage: 0.5, // 막대 폭 설정
                    categoryPercentage: 0.7 // 범주 간 간격 설정
                },
                y: {
                    beginAtZero: true,
                    max: 5,
                    ticks: {
                        color: 'black', // 축 글씨 색상
                        font: {
                            size: 14 // 축 글씨 크기
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        font: {
                            size: 14 // 범례 글씨 크기
                        }
                    }
                }
            },
            animation: false
        }
    });
}

// 활동성 지표 시각화
function visualizeActivityMetrics(data, ctx) {
    const labels = ['총자산회전율', '매출채권회전율', '재고자산회전율'];
    const pastelColors = [
        'rgba(0, 102, 204, 0.8)',   // Blue
        'rgba(34, 139, 34, 0.8)',   // Dark Green
        'rgba(178, 34, 34, 0.8)'    // Dark Red
    ];

    const chartData = data.map((company, index) => ({
        label: company.companyName,
        data: Object.values(company['활동성 지표']),
        backgroundColor: pastelColors[index],
        borderColor: pastelColors[index].replace('0.8', '1'),
        borderWidth: 1
    }));

    // 차트 그리기
    window.activityChart = new Chart(ctx, {
        type: 'bar', // Radar Chart 사용
        data: {
            labels: labels,
            datasets: chartData
        },
        options: {
            scales: {
                x: {
                    ticks: {
                        color: 'black',
                        font: {
                            size: 14
                        }
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        color: 'black',
                        font: {
                            size: 14
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        font: {
                            size: 14
                        }
                    }
                }
            },
            animation: false
        }
    });
}

// 성장성 지표 시각화
function visualizeGrowthMetrics(data, ctx) {
    const labels = ['매출액증가율(YoY)', '영업이익증가율(YoY)', '순이익증가율(YoY)', '총포괄이익증가율(YoY)'];
    const pastelColors = [
        'rgba(0, 102, 204, 0.8)',   // Blue
        'rgba(34, 139, 34, 0.8)',   // Dark Green
        'rgba(178, 34, 34, 0.8)'    // Dark Red
    ];

    const chartData = data.map((company, index) => ({
        label: company.companyName,
        data: Object.values(company['성장성 지표']),
        backgroundColor: pastelColors[index],
        borderColor: pastelColors[index].replace('0.8', '1'),
        borderWidth: 1
    }));

    new Chart(ctx, {
        type: 'bar', // 성장성 지표에 적합한 차트 타입
        data: {
            labels: labels,
            datasets: chartData
        },
        options: {
            scales: {
                x: {
                    ticks: {
                        color: 'black',
                        font: {
                            size: 14
                        }
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        color: 'black',
                        font: {
                            size: 14
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        font: {
                            size: 14
                        }
                    }
                }
            },
            animation: false
        }
    });
}

// 수익성 지표 시각화
function visualizeProfitabilityMetrics(data, ctx) {
    const labels = ['순이익률', '매출총이익률', '자기자본영업이익률'];
    const pastelColors = [
        'rgba(0, 102, 204, 0.8)',   // Blue
        'rgba(34, 139, 34, 0.8)',   // Dark Green
        'rgba(178, 34, 34, 0.8)'    // Dark Red
    ];
    const chartData = data.map((company, index) => ({
        label: company.companyName,
        data: Object.values(company['수익성 지표']),
        backgroundColor: pastelColors[index],
        borderColor: pastelColors[index].replace('0.8', '1'),
        borderWidth: 1
    }));

    new Chart(ctx, {
        type: 'bar', // 수익성 지표에 적합한 차트 타입
        data: {
            labels: labels,
            datasets: chartData
        },
        options: {
            scales: {
                x: {
                    ticks: {
                        color: 'black',
                        font: {
                            size: 14
                        }
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        color: 'black',
                        font: {
                            size: 14
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        font: {
                            size: 14
                        }
                    }
                }
            },
            animation: false
        }
    });
}


// 안정성 지표 시각화
function visualizeStabilityMetrics(data, ctx) {
    const labels = ['자기자본비율', '부채비율', '유동비율'];
    const pastelColors = [
        'rgba(0, 102, 204, 0.8)',   // Blue
        'rgba(34, 139, 34, 0.8)',   // Dark Green
        'rgba(178, 34, 34, 0.8)'    // Dark Red
    ];
    const chartData = data.map((company, index) => ({
        label: company.companyName,
        data: Object.values(company['안정성 지표']),
        backgroundColor: pastelColors[index],
        borderColor: pastelColors[index].replace('0.8', '1'),
        borderWidth: 1
    }));

    new Chart(ctx, {
        type: 'bar', // 안정성 지표에 적합한 차트 타입
        data: {
            labels: labels,
            datasets: chartData
        },
        options: {
            plugins: {
                legend: {
                    labels: {
                        font: {
                            size: 14
                        }
                    }
                }
            },
            animation: false
        }
    });
}

// 회사 선택 시 버튼에 이름 표시
// 처음으로 + 표시가 있는 버튼을 찾아 그 버튼의 텍스트를 선택한 회사 이름으로 변경
function selectCompany(companyName, companyCode) {
    const buttons = document.querySelectorAll('.button');
    const emptyButton = Array.from(buttons).find(button => button.textContent.trim() === '+');
    if (emptyButton) {
        emptyButton.textContent = companyName; // 기업명만 버튼에 표시
        emptyButton.dataset.companyCode = companyCode; // 회사 코드는 데이터 속성에 저장
        closeModal();
    }
}

// 랜덤한 색상을 생성하는 함수
function getRandomColor() {
    const colors = [
        'rgba(0, 102, 204, 0.8)',   // Blue
        'rgba(34, 139, 34, 0.8)',   // Dark Green
        'rgba(178, 34, 34, 0.8)'    // Dark Red
    ];
    return colors[Math.floor(Math.random() * colors.length)];
}