function editQuiz(id) {
    var editQuiz = document.getElementById('editQuiz' + id)
    if (editQuiz.style.display === 'none') {
        editQuiz.style.display = 'block';
    } else {
        editQuiz.style.display = 'none';
    }
}