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
    if (features.includes('ESG')) {
        visualizeESGData(data);
    }
    if (features.includes('활동성 지표')) {
        visualizeActivityMetrics(data);
    }
    if (features.includes('성장성 지표')) {
        visualizeGrowthMetrics(data);
    }
    if (features.includes('안정성 지표')) {
        visualizeStabilityMetrics(data);
    }
    if (features.includes('수익성 지표')) {
        visualizeProfitabilityMetrics(data); // 차트를 추가적으로 할당하거나 교체할 수 있음
    }
    if (features.includes('3개년 현금 흐름 분석')) {
        visualizeCustomCashFlowComparison(data); // 차트를 추가적으로 할당하거나 교체할 수 있음
    }
    // if (features.includes('재무 비율 및 건전성 분석')) {
    //     visualize(data); // 차트를 추가적으로 할당하거나 교체할 수 있음
    // }
}

function visualizeCustomCashFlowComparison(data) {
    const labels = ["3년 전", "2년 전", "1년 전"];

    // 재무활동현금흐름, 영업활동현금흐름, 투자활동현금흐름을 각각 다른 그래프에 그리기 위한 데이터셋 배열
    const operatingCashFlowDatasets = [];
    const investingCashFlowDatasets = [];
    const financingCashFlowDatasets = [];

    // 색상 설정
    const colors = [
        "rgba(52, 152, 219, 0.9)",  // 파란색
        "rgba(231, 76, 60, 0.9)",   // 빨간색
        "rgba(46, 204, 113, 0.9)"   // 녹색
    ];

    // 각 기업의 현금 흐름 데이터를 처리
    data.forEach((company, index) => {
        const color = colors[index % colors.length];
        const cashFlowData = company.cashFlow;

        // 영업활동현금흐름 추세선
        operatingCashFlowDatasets.push({
            label: `${company.companyName} - 영업활동현금흐름`,
            data: [
                cashFlowData.operatingCashFlow.prePrevious,
                cashFlowData.operatingCashFlow.previous,
                cashFlowData.operatingCashFlow.current
            ],
            borderColor: color,
            backgroundColor: color.replace("0.9", "0.2"),
            fill: false,
            tension: 0.4,
            pointStyle: 'circle',
            pointRadius: 5,
            pointHoverRadius: 7,
        });

        // 투자활동현금흐름 추세선
        investingCashFlowDatasets.push({
            label: `${company.companyName} - 투자활동현금흐름`,
            data: [
                cashFlowData.investingCashFlow.prePrevious,
                cashFlowData.investingCashFlow.previous,
                cashFlowData.investingCashFlow.current
            ],
            borderColor: color,
            backgroundColor: color.replace("0.9", "0.2"),
            fill: false,
            tension: 0.4,
            pointStyle: 'triangle',
            pointRadius: 5,
            pointHoverRadius: 7,
        });

        // 재무활동현금흐름 추세선
        financingCashFlowDatasets.push({
            label: `${company.companyName} - 재무활동현금흐름`,
            data: [
                cashFlowData.financingCashFlow.prePrevious,
                cashFlowData.financingCashFlow.previous,
                cashFlowData.financingCashFlow.current
            ],
            borderColor: color,
            backgroundColor: color.replace("0.9", "0.2"),
            fill: false,
            tension: 0.4,
            pointStyle: 'rect',
            pointRadius: 5,
            pointHoverRadius: 7,
        });
    });

    // 차트 구성
    const createChartConfig = (datasets, title) => ({
        type: "line",
        data: {
            labels: labels,
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: title,
                    font: {
                        size: 24,
                        weight: "bold",
                        family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                    },
                    color: "#000000"  // 검은색 텍스트 색상
                },
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        font: {
                            size: 14,
                            weight: "bold",
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        },
                        color: "#000000"  // 검은색 텍스트 색상
                    }
                },
                tooltip: {
                    backgroundColor: "#f5f5f5",
                    titleFont: {
                        size: 16,
                        weight: "bold",
                        family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    bodyFont: {
                        size: 14,
                        family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    callbacks: {
                        label: function (context) {
                            return `${context.dataset.label}: ${context.raw.toLocaleString()} 원`;
                        }
                    }
                }
            },
            scales: {
                x: {
                    display: true,
                    title: {
                        display: true,
                        text: '년도',
                        color: '#000000',
                        font: {
                            size: 16,
                            weight: 'bold',
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        }
                    },
                    ticks: {
                        font: {
                            size: 14,
                            weight: "bold",
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        },
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    grid: {
                        color: "rgba(0, 0, 0, 0.1)"  // 연한 검은색 그리드 선
                    }
                },
                y: {
                    display: true,
                    title: {
                        display: true,
                        text: '금액 (원)',
                        color: '#000000',
                        font: {
                            size: 16,
                            weight: 'bold',
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        }
                    },
                    beginAtZero: true,
                    ticks: {
                        font: {
                            size: 14,
                            weight: "bold",
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        },
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    grid: {
                        color: "rgba(0, 0, 0, 0.1)"  // 연한 검은색 그리드 선
                    }
                }
            },
            layout: {
                padding: {
                    left: 10,
                    right: 10,
                    top: 20,
                    bottom: 10
                }
            },
            elements: {
                line: {
                    borderWidth: 3
                }
            }
        }
    });

    // 각 현금흐름 차트를 그리드에 추가
    addChartToGrid("operatingCashFlowChart", createChartConfig(operatingCashFlowDatasets, "영업활동현금흐름 비교 분석"), "영업활동현금흐름 비교 분석");
    addChartToGrid("investingCashFlowChart", createChartConfig(investingCashFlowDatasets, "투자활동현금흐름 비교 분석"), "투자활동현금흐름 비교 분석");
    addChartToGrid("financingCashFlowChart", createChartConfig(financingCashFlowDatasets, "재무활동현금흐름 비교 분석"), "재무활동현금흐름 비교 분석");
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
function visualizeCustomCashFlowComparison(data) {
    const labels = ["3년 전", "2년 전", "1년 전"];

    // 재무활동현금흐름, 영업활동현금흐름, 투자활동현금흐름을 각각 다른 그래프에 그리기 위한 데이터셋 배열
    const operatingCashFlowDatasets = [];
    const investingCashFlowDatasets = [];
    const financingCashFlowDatasets = [];

    // 색상 설정
    const colors = [
        "rgba(52, 152, 219, 0.9)",  // 파란색
        "rgba(231, 76, 60, 0.9)",   // 빨간색
        "rgba(46, 204, 113, 0.9)"   // 녹색
    ];

    // 각 기업의 현금 흐름 데이터를 처리
    data.forEach((company, index) => {
        const color = colors[index % colors.length];
        const cashFlowData = company.cashFlow;

        // 영업활동현금흐름 추세선
        operatingCashFlowDatasets.push({
            label: `${company.companyName} - 영업활동현금흐름`,
            data: [
                cashFlowData.operatingCashFlow.prePrevious,
                cashFlowData.operatingCashFlow.previous,
                cashFlowData.operatingCashFlow.current
            ],
            borderColor: color,
            backgroundColor: color.replace("0.9", "0.2"),
            fill: false,
            tension: 0.4,
            pointStyle: 'circle',
            pointRadius: 5,
            pointHoverRadius: 7,
        });

        // 투자활동현금흐름 추세선
        investingCashFlowDatasets.push({
            label: `${company.companyName} - 투자활동현금흐름`,
            data: [
                cashFlowData.investingCashFlow.prePrevious,
                cashFlowData.investingCashFlow.previous,
                cashFlowData.investingCashFlow.current
            ],
            borderColor: color,
            backgroundColor: color.replace("0.9", "0.2"),
            fill: false,
            tension: 0.4,
            pointStyle: 'triangle',
            pointRadius: 5,
            pointHoverRadius: 7,
        });

        // 재무활동현금흐름 추세선
        financingCashFlowDatasets.push({
            label: `${company.companyName} - 재무활동현금흐름`,
            data: [
                cashFlowData.financingCashFlow.prePrevious,
                cashFlowData.financingCashFlow.previous,
                cashFlowData.financingCashFlow.current
            ],
            borderColor: color,
            backgroundColor: color.replace("0.9", "0.2"),
            fill: false,
            tension: 0.4,
            pointStyle: 'rect',
            pointRadius: 5,
            pointHoverRadius: 7,
        });
    });

    // 차트 구성
    const createChartConfig = (datasets, title) => ({
        type: "line",
        data: {
            labels: labels,
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: title,
                    font: {
                        size: 24,
                        weight: "bold",
                        family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                    },
                    color: "#000000"  // 검은색 텍스트 색상
                },
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        font: {
                            size: 14,
                            weight: "bold",
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        },
                        color: "#000000"  // 검은색 텍스트 색상
                    }
                },
                tooltip: {
                    backgroundColor: "#f5f5f5",
                    titleFont: {
                        size: 16,
                        weight: "bold",
                        family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    bodyFont: {
                        size: 14,
                        family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    callbacks: {
                        label: function (context) {
                            return `${context.dataset.label}: ${context.raw.toLocaleString()} 원`;
                        }
                    }
                }
            },
            scales: {
                x: {
                    display: true,
                    title: {
                        display: true,
                        text: '년도',
                        color: '#000000',
                        font: {
                            size: 16,
                            weight: 'bold',
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        }
                    },
                    ticks: {
                        font: {
                            size: 14,
                            weight: "bold",
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        },
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    grid: {
                        color: "rgba(0, 0, 0, 0.1)"  // 연한 검은색 그리드 선
                    }
                },
                y: {
                    display: true,
                    title: {
                        display: true,
                        text: '금액 (원)',
                        color: '#000000',
                        font: {
                            size: 16,
                            weight: 'bold',
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        }
                    },
                    beginAtZero: false,
                    ticks: {
                        font: {
                            size: 14,
                            weight: "bold",
                            family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                        },
                        color: "#000000"  // 검은색 텍스트 색상
                    },
                    grid: {
                        color: "rgba(0, 0, 0, 0.1)"  // 연한 검은색 그리드 선
                    }
                }
            },
            layout: {
                padding: {
                    left: 10,
                    right: 10,
                    top: 20,
                    bottom: 10
                }
            },
            elements: {
                line: {
                    borderWidth: 3
                }
            }
        }
    });

    // 각 현금흐름 차트를 그리드에 추가
    addChartToGrid("operatingCashFlowChart", createChartConfig(operatingCashFlowDatasets, "영업활동현금흐름 비교 분석"), "영업활동현금흐름 비교 분석");
    addChartToGrid("investingCashFlowChart", createChartConfig(investingCashFlowDatasets, "투자활동현금흐름 비교 분석"), "투자활동현금흐름 비교 분석");
    addChartToGrid("financingCashFlowChart", createChartConfig(financingCashFlowDatasets, "재무활동현금흐름 비교 분석"), "재무활동현금흐름 비교 분석");
}

function addChartToGrid(chartId, chartConfig, title) {
    const gridPositions = ['grid-item-1', 'grid-item-2', 'grid-item-3'];

    for (let position of gridPositions) {
        const gridItem = document.getElementById(position);
        if (!gridItem.innerHTML.trim()) { // 해당 위치가 비어있는지 확인
            gridItem.innerHTML = `<h3>${title}</h3><canvas id="${chartId}"></canvas>`;
            const ctx = document.getElementById(chartId).getContext('2d');
            new Chart(ctx, chartConfig);
            break;
        }
    }
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
    const chartConfig = {
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
                            size: 14,
                            color: 'black' // 범례 텍스트를 검정색으로 설정
                        }
                    }
                }
            },
            animation: false
        }
    };
    addChartToGrid('activityChart', chartConfig, '활동성 지표 분석');
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

    const chartConfig = {
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
                            size: 14,
                            color: 'black' // 범례 텍스트를 검정색으로 설정
                        }
                    }
                }
            },
            animation: false
        }
    };
    addChartToGrid('growthChart', chartConfig, '성장성 지표 분석');
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

    const chartConfig = {
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
                            size: 14,
                            color: 'black' // 범례 텍스트를 검정색으로 설정
                        }
                    }
                }
            },
            animation: false
        }
    };
    addChartToGrid('profitabilityChart', chartConfig, '수익성 지표 분석');
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




    const chartConfig = {
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
                            size: 14,
                            color: 'black' // 범례 텍스트를 검정색으로 설정
                        }
                    }
                }
            },
            animation: false
        }
    };
    addChartToGrid('stabilityChart', chartConfig, '안정성 지표 분석');
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

function addChartToGrid(chartId, chartConfig, title) {
    const gridPositions = ['grid-item-1', 'grid-item-2', 'grid-item-3', 'grid-item-4', 'grid-item-5', 'grid-item-6'];

    for (let position of gridPositions) {
        const gridItem = document.getElementById(position);
        if (!gridItem.innerHTML.trim()) { // 해당 위치가 비어있는지 확인
            gridItem.innerHTML = `<h3>${title}</h3><canvas id="${chartId}"></canvas>`;
            const ctx = document.getElementById(chartId).getContext('2d');
            new Chart(ctx, chartConfig);
            break;
        }
    }
}
