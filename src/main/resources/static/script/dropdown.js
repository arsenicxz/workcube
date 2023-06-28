window.onload = () => {
    const dropdowns = document.querySelectorAll(".dropdown");
    const dropdownGroups = document.querySelectorAll(".dropdown-group");
    const extraFilters = document.querySelector(".extra-filters");
    const overlay = document.querySelector(".overlay");


    let activeDropdown = null;
    let isExtraFiltersOn = false;

    function toggleDropdown(dropdown) {
        const dropdownLabel = dropdown.querySelector(".dropdown-label");
        const dropdownMenu = dropdown.querySelector(".dropdown-menu");
        if (activeDropdown !== dropdown) {
            if (activeDropdown) {
                toggleDropdown(activeDropdown);
            }
            overlay.classList.add("active");
            dropdownLabel.classList.add("active");
            dropdownMenu.classList.add("show");
            activeDropdown = dropdown;
        } else {
            activeDropdown = null;
            if (!isExtraFiltersOn) {
                overlay.classList.remove("active");
            }
            dropdownLabel.classList.remove("active");
            dropdownMenu.classList.remove("show");
        }
    }

    function updateCheckboxCount(dropdown) {
        const content = dropdown.querySelector(".dropdown-label-content");
        const dropdownCount = dropdown.querySelector(".dropdown-count");
        const checkedItemsCount = dropdown.querySelectorAll(".dropdown-item input:checked").length;
        let numberInputsCount = 0;
        const numberInputs = dropdown.querySelectorAll(".mini-input");
        numberInputs.forEach(function(input) {
            if (input.value) {
                numberInputsCount++;
            }
        });

        let totalCount = checkedItemsCount + numberInputsCount;
        content.classList.toggle("has-count", totalCount > 0);
        if(!dropdownCount.hasAttribute("no-text")){
            dropdownCount.textContent = checkedItemsCount;
        }

    }

    function blockInputs(dropdown){
        const numberInputs = dropdown.querySelectorAll(".mini-input");
        const checkedItemsCount = dropdown.querySelectorAll(".dropdown-item input:checked").length;
        if(numberInputs){
            if(checkedItemsCount > 0){
                numberInputs.forEach((input)=>{
                    input.setAttribute("disabled", "")
                })
            }else{
                numberInputs.forEach((input)=>{
                    input.removeAttribute("disabled")
                })
            }
        }
    }

    function closeAll() {
        if (activeDropdown) {
            toggleDropdown(activeDropdown);
        }
        overlay.classList.remove("active");
        extraFilters.classList.remove("show");
        isExtraFiltersOn = false;
    }

    function toggleExtraFilters() {
        isExtraFiltersOn = !isExtraFiltersOn;
        if (isExtraFiltersOn) {
            if (activeDropdown) {
                toggleDropdown(activeDropdown);
            }
            overlay.classList.add("active");
            extraFilters.classList.add("show");
        } else {
            closeAll();
        }
    }



    dropdowns.forEach(function (dropdown) {
        const dropdownLabel = dropdown.querySelector(".dropdown-label");
        const dropdownText = dropdown.querySelector(".dropdown-text");

        window.addEventListener('resize',(e) => {
            setWidth(dropdownLabel, dropdownText);
        });

        setWidth(dropdownLabel, dropdownText);

        dropdownLabel.addEventListener("click", function () {
            toggleDropdown(dropdown);
        });

        dropdown.querySelectorAll(".dropdown-item input").forEach(function (item) {
            item.addEventListener("change", function () {
                updateCheckboxCount(dropdown);
            });
        });

        dropdown.querySelectorAll(".mini-input").forEach(function (item) {
            item.addEventListener("change", function () {
                updateCheckboxCount(dropdown);
            });
        });

        updateCheckboxCount(dropdown);
    });

    window.addEventListener("click", function (event) {
        if (!dropdownGroups.some(group => group.contains(event.target))) {
            closeAll();
        }
    });

    overlay.addEventListener("click", closeAll);

    const plusButton = document.getElementById("plus-button");
    plusButton.addEventListener("click", toggleExtraFilters);
    const plusButtonIcon = plusButton.querySelector(".plus-button__icon");

    extraFilters.addEventListener("click", () => {
        if (activeDropdown) {
            toggleDropdown(activeDropdown);
        }
        activeDropdown = null;
    })

    function updateExtraActivity(){
        const extraSearch = extraFilters.querySelector(".custom-input");
        const extraCheckboxes = extraFilters.querySelectorAll("input:checked").length;

        let numberInputs = 0;
        if(extraSearch.value){
            numberInputs++;
        }

        let total = extraCheckboxes + numberInputs;
        plusButtonIcon.classList.toggle("exist", total > 0);

    }

    updateExtraActivity();

    const extraInputs = extraFilters.querySelectorAll("input");
    extraInputs.forEach((item) => {
        item.addEventListener("change", () => {
            updateExtraActivity();
        })
    })

};

function setWidth(dropdownLabel, dropdownText){
    let textWidth = dropdownText.offsetWidth;
    let labelWidth = textWidth + 50;
    dropdownLabel.style.width = labelWidth + "px";
}


