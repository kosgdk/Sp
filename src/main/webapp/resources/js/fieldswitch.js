function disableFields(fields) {
    for (var i = 0; i < fields.length; i++) {
        fields[i].setAttribute("readonly", "readonly");
    }
}

function enableFields(fields) {
    for (var i = 0; i < fields.length; i++) {
        fields[i].removeAttribute("readonly");
    }
}