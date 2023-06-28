let submitButton = document.querySelector("#submitButton");
submitButton.addEventListener("click", ()=>{
    if(validateForm()){
        document.querySelector('#form').submit();
    }
})

function validateForm() {
    var names = [];
    document.querySelectorAll("input[type=radio]").forEach(function(radio) {
        var name = radio.getAttribute("name");
        if (!names.includes(name)) {
            names.push(name);
        }
    });

    for (var name of names) {
        var radios = document.getElementsByName(name);
        var formValid = false;
        var i = 0;
        while (!formValid && i < radios.length) {
            if (radios[i].checked) formValid = true;
            i++;
        }
        if (!formValid) {
            alert("Пожалуйста, выберите все необходимые варианты!");
            return false;
        }
    }
    return true;
}

