ymaps.ready(init);

function init() {
    let createAreaButton = document.querySelector("#createAreaButton");
    let inputArea = document.querySelector("#inputAreaRadius");
    let spanCenterArea = document.querySelector("#coordinatesCurrentArea");

    const showMe = document.querySelector("#showMe");
    const latitudeForm = document.querySelector("#latitude").value;
    const longitudeForm = document.querySelector("#longitude").value;

    const places = document.querySelector("#places");
    const placesJSON = JSON.parse(places.value);

    console.log(places.value);



    let myMap = new ymaps.Map('map', {
            center: showMe ? [latitudeForm, longitudeForm] : [59.94, 30.32],
            zoom: showMe ? 14 : 12,
            controls: ['zoomControl', 'typeSelector', 'trafficControl']
        }, {
            searchControlProvider: 'yandex#search'
        }),
        myPlacemark,
        circle;

    // myMap.events.add('click', function (e) {
    //     let coords = e.get('coords');
    //     console.log(coords);
    //     if (myPlacemark) {
    //         myPlacemark.geometry.setCoordinates(coords);
    //     }
    //     else {
    //         myPlacemark = createPlacemark(coords);
    //         myMap.geoObjects.add(myPlacemark);
    //     }
    //
    //     getCoordinates(coords);
    //
    //     inputArea.disabled = false;
    //     createAreaButton.disabled = false;
    //
    //     console.log("coordinates:", myPlacemark.geometry.getCoordinates());
    //     createAreaButton.onclick = () => {createArea(coords);};
    // });

    function createPlacemark(object) {
        return new ymaps.Placemark([object.latitude, object.longitude], {
            // Зададим содержимое заголовка балуна.
            balloonContentHeader: `<h3 href = "#">${object.name}</h3>` +
            `<span class="baloon-description">
                <span>${object.type}</span><div class="point">
                <div class="point-img"></div>
                <div class="point-text">${object.rating != 0 ? object.rating : "Отсутствует"}</div>
            </div></span>`,
            // Зададим содержимое основной части балуна.
            balloonContentBody:
                `<div class="baloon-content"><div class="baloon-img-container"><img class="image-content" src="/img/${object.photo}"></div>` +
                `<button onclick="location.href='/place/${object.id}'" class="button-regular">Открыть</button></div>`,
            // Зададим содержимое всплывающей подсказки.
            hintContent: `${object.name}`
        }, {
            preset: 'islands#violetDotIconWithCaption',
            draggable: false
        });
    }

    function createMyPlacemark(latitude, longitude) {
        return new ymaps.Placemark([latitude, longitude], {}, {
            preset: 'islands#circleDotIcon',
            iconColor: '#E0E0E0',
            draggable: false
        });
    }

    function placesOnMap(places){
        for(i = 0; i<places.length; i++){
            let newPlacemark = createPlacemark(places[i]);
            myMap.geoObjects.add(newPlacemark);
        }
    }
    function meOnMap(){
        if(showMe){

            let newPlacemark = createMyPlacemark(latitudeForm, longitudeForm);
            myMap.geoObjects.add(newPlacemark);
            createArea([latitudeForm, longitudeForm]);
        }
    }
    placesOnMap(placesJSON);
    meOnMap();
    function getCoordinates(coords) {
        myPlacemark.properties
            .set({
                balloonContentHeader: "Координаты",
                balloonContentBody: coords,
            });
    }

    function createArea(coords){

        if(circle){
            circle.geometry.setCoordinates(coords);
            circle.geometry.setRadius(inputArea.value);
            spanCenterArea.innerHTML = coords;
        }
        else{
            circle = new ymaps.Circle([coords, 1000], null, {
                draggable: false,
                fillColor: "ffffff99",
                strokeColor: "9E00FF"
            } );
            myMap.geoObjects.add(circle);
            spanCenterArea.innerHTML = coords;
        }

        circle.events.add('click', function (e) {
            let coords = e.get('coords');

            if (myPlacemark) {
                myPlacemark.geometry.setCoordinates(coords);
            }
            else {
                myPlacemark = createPlacemark(coords);
                myMap.geoObjects.add(myPlacemark);
            }

            getCoordinates(coords);
            inputArea.disabled = false;
            createAreaButton.disabled = false;
            createAreaButton.onclick = () => {createArea(coords);};
        });
    }

    console.log("3000: ",
        ymaps.coordSystem.geo.getDistance
        ([59.985066165388034,30.343844417788915],
            [59.958664372304774,30.354507804085678]));

    console.log("7000: ",
        ymaps.coordSystem.geo.getDistance
        ([59.903191108103286,30.486951293912632],
            [59.88405600147845,30.367807729304342]));


}

const button = document.querySelector(".map-button");

button.addEventListener("click", ()=>{
    if(navigator.geolocation){
        button.classList.add("loading");
        button.innerText = "Предоставьте доступ";
        navigator.geolocation.getCurrentPosition(onSuccess, onError);
    }else{
        button.innerText = "Не удалось";
    }
});

function onSuccess(position){
    button.classList.remove("loading");
    button.innerText = "Рядом со мной";
    let {latitude, longitude} = position.coords;
    let latitudeForm = document.querySelector("#latitude");
    let longitudeForm = document.querySelector("#longitude");
    latitudeForm.value = latitude;
    longitudeForm.value = longitude;
    let form = document.querySelector("#location-form");
    console.log(form);
    form.submit();
    // var params = `latitude=${latitude}&longitude=${longitude}`;
    //
    // xhr.send(params);


    console.log(latitude, longitude);
}

function onError(error){
    button.classList.remove("loading");
    if(error.code == 1){
        button.innerText = "Запрос отклонен";
    }else if(error.code == 2){
        button.innerText = "Недоступна";
    }else{
        button.innerText = "Что-то пошло не так";
    }
    button.setAttribute("disabled", "true");
}


document.addEventListener("DOMContentLoaded", function(event) {
    const places = document.querySelector("#places");
    const placesJSON = JSON.parse(places.value);

    console.log(placesJSON);
});