var ctx = document.getElementById('myChart').getContext("2d");
const times = document.querySelector("#times");
const points = document.querySelector("#points");
const dbLevel = document.querySelector(".db-level");
const dbLevelInfo = document.querySelector(".db-level-info");

const timesJSON = JSON.parse(times.value);
const pointsJSON = JSON.parse(points.value);


function getInfoAboutNoise(dbLevel){
    if(dbLevel < 40){
        return 'Довольно тихо';
    }
    if (dbLevel < 60){
        return 'Небольшой шум';
    }
    if(dbLevel < 80){
        return 'Шумно';
    }
    if (dbLevel < 200){
        return 'Очень шумно';
    }
}

dbLevel.innerHTML = pointsJSON[pointsJSON.length - 1];
dbLevelInfo.innerHTML = getInfoAboutNoise(pointsJSON[pointsJSON.length - 1]);

console.log(timesJSON);
console.log(pointsJSON);

var gradientFill = ctx.createLinearGradient(0, 200, 0, 0);
gradientFill.addColorStop(0, "rgba(158, 0, 255, 0.0)");
gradientFill.addColorStop(1, "rgba(158, 0, 255, 0.6)");

//#80b6f4


const labels = timesJSON;

var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: labels,
        datasets: [{
            borderColor: "#9E00FF",
            // pointBorderColor: "#9E00FF",
            // pointBackgroundColor: "#9E00FF",
            pointHoverBackgroundColor:"#9E00FF",
            // pointHoverBorderColor: "#9E00FF",
            // pointBorderWidth: 10,
            pointHoverRadius: 10,
            pointHoverBorderWidth: 0.5,
            pointRadius: 0,
            fill: true,
            backgroundColor: gradientFill,
            borderWidth: 2,
            data: pointsJSON,
            cubicInterpolationMode: 'monotone',
        }]
    },
    options: {
        plugins:{
            legend: {
                display: false
            },
        },

        maintainAspectRatio: false,
        scales: {
            y: {

                position: "right",
                min: 5,
                max: 90,
                ticks:{
                    step: 20,
                    z: 1,
                    callback: function(value, index, ticks) {
                        if(index !== 0 && index !== 1 && index !== 2 && value !== 90 ){
                            return value;
                        }
                    },
                    padding: -45,
                    font: {
                        family: "Inter"
                    }
                },
                grid: {
                    display: false
                },
                offset: false,

            },
            x: {

                position: {
                  y: 25
                },
                grid: {
                    display: false
                },
                ticks:{

                    z: 1,
                    callback: function(value, index, ticks) {
                        if(index !== 0 && index !== (labels.length - 1)){
                            return labels[index];
                        }

                    },
                    font: {
                        family: "Inter"
                    }

                },
                offset: false,
            }
        }
    }
});