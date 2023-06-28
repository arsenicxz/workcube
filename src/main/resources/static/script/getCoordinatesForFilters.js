const latitude = document.querySelector("#filterLatitude");
const longitude = document.querySelector("#filterLongitude");
const latitudeMobile = document.querySelector("#filterLatitudeMobile");
const longitudeMobile = document.querySelector("#filterLongitudeMobile");

const locationBadge = document.querySelector("#near-me-check");
const locationCheck = document.querySelector("#near-me-check-input");
const locationTitle = document.querySelector("#near-me-check-title");

const locationBadgeMobile = document.querySelector("#near-me-check-mobile");
const locationCheckMobile = document.querySelector("#near-me-check-input-mobile");
const locationTitleMobile = document.querySelector("#near-me-check-title-mobile");

if(locationCheck.checked || locationCheckMobile.checked){
    tryGetCoordinates();
}

locationCheck.addEventListener("click", function() {
    console.log("yes");
    if (locationCheck.checked) {
        tryGetCoordinates();
    }else{
        latitude.value = "";
        longitude.value = "";
        latitudeMobile.value = "";
        longitudeMobile.value = "";
    }
});

locationCheckMobile.addEventListener("click", function() {
    console.log("yesMobile");
    if (locationCheckMobile.checked) {
        tryGetCoordinates();
    }else{
        latitude.value = "";
        longitude.value = "";
        latitudeMobile.value = "";
        longitudeMobile.value = "";
    }
});

function tryGetCoordinates(){
    if(navigator.geolocation){
        locationTitle.classList.add("loading");
        locationTitle.classList.add("loading");
        locationCheckMobile.disabled = true;
        locationCheck.disabled = true;

        navigator.geolocation.getCurrentPosition(onSuccessForFilter, onErrorForFilter);
    }else{
        button.innerText = "Не удалось";
    }
}


function onSuccessForFilter(position){
    locationTitle.classList.remove("loading");
    locationTitleMobile.classList.remove("loading");
    locationCheck.disabled = false;
    locationCheckMobile.disabled = false;
    let {latitude, longitude} = position.coords;
    const latitudeForm = document.querySelector("#filterLatitude");
    const longitudeForm = document.querySelector("#filterLongitude");
    latitudeForm.value = latitude;
    longitudeForm.value = longitude;
    latitudeMobile.value = latitude;
    longitudeMobile.value = longitude;

    console.log(latitude, longitude);
}

function onErrorForFilter(error){
    locationTitle.classList.remove("loading");
    locationTitleMobile.classList.remove("loading");
    if(error.code == 1){
        locationTitle.innerText = "Запрос отклонен";
        locationTitleMobile.innerText = "Запрос отклонен";
    }else if(error.code == 2){
        locationTitle.innerText = "Недоступна";
        locationTitleMobile.innerText = "Недоступна";
    }else{
        locationTitle.innerText = "Что-то пошло не так";
        locationTitleMobile.innerText = "Что-то пошло не так";
    }

    locationCheck.disabled = true;
    locationCheckMobile.disabled = true;
}