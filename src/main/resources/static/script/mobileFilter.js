const filterButton = document.querySelector("#mobileFilter");
const closeFilterButton = document.querySelector("#closeMobileFilter");
const filterMenu = document.querySelector(".filter-menu-content");
const body = document.querySelector('body');

filterButton.addEventListener("click", () => {
    filterMenu.classList.toggle("active");
    body.classList.toggle('lock');
})

closeFilterButton.addEventListener("click", () => {
    filterMenu.classList.toggle("active");
    body.classList.toggle('lock');
})