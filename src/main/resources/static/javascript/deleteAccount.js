function submitForm() {
    var confirmation = confirm("Bạn có chắc chắn muốn xoá tài khoản?");
    if(confirmation){
        var form = document.createElement("form");
        form.method = "POST";
        form.action = "/account/deleteAccount";
        document.body.appendChild(form);
        form.submit();
    }
}