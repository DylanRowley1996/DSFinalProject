function ischeckemail(){
    var email = document.getElementById("email").value;
    if (email != "") {
        var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        isok= reg.test(email);
        if (!isok) {
            alert("Wrong Email Address");
            document.getElementById("email").focus();
            return false;
        }
    }
    return true;
}