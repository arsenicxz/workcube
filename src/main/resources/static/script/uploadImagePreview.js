const imageButton = document.querySelector
(".image-button");

const chosenImage = document.querySelector
(".chosen-image");

const deleteButton = document.querySelector
(".delete-photo");

const photoTitle = document.querySelector
(".photo-title");

deleteButton.addEventListener("click", () => {
    removeImage();
})

imageButton.addEventListener("change", () => {
    let reader = new FileReader();
    reader.readAsDataURL(imageButton.files[0]);
    console.log(imageButton.files[0]);
    reader.onload = () => {
        chosenImage.setAttribute("src", reader.result);
    }
    deleteButton.classList.add("active");
    imageButton.setAttribute("name", "file");
    photoTitle.innerHTML = "Заменить фото";
})

function removeImage(){
    imageButton.removeAttribute("name");
    imageButton.value = "";
    chosenImage.setAttribute("src", "");
    deleteButton.classList.toggle("active");
    photoTitle.innerHTML = "Выберите фото";
}