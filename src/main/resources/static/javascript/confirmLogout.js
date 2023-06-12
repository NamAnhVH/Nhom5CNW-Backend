function confirmLogout() {
    var confirmation = confirm("Bạn có chắc chắn muốn đăng xuất?");

    if (confirmation) {
        window.location.href = "/logout";
    }
}