const kargoCard = document.getElementById("kargoCard");
if (kargoCard) {
    kargoCard.addEventListener("click", function () {
        window.location.href = "/kargo";
    });
}

const stokCard = document.getElementById("stokCard");
if (stokCard) {
    stokCard.addEventListener("click", function () {
        window.location.href = "/stok";
    });
}

const ulasimCard = document.getElementById("ulasimCard");
if (ulasimCard) {
    ulasimCard.addEventListener("click", function () {
        window.location.href = "/ulasim";
    });
}

const mesaiCard = document.getElementById("mesaiCard");
if (mesaiCard) {
    mesaiCard.addEventListener("click", function () {
        window.location.href = "/mesai";
    });
}