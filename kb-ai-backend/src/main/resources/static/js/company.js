const companies = [
    {
        name: '에이젠글로벌',
        esg: 'A',
        address: '서울특별시',
        establishment: '2015-08-01',
        businessArea: 'IT',
        industryCode: '12345',
        status: true
    },
    {
        name: '앤톡',
        esg: 'B+',
        address: '부산광역시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: false
    },
    {
        name: 'AJ네트웍스',
        esg: 'A',
        address: '대구광역시',
        establishment: '2010-01-24',
        businessArea: 'IT',
        industryCode: '095570',
        status: true
    },
    {
        name: 'BGF',
        esg: 'B+',
        address: '인천광역시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: false
    },
    {
        name: 'BGF리테일',
        esg: 'B',
        address: '광주광역시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: true
    },
    {
        name: 'BNK금융지주',
        esg: 'C',
        address: '대전광역시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: false
    },
    {
        name: 'AK홀딩스',
        esg: 'C',
        address: '울산광역시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: true
    },
    {
        name: 'CJ CGV',
        esg: 'B+',
        address: '세종특별자치시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: false
    },
    {
        name: 'BNK캐피탈',
        esg: 'D',
        address: '경기도',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: true
    },
    {
        name: 'DN오토모티브',
        esg: 'D',
        address: '전라남도',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: false
    },
    {
        name: 'BYC',
        esg: 'S',
        address: '서울특별시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: true
    },
    {
        name: 'CJ ENM',
        esg: 'S',
        address: '서울특별시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: false
    },
    {
        name: 'DL건설',
        esg: 'A+',
        address: '서울특별시',
        establishment: '2016-05-12',
        businessArea: 'Fintech',
        industryCode: '67890',
        status: true
    },
];

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
        <span class="company-name">기업명</span>
        <span class="company-address">지역</span>
        <span class="company-establishment">설립일자</span>
        <span class="company-businessArea">사업영역</span>
        <span class="company-industryCode">산업분류</span>
        <span class="company-esg">ESG 지표</span>
        <span class="company-status">산업현황</span>
    `;
    companyListDiv.appendChild(columnDiv);

    companyList.forEach(company => {
        const companyDiv = document.createElement('div');
        companyDiv.className = 'company-item';
        companyDiv.innerHTML = `
            <span class="bookmark" onclick="toggleBookmark(event)"><img src="/icons/bookmark2.png" alt="Bookmark Icon" width="20px" height="20px"></span>
            <span class="company-name">${company.name}</span>
            <span class="company-address">${company.address}</span>
            <span class="company-establishment">${company.establishment}</span>
            <span class="company-businessArea">${company.businessArea}</span>
            <span class="company-industryCode">${company.industryCode}</span>
            <span class="company-esg">${company.esg}</span>
            <span class="company-status"><img src="/icons/${company.status ? 'green_circle' : 'red_circle'}.png" alt="${company.status ? '긍정' : '부정'}" width="20px" height="20px"></span>`;
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
    const companyDetails = document.getElementById('company-details');
    document.getElementById('company-name').textContent = company.name;
    // 상세 정보 내용 업데이트
    companyDetails.classList.add('active');
    companyDetails.classList.remove('closed', 'collapsed');

    // URL 해시를 '기업개요'로 설정 -> 상세페이지 창을 열었을 때 기본적으로 기업개요가 나타나도록 함
    location.hash = '기업개요';

    // '기업개요' 탭을 표시
    showTabContent('기업개요');

    // 지도를 초기화하고 표시
    initMap();
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
    link.addEventListener('click', function(event) {
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