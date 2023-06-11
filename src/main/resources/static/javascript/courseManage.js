document.getElementById("files").onchange = function () {
    var reader = new FileReader();
    reader.onload = function (e) {
        document.getElementById("image-course").src = e.target.result;
    };
    reader.readAsDataURL(this.files[0]);
};
CKEDITOR.replace('description', {
    height: 560,
});