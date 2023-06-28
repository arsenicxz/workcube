const menu = document.querySelector(".filter-menu-content");
const details = document.querySelectorAll("details");

function updateWholeActivity(){
    const filterButton = document.querySelector("#mobileFilter");
    const allCheckedInputs = menu.querySelectorAll("input:checked").length;
    const allBigInputs = menu.querySelectorAll(".custom-input");
    const allMiniInputs = menu.querySelectorAll(".mini-input");
    let numberAllInputs = 0;
    allBigInputs.forEach((input) =>{
        if(input.value){
            numberAllInputs++;
        }
    })
    allMiniInputs.forEach((input) =>{
        if(input.value){
            numberAllInputs++;
        }
    })

    let totalCount = allCheckedInputs + numberAllInputs;
    filterButton.classList.toggle("active", totalCount > 0);
}

const inputs = menu.querySelectorAll("input");
inputs.forEach((item) => {
    item.addEventListener("change", () =>{
        updateWholeActivity();
    })
})


updateWholeActivity();

function updateActivity(detail){
    const title = detail.querySelector("summary");
    const checkedInputs = detail.querySelectorAll("input:checked").length;
    let numberInputsCount = 0;
    const numberInputs = detail.querySelectorAll(".mini-input");
    numberInputs.forEach((input) => {
        if(input.value){
            numberInputsCount++;
        }
    })
    let totalCount = checkedInputs + numberInputsCount;
    title.classList.toggle("active", totalCount > 0);
}

details.forEach(function(detail){
    console.log(detail);
    detail.querySelectorAll("input").forEach((item) => {
        item.addEventListener("change", () => {
            updateActivity(detail);
        })
    })

    updateActivity(detail);
})