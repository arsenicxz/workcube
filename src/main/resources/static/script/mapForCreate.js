ymaps.ready(init);

const latitudePlace = document.querySelector("#latitudePlace");
const longitudePlace = document.querySelector("#longitudePlace");

function init() {

    const input = document.querySelector("#input-address");
    const listResult = document.querySelector(".list-result");

    let listItems = document.querySelectorAll(".list-result__item");


    listItems.forEach(e => {
        e.addEventListener("click", () => {
            input.value = e.getAttribute("value");
        })
    });

    function getItOnPage(mas){
        listResult.innerHTML = "";
        mas.forEach((e)=>{
            listResult.style.display = "flex";
            let resultStr = e.title.split(',')[0];
            listResult.insertAdjacentHTML('beforeend', `<div latitude="${e.position.lat}" longitude="${e.position.lng}" value="${resultStr}" class="list-result__item">${resultStr}</div>`);
        })
        listItems = document.querySelectorAll(".list-result__item");
        listItems.forEach(e => {
            e.addEventListener("click", () => {
                input.value = e.getAttribute("value");
                latitudePlace.value = e.getAttribute("latitude");
                longitudePlace.value = e.getAttribute("longitude");
                setPlacemark([parseFloat(e.getAttribute("latitude")), parseFloat(e.getAttribute("longitude"))]);
                myMap.setCenter([parseFloat(e.getAttribute("latitude")), parseFloat(e.getAttribute("longitude"))]);
                listResult.style.display = "none";
            })
        });
        console.log(listItems);
    }

    input.addEventListener("input", () => {
        geocode(platform, input.value);
    });

    input.addEventListener("focus", () => {
        listResult.style.display = "flex";
    });

    function onSuccess(result) {
        var locations = result.items;
        console.log(locations);
        getItOnPage(locations);

    }

    function onError(error) {

    }

    var platform = new H.service.Platform({
        apikey: "?"
    });

    function geocode(platform, value) {
        var geocoder = platform.getSearchService(),
            geocodingParameters = {
                q: 'Cанкт-Петербуррг ' + value
            };

        geocoder.geocode(
            geocodingParameters,
            onSuccess,
            onError
        );
    }

    let myMap = new ymaps.Map('map', {
            center: [59.94, 30.32],
            zoom: 12,
            controls: ['zoomControl', 'typeSelector']
        }, {
            searchControlProvider: 'yandex#search'
        }),
        myPlacemark;

    function setPlacemark(coords){
        if (myPlacemark) {
            myPlacemark.geometry.setCoordinates(coords);
        }
        else {
            myPlacemark = createPlacemark(coords);
            myMap.geoObjects.add(myPlacemark);
        }
    }

    function createPlacemark(object) {
        return new ymaps.Placemark(object, {}, {
            preset: 'islands#violetDotIconWithCaption',
            draggable: false
        });
    }


    function getCoordinates(coords) {
        myPlacemark.properties
            .set({
                balloonContentHeader: "Координаты",
                balloonContentBody: coords,
            });
    }
}





