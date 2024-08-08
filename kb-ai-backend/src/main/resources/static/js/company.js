const companies = [
    { name: '에이젠글로벌', esg: 'A', address: '서울특별시', establishment: '2015-08-01', businessArea: 'IT', industryCode: '12345', status: true },
    { name: '앤톡', esg: 'B+', address: '부산광역시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890', status: false },
    // 다른 기업 데이터...
];

function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    const content = document.getElementById("content");
    sidebar.classList.toggle("active");
    content.classList.toggle("expanded");
}

document.querySelector('.search-clear').addEventListener('click', function() {
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

function searchCompanyInResults() {
    const searchWithinResults = document.getElementById('search-within-results').checked;
    const searchInput = document.getElementById('sidebar-search-input').value.toLowerCase();
    const filteredCompanies = companies.filter(company => company.name.toLowerCase().includes(searchInput));
    if (searchWithinResults) {
        displayCompanies(filteredCompanies);
    } else {
        displayCompanies(companies);
    }
}

function clearSearch() {
    document.getElementById('search-input').value = '';
    document.getElementById('sidebar-search-input').value = '';
    displayCompanies(companies);
}

function refreshCompanies() {
    clearSearch();
    displayCompanies(companies);
}

function displayCompanies(companyList) {
    const companyListDiv = document.getElementById('company-list');
    companyListDiv.innerHTML = '';

    // 컬럼명 추가
    const columnDiv = document.createElement('div');
    columnDiv.className = 'company-item columns';
    columnDiv.innerHTML = `
        <span class="bookmark"></span>
        <span class="company-name">회사명</span>
        <span class="company-address">주소</span>
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
            <span class="company-status"><img src="/icons/${company.status ? 'green_circle' : 'red_circle'}.png" alt="${company.status ? '긍정' : '부정'}" width="18px" height="18px"></span>
        `;
        companyDiv.addEventListener('click', () => displayCompanyDetails(company));
        companyListDiv.appendChild(companyDiv);
    });
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

function displayCompanyDetails(company) {
    alert(`Displaying details for ${company.name}`);
}

document.getElementById('search-input').addEventListener('input', searchCompany);
document.getElementById('esg-slider').addEventListener('input', filterCompanies);

// 초기 기업 리스트 표시
displayCompanies(companies);