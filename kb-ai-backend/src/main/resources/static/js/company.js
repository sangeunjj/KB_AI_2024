const companies = [
    { name: '에이젠글로벌', esg: 'A', address: '서울특별시', establishment: '2015-08-01', businessArea: 'IT', industryCode: '12345' },
    { name: '앤톡', esg: 'B+', address: '부산광역시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: 'AJ네트웍스', esg: 'B+', address: '대구광역시', establishment: '2010-01-24', businessArea: 'IT', industryCode: '095570' },
    { name: '앤톡', esg: 'B+', address: '인천광역시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '광주광역시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '대전광역시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '울산광역시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '세종특별자치시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '경기도', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '전라남도', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '서울특별시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '서울특별시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
    { name: '앤톡', esg: 'B+', address: '서울특별시', establishment: '2016-05-12', businessArea: 'Fintech', industryCode: '67890' },
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
        return esgOrder.indexOf(company.esg) <= esgOrder.indexOf(esgValue);
    });
    displayCompanies(filteredCompanies);
}

function searchCompany() {
    const searchInput = document.getElementById('search-input').value.toLowerCase();
    const filteredCompanies = companies.filter(company => company.name.toLowerCase().includes(searchInput));
    displayCompanies(filteredCompanies);
}

function displayCompanies(companyList) {
    const companyListDiv = document.getElementById('company-list');
    companyListDiv.innerHTML = '';

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
        `;
        companyDiv.addEventListener('click', () => displayCompanyDetails(company));
        companyListDiv.appendChild(companyDiv);
    });
}

function toggleBookmark(event) {
    event.stopPropagation();
    const bookmarkIcon = event.target;
    if (bookmarkIcon.src.includes('bookmark2.png')) {
        bookmarkIcon.src = '/icons/bookmark_blank1.png'; // 원하는 아이콘으로 변경
    } else {
        bookmarkIcon.src = '/icons/bookmark2.png';
    }
}

function displayCompanyDetails(company) {
    // 모달 또는 새로운 창에서 회사 정보를 표시하는 로직 추가
    alert(`Displaying details for ${company.name}`);
}

document.getElementById('search-input').addEventListener('input', searchCompany);
document.getElementById('esg-slider').addEventListener('input', filterCompanies);

// 초기 기업 리스트 표시
displayCompanies(companies);