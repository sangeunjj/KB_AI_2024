function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    const content = document.getElementById("content");
    sidebar.classList.toggle("active");
    content.classList.toggle("expanded");
}

document.querySelector('.search-clear').addEventListener('click', function() {
    document.querySelector('.search-container input').value = '';
});
