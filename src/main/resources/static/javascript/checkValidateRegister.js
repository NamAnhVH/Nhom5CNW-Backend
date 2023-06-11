function validateForm() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("check-password").value;
    var errorMessage = document.getElementById("error-message");

    if (username.length < 8 || !/^[a-zA-Z0-9]+$/.test(username)) {
        errorMessage.innerText = "Tên đăng nhập phải gồm ít nhất 8 ký tự và không có ký tự đặc biệt.";
        errorMessage.style.display = "block";
        return false;
    }

    if (password.length < 8 || !/^[a-zA-Z0-9]+$/.test(password)) {
        errorMessage.innerText = "Mật khẩu phải gồm ít nhất 8 ký tự và không có ký tự đặc biệt.";
        errorMessage.style.display = "block";
        return false;
    }

    if (password !== confirmPassword) {
        errorMessage.innerText = "Mật khẩu xác nhận không khớp.";
        errorMessage.style.display = "block";
        return false;
    }

    errorMessage.style.display = "none";
}