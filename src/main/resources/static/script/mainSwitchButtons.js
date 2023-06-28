const switchButtons = document.querySelectorAll(".switch__button");
const filterForm = document.querySelector(".filter-form");
const viewType = document.querySelector("#view");
const viewTypeMobile = document.querySelector("#view-mobile");
const filterFormMobile = document.querySelector(".filter-form-mobile");


switchButtons.forEach(function (button){
    if(button.hasAttribute("mobile")){
        if(button.value === "list"){
            button.addEventListener("click", () => {
                viewTypeMobile.value = "list";
                filterFormMobile.submit();
                // location.href = "/main"
            })
        }else if(button.value === "map"){
            button.addEventListener("click", () => {
                viewTypeMobile.value = "map";
                filterFormMobile.submit();
                // location.href = "/main_map"
            })
        }
    }else{
        if(button.value === "list"){
            button.addEventListener("click", () => {
                viewType.value = "list";
                filterForm.submit();
                // location.href = "/main"
            })
        }else if(button.value === "map"){
            button.addEventListener("click", () => {
                viewType.value = "map";
                filterForm.submit();
                // location.href = "/main_map"
            })
        }
    }

})
