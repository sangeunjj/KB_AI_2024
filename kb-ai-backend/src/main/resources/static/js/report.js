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
    const unicode = firstChar.charCodeAt(0);

    if (unicode >= 0xAC00 && unicode <= 0xD7A3) { // Hangul Syllables
        const initialConsonants = ["ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"];
        const consonantIndex = Math.floor((unicode - 0xAC00) / 588);
        return initialConsonants[consonantIndex];
    } else if ((unicode >= 65 && unicode <= 90) || (unicode >= 97 && unicode <= 122)) {
        return firstChar.toUpperCase(); // English letters
    } else {
        return '#'; // For other characters
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
// Fetch data and show results and report sections
function fetchCompanyData() {
    const companies = getSelectedCompanies();
    const features = getSelectedFeatures();

    // Show the results and report sections
    document.getElementById('resultsContainer').style.display = 'block';
    document.getElementById('reportContainer').style.display = 'block';

    // Show the loading message
    document.getElementById('report-output').innerHTML = "<h3>비교/분석 자동 보고서</h3><p>응답 중입니다..... 잠시만 기다려주세요.</p>";

    fetch(`/api/company/features?companyCodes=${companies.join(',')}&features=${features.join(',')}`)
        .then(response => response.json())
        .then(data => {
            fetchedData = data;
            // console.log(data);
            visualizeData(data, features);

            fetch(`/bot/generate-report?companyCodes=${companies.join(',')}&features=${features.join(',')}`)
                .then(response => response.text())
                .then(report => {
                    // console.log(report);
                    // Replace loading message with the actual report
                    const reportContainer = document.getElementById("report-output");
                    reportContainer.innerHTML = marked.parse(report);
                })
                .catch(error => {
                    console.error('Error:', error);
                    document.getElementById("report-output").innerHTML = "<p>오류가 발생했습니다. 다시 시도해 주세요.</p>";
                });
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById("report-output").innerHTML = "<h3>비교/분석 자동 보고서</h3><p>오류가 발생했습니다. 다시 시도해 주세요.</p>";
        });
}

// 데이터 시각화 어디로 할지 정하는 갈림길 같은거
function visualizeData(data, features) {
    if (features.includes('3개년 현금 흐름 분석')) {
        visualizeCustomCashFlowComparison(data); // 차트를 추가적으로 할당하거나 교체할 수 있음
    }
    if (features.includes('재무 건전성 및 수익성 지표 분석')) {
        visualizeFinancialHealthAndProfitability(data); // 차트를 추가적으로 할당하거나 교체할 수 있음
    }
    if (features.includes('재무 건전성 및 유동성 분석')) {
        visualizeFinancialHealthAndLiquidity(data); // 차트를 추가적으로 할당하거나 교체할 수 있음
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
    if (features.includes('ESG')) {
        visualizeESGData(data);
    }
}
function calculateRatios(company) {
    // 금융비용대매출액비율 = 매출액 / 금융비용
    const financialCostToSalesRatio = {
        current: (company.salesCurrent / company.financialCostCurrent) * 100,
        previous: (company.salesPrevious / company.financialCostPrevious) * 100,
        prePrevious: (company.salesPrePrevious / company.financialCostPrePrevious) * 100
    };

    // 미수금 대비 현금성 자산 충분성 분석 = 현금및현금성자산 / 미수금
    const receivablesToCashRatio = {
        current: (company.cashAndCashEquivalentsCurrent / company.accountsReceivableCurrent) * 100,
        previous: (company.cashAndCashEquivalentsPrevious / company.accountsReceivablePrevious) * 100,
        prePrevious: (company.cashAndCashEquivalentsPrePrevious / company.accountsReceivablePrePrevious) * 100
    };

    return { financialCostToSalesRatio, receivablesToCashRatio };
}

function visualizeFinancialHealthAndProfitability(data) {
    const labels = ["3년 전", "2년 전", "1년 전"];
    const colors = [
        "rgba(52, 152, 219, 1)",  // 파란색
        "rgba(231, 76, 60, 1)",   // 빨간색
        "rgba(46, 204, 113, 1)"   // 녹색
    ];

    data.forEach((company, index) => {
        const color = colors[index % colors.length];

        // 3개년 영업이익률 변화율 그래프
        const operatingProfitMarginConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 3개년 영업이익률 변화율`,
                    data: [
                        company.operatingProfitMargin.prePrevious,
                        company.operatingProfitMargin.previous,
                        company.operatingProfitMargin.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 3개년 영업이익률 변화율`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} %`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '비율 (%)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`operatingProfitMarginChart_${company.companyName}`, operatingProfitMarginConfig, `${company.companyName} - 3개년 영업이익률 변화율`);

        // 3개년 부채비율 변화율 그래프
        const debtRatioConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 3개년 부채비율 변화율`,
                    data: [
                        company.debtRatio.prePrevious,
                        company.debtRatio.previous,
                        company.debtRatio.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 3개년 부채비율 변화율`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} %`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '비율 (%)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`debtRatioChart_${company.companyName}`, debtRatioConfig, `${company.companyName} - 3개년 부채비율 변화율`);

        // 3개년 유동비율 변화율 그래프
        const liquidityRatioConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 3개년 유동비율 변화율`,
                    data: [
                        company.liquidityRatio.prePrevious,
                        company.liquidityRatio.previous,
                        company.liquidityRatio.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 3개년 유동비율 변화율`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} %`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '비율 (%)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`liquidityRatioChart_${company.companyName}`, liquidityRatioConfig, `${company.companyName} - 3개년 유동비율 변화율`);
    });
}
function visualizeFinancialHealthAndLiquidity(data) {
    const labels = ["3년 전", "2년 전", "1년 전"];
    const colors = [
        "rgba(52, 152, 219, 1)",  // 파란색
        "rgba(231, 76, 60, 1)",   // 빨간색
        "rgba(46, 204, 113, 1)"   // 녹색
    ];

    data.forEach((company, index) => {
        const { financialCostToSalesRatio, receivablesToCashRatio } = calculateRatios(company);
        const color = colors[index % colors.length];

        // 금융비용대매출액비율 변화율 그래프
        const financialCostToSalesConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 금융비용대매출액비율 변화율`,
                    data: [
                        financialCostToSalesRatio.prePrevious,
                        financialCostToSalesRatio.previous,
                        financialCostToSalesRatio.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 금융비용대매출액비율 변화율`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} %`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '비율 (%)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`financialCostToSalesChart_${company.companyName}`, financialCostToSalesConfig, `${company.companyName} - 금융비용대매출액비율 변화율(매출액/금융비용)`);

        // 미수금 대비 현금성 자산 충분성 분석 그래프
        const receivablesToCashRatioConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 미수금 대비 현금성 자산 충분성 분석`,
                    data: [
                        receivablesToCashRatio.prePrevious,
                        receivablesToCashRatio.previous,
                        receivablesToCashRatio.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 미수금 대비 현금성 자산 충분성 분석`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} %`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '비율 (%)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`receivablesToCashRatioChart_${company.companyName}`, receivablesToCashRatioConfig, `${company.companyName} - 미수금 대비 현금성 자산 충분성 분석 (현금및현금성자산 / 미수금)`);
    });
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
    const chartConfig = {
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
                            size: 14, // 범례 글씨 크기
                            color: 'black' // 범례 텍스트를 검정색으로 설정
                        }
                    }
                }
            },
            animation: false
        }
    };
    addChartToGrid('esgChart', chartConfig, '환경, 사회, 지배구조(ESG) 통합 분석');
}



// ESG 지표 시각화
function visualizeCustomCashFlowComparison(data) {
    const labels = ["3년 전", "2년 전", "1년 전"];

    // 색상 설정
    const colors = [
        "rgba(52, 152, 219, 1)",  // 파란색
        "rgba(231, 76, 60, 1)",   // 빨간색
        "rgba(46, 204, 113, 1)"   // 녹색
    ];

    // 각 회사에 대해 세 개의 그래프를 생성
    data.forEach((company, index) => {
        const color = colors[index % colors.length];
        const cashFlowData = company.cashFlow;

        // 영업활동현금흐름 그래프
        const operatingCashFlowConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 영업활동현금흐름`,
                    data: [
                        cashFlowData.operatingCashFlow.prePrevious,
                        cashFlowData.operatingCashFlow.previous,
                        cashFlowData.operatingCashFlow.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 영업활동현금흐름`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} 원`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '금액 (원)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`operatingCashFlowChart_${company.companyName}`, operatingCashFlowConfig, `${company.companyName} - 영업활동현금흐름`);

        // 투자활동현금흐름 그래프
        const investingCashFlowConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 투자활동현금흐름`,
                    data: [
                        cashFlowData.investingCashFlow.prePrevious,
                        cashFlowData.investingCashFlow.previous,
                        cashFlowData.investingCashFlow.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 투자활동현금흐름`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} 원`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '금액 (원)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`investingCashFlowChart_${company.companyName}`, investingCashFlowConfig, `${company.companyName} - 투자활동현금흐름`);

        // 재무활동현금흐름 그래프
        const financingCashFlowConfig = {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: `${company.companyName} - 재무활동현금흐름`,
                    data: [
                        cashFlowData.financingCashFlow.prePrevious,
                        cashFlowData.financingCashFlow.previous,
                        cashFlowData.financingCashFlow.current
                    ],
                    borderColor: color,
                    backgroundColor: color,
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        // text: `${company.companyName} - 재무활동현금흐름`,
                        font: {
                            size: 18
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `${context.dataset.label}: ${context.raw.toLocaleString()} 원`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '년도'
                        }
                    },
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: '금액 (원)'
                        }
                    }
                }
            }
        };
        addChartToGrid(`financingCashFlowChart_${company.companyName}`, financingCashFlowConfig, `${company.companyName} - 재무활동현금흐름`);
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
    const gridPositions = [
        'grid-item-1', 'grid-item-2', 'grid-item-3', 'grid-item-4', 'grid-item-5', 'grid-item-6',
        'grid-item-7', 'grid-item-8', 'grid-item-9', 'grid-item-10', 'grid-item-11', 'grid-item-12',
        'grid-item-13', 'grid-item-14', 'grid-item-15', 'grid-item-16', 'grid-item-17', 'grid-item-18',
        'grid-item-19', 'grid-item-20', 'grid-item-21', 'grid-item-22', 'grid-item-23', 'grid-item-24',
        'grid-item-25', 'grid-item-26', 'grid-item-27', 'grid-item-28', 'grid-item-29', 'grid-item-30'
    ];

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
