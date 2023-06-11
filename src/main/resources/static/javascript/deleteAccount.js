function submitForm() {
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "/account/deleteAccount";
    document.body.appendChild(form);
    form.submit();
}