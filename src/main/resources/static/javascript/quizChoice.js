function updateHiddenField(radio) {
    var hiddenField = radio.parentNode.nextElementSibling;
    hiddenField.value = radio.checked ? radio.value : '';
}