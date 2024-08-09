const companies = [
    // 가짜 데이터 리스트
];

// 기존의 가짜 데이터 리스트를 제거하고 API 호출로 대체
document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/company')
        .then(response => response.json())
        .then(companies => {
            displayCompanies(companies);
        })
        .catch(error => console.error('Error fetching company list:', error));

    document.querySelector('.search-clear').addEventListener('click', function () {
        document.getElementById('search-input').value = '';
        refreshCompanies();
    });

    document.getElementById('search-input').addEventListener('input', searchCompany);
    document.getElementById('esg-slider').addEventListener('input', filterCompanies);
    document.getElementById('region-select').addEventListener('change', filterByRegion);
});


function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    const content = document.getElementById("content");
    sidebar.classList.toggle("active");
    content.classList.toggle("expanded");
}

document.querySelector('.search-clear').addEventListener('click', function () {
    document.getElementById('search-input').value = '';
    displayCompanies(companies);
});

function getESGValue() {
    const esgSlider = document.getElementById('esg-slider').value;
    const esgValues = ['D', 'C', 'B', 'B+', 'A', 'A+', 'S'];
    return esgValues[esgSlider];
}

function filterCompanies() {
    const esgValue = getESGValue();
    document.getElementById('esg-value').textContent = esgValue;
    const filteredCompanies = companies.filter(company => {
        const esgOrder = ['D', 'C', 'B', 'B+', 'A', 'A+', 'S'];
        return esgOrder.indexOf(company.esg) >= esgOrder.indexOf(esgValue);
    });
    displayCompanies(filteredCompanies);
}

function searchCompany() {
    const searchInput = document.getElementById('search-input').value.toLowerCase();
    const filteredCompanies = companies.filter(company => company.name.toLowerCase().includes(searchInput));
    displayCompanies(filteredCompanies);
}

function clearSearch() {
    document.getElementById('search-input').value = '';
    displayCompanies(companies);
}

function refreshCompanies() {
    clearSearch();
    displayCompanies(companies);
}

function toggleBookmark(event) {
    event.stopPropagation();
    const bookmarkIcon = event.target;
    if (bookmarkIcon.src.includes('bookmark2.png')) {
        bookmarkIcon.src = '/icons/bookmark_blank1.png';
    } else {
        bookmarkIcon.src = '/icons/bookmark2.png';
    }
}


document.getElementById('search-input').addEventListener('input', searchCompany);
document.getElementById('esg-slider').addEventListener('input', filterCompanies);

// 초기 기업 리스트 표시
displayCompanies(companies);

function filterByRegion() {
    const selectedRegion = document.getElementById('region-select').value;
    const filteredCompanies = selectedRegion ? companies.filter(company => company.address === selectedRegion) : companies;
    displayCompanies(filteredCompanies);
}

function displayCompanies(companyList) {
    const companyListDiv = document.getElementById('company-list');
    companyListDiv.innerHTML = '';

    // 컬럼명 추가
    const columnDiv = document.createElement('div');
    columnDiv.className = 'company-item columns';
    columnDiv.innerHTML = `
        <span class="bookmark"></span>
        <span class="company-name">기업명</span> <!-- 기업명 -->
        <span class="company-address">지역</span> <!-- 지역 (주소) -->
        <span class="company-establishment">설립일자</span> <!-- 설립일자 -->
        <span class="company-ceo">대표자명</span> <!-- 대표자명 -->
        <span class="company-esg">ESG 지표</span> <!-- ESG 지표 -->
        <span class="company-female-executives">여성임원수</span> <!-- 여성임원수 -->
        <span class="company-sentiment">산업현황(긍/부정)</span> <!-- 산업현황 (긍정/부정 점수) -->
    `;
    companyListDiv.appendChild(columnDiv);

    companyList.forEach(company => {
        const firstWordOfAddress = getFirstWordOfAddress(company.adres);
        const companyDiv = document.createElement('div');
        companyDiv.className = 'company-item';
        companyDiv.innerHTML = `
            <span class="bookmark" onclick="toggleBookmark(event)"><img src="/icons/bookmark2.png" alt="Bookmark Icon" width="20px" height="20px"></span>
            <span class="company-name">${company.corp_name}</span> <!-- 기업명 -->
            <span class="company-address">${firstWordOfAddress}</span> <!-- 지역 (주소) -->
            <span class="company-establishment">${company.est_dt}</span> <!-- 설립일자 -->
            <span class="company-ceo">${company.ceo_nm}</span> <!-- 대표자명 -->
            <span class="company-esg">${company.esg}</span> <!-- ESG 지표 -->
            <span class="company-female-executives">${company.femaleExecutives}</span> <!-- 여성임원수 -->
            <span class="company-sentiment">${company.sentimentScore}</span> <!-- 산업현황 (긍정/부정 점수) -->
        `;
        companyDiv.addEventListener('click', () => displayCompanyDetails(company));
        companyListDiv.appendChild(companyDiv);
    });
}

// 초기 기업 리스트 표시
displayCompanies(companies);

document.getElementById('search-input').addEventListener('input', searchCompany);
document.getElementById('esg-slider').addEventListener('input', filterCompanies);
document.getElementById('region-select').addEventListener('change', filterByRegion);

// 기존 코드 생략
function displayCompanyDetails(company) {
    fetch(`/api/company/${company.companyCode}`)
        .then(response => response.json())
        .then(data => {
            const companyDetails = document.getElementById('company-details');

            // `company` 객체에서 데이터 가져오기
            const companyData = data.company;
            // `dartResponse` 객체에서 데이터 가져오기
            const dartData = data.dartResponse;

            // 데이터를 각 HTML 요소에 바인딩
            document.getElementById('company-name').textContent = dartData.corp_name || companyData.companyName;
            document.getElementById('company-ceo').textContent = dartData.ceo_nm;
            document.getElementById('company-stockCode').textContent = dartData.stock_code || "N/A";
            document.getElementById('company-industryCode').textContent = dartData.induty_code;
            document.getElementById('company-estDt').textContent = dartData.est_dt;
            document.getElementById('company-adres').textContent = dartData.adres;
            document.getElementById('company-phnNo').textContent = dartData.phn_no;
            document.getElementById('company-faxNo').textContent = dartData.fax_no;
            document.getElementById('company-hmUrl').querySelector('a').textContent = dartData.hm_url || "N/A";
            document.getElementById('company-hmUrl').querySelector('a').href = dartData.hm_url || "#";
            document.getElementById('company-irUrl').querySelector('a').textContent = dartData.ir_url || "N/A";
            document.getElementById('company-irUrl').querySelector('a').href = dartData.ir_url || "#";
            document.getElementById('company-jurirNo').textContent = dartData.jurir_no;
            document.getElementById('company-bizrNo').textContent = dartData.bizr_no;

            // 상세 정보 내용 업데이트
            companyDetails.classList.add('active');
            companyDetails.classList.remove('closed', 'collapsed');

            // URL 해시를 '기업개요'로 설정 -> 상세페이지 창을 열었을 때 기본적으로 기업개요가 나타나도록 함
            location.hash = '기업개요';

            // '기업개요' 탭을 표시
            showTabContent('기업개요');

            // 지도를 초기화하고 표시
            initMap();

            // '기업개요' 탭을 활성화 상태로 설정
            updateActiveTab('기업개요');
        })
        .catch(error => console.error('Error fetching company details:', error));
}

function updateActiveTab(tabId) {
    // 모든 탭에서 active 클래스를 제거
    document.querySelectorAll('.com-details-index a').forEach(link => {
        link.classList.remove('active');
    });

    // 선택된 탭에 active 클래스 추가
    document.querySelector(`.com-details-index a[href="#${tabId}"]`).classList.add('active');
}

// function closeCompanyDetails() {
//     const companyDetails = document.getElementById('company-details');
//     companyDetails.classList.remove('active');
//     companyDetails.classList.add('closed');
// }

function openCompanyDetails() {
    const companyDetails = document.getElementById('company-details');
    companyDetails.classList.add('active');
    companyDetails.classList.remove('closed', 'collapsed');
}

function toggleCompanyDetails() {
    const companyDetails = document.getElementById('company-details');
    if (companyDetails.classList.contains('collapsed')) {
        openCompanyDetails();
    } else {
        collapseCompanyDetails();
    }
}

function collapseCompanyDetails() {
    const companyDetails = document.getElementById('company-details');
    companyDetails.classList.remove('active', 'closed');
    companyDetails.classList.add('collapsed');

    // 해시태그를 초기화하고, URL을 http://localhost:8080/company로 설정
    window.history.pushState('', document.title, window.location.pathname);
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('.search-clear').addEventListener('click', function () {
        document.getElementById('search-input').value = '';
        displayCompanies(companies);
    });

    document.getElementById('search-input').addEventListener('input', searchCompany);
    document.getElementById('esg-slider').addEventListener('input', filterCompanies);
    document.getElementById('region-select').addEventListener('change', filterByRegion);

    // 초기 기업 리스트 표시
    displayCompanies(companies);
});

// 선택한 메뉴만 노란색 글씨로 변경
document.querySelectorAll('.com-details-index a').forEach(link => {
    link.addEventListener('click', function (event) {
        document.querySelectorAll('.com-details-index a').forEach(el => el.classList.remove('active'));
        this.classList.add('active');
    });
});

// 선택한 메뉴의 내용만 표시
function showTabContent(tabId) {
    // 모든 탭 내용을 숨김
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.add('hidden');
    });
    // 클릭된 탭 내용을 표시
    document.getElementById(tabId).classList.remove('hidden');
}

// 지도를 초기화하고 표시하는 함수
function initMap() {
    var mapContainer = document.getElementById('map');

    // 만약 이미 지도가 초기화되어 있다면 다시 로드하지 않도록 처리
    if (mapContainer.innerHTML.trim() !== "") {
        return;
    }

    var mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 기본 중심좌표 설정
        level: 3 // 지도의 확대 레벨
    };

    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도 생성
}

// 주소에서 앞 단어만 추출하는 함수를 추가
function getFirstWordOfAddress(address) {
    if (address) {
        return address.split(' ')[0];
    }
    return '';
}
