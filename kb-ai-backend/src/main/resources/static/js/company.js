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
    document.querySelectorAll('#region-select input[type="checkbox"]').forEach(checkbox => {
        checkbox.addEventListener('change', filterByRegion);
    });
});

function searchCompany() {
    const searchInput = document.getElementById('search-input').value.toLowerCase();
    fetch(`/api/company?search=${searchInput}`)
        .then(response => response.json())
        .then(companies => displayCompanies(companies))
        .catch(error => console.error('Error fetching company list:', error));
}

function clearSearch() {
    document.getElementById('search-input').value = '';
    refreshCompanies();
}

function refreshCompanies() {
    fetch('/api/company')
        .then(response => response.json())
        .then(companies => displayCompanies(companies))
        .catch(error => console.error('Error fetching company list:', error));
}

function filterByRegion() {
    const checkboxes = document.querySelectorAll('#region-select input[type="checkbox"]:checked');
    const selectedRegions = Array.from(checkboxes).map(checkbox => checkbox.value);
    const queryString = selectedRegions.map(region => `region=${region}`).join('&');

    fetch(`/api/company?${queryString}`)
        .then(response => response.json())
        .then(companies => displayCompanies(companies))
        .catch(error => console.error('Error filtering companies by region:', error));
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
        <span class="company-ceo">대표자명</span>
    `;
    companyListDiv.appendChild(columnDiv);

    companyList.forEach(company => {
        const companyDiv = document.createElement('div');
        companyDiv.className = 'company-item';
        companyDiv.innerHTML = `
            <span class="bookmark" onclick="toggleBookmark(event)"><img src="/icons/bookmark2.png" alt="Bookmark Icon" width="20px" height="20px"></span>
            <span class="company-name">${company.corp_name}</span>
            <span class="company-address">${company.address}</span>
            <span class="company-establishment">${company.est_dt}</span>
            <span class="company-ceo">${company.ceo_nm}</span>
        `;

        // 여기에서 세부 페이지로 이동
        companyDiv.addEventListener('click', () => {
            window.location.href = `/company/${company.companyCode}`; // companyCode를 세부 페이지의 URL로 사용
        });

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
    fetch(`/api/company/${company.companyCode}`)
        .then(response => response.json())
        .then(data => {
            const companyDetails = document.getElementById('company-details');

            const companyData = data.company;
            const dartData = data.dartResponse;

            document.getElementById('company-name').textContent = dartData.corp_name || companyData.companyName;
            document.getElementById('company-name2').textContent = dartData.corp_name || companyData.companyName;
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

            companyDetails.classList.add('active');
            companyDetails.classList.remove('closed', 'collapsed');

            location.hash = '기업개요';
            showTabContent('기업개요');

            initMap();
            updateActiveTab('기업개요');
        })
        .catch(error => console.error('Error fetching company details:', error));
}

function updateActiveTab(tabId) {
    document.querySelectorAll('.com-details-index a').forEach(link => {
        link.classList.remove('active');
    });
    document.querySelector(`.com-details-index a[href="#${tabId}"]`).classList.add('active');
}

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

    window.history.pushState('', document.title, window.location.pathname);
}

function showTabContent(tabId) {
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.add('hidden');
    });
    document.getElementById(tabId).classList.remove('hidden');
}

function initMap() {
    const mapContainer = document.getElementById('map');

    if (mapContainer.innerHTML.trim() !== "") {
        return;
    }

    const mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3
    };

    const map = new kakao.maps.Map(mapContainer, mapOption);
}

function getFirstWordOfAddress(address) {
    if (address) {
        return address.split(' ')[0];
    }
    return '';
}