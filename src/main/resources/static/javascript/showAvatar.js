document.getElementById("files").onchange = function () {
    var reader = new FileReader();
    reader.onload = function (e) {
        document.getElementById("avatar").src = e.target.result;
    };
    reader.readAsDataURL(this.files[0]);
};