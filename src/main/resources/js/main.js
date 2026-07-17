// ==========================================
// OTURUM KONTROLÜ VE YÖNLENDİRME (SECURITY)
// ==========================================

// Eğer kullanıcı giriş yapmamışsa ve giriş sayfasında (login) değilse giriş sayfasına yönlendir
const kullaniciToken = localStorage.getItem("token");
if (!kullaniciToken && window.location.pathname !== "/login") {
    window.location.href = "/login";
}

// Eğer kullanıcı zaten giriş yapmışsa ve giriş sayfasındaysa ana sayfaya yönlendir
if (kullaniciToken && window.location.pathname === "/login") {
    window.location.href = "/";
}

// 401 Yetkisiz Erişim hatası alındığında otomatik çıkış yapıp login'e yönlendirmek için fetch fonksiyonunu sarmallıyoruz
const orjinalFetch = window.fetch;
window.fetch = async function(...parametreler) {
    const yanit = await orjinalFetch(...parametreler);
    
    // Sunucu 401 (Yetkisiz Erişim) döndürdüyse oturumu sonlandır ve login sayfasına yönlendir
    if (yanit.status === 401 && window.location.pathname !== "/login") {
        localStorage.removeItem("token");
        localStorage.removeItem("rol");
        window.location.href = "/login";
    }
    
    return yanit;
};

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

const adminCard = document.getElementById("adminCard");
if (adminCard && localStorage.getItem('rol') === 'ADMIN') {
    adminCard.style.display = 'block';
}

// ===============================
// OTURUM KAPATMA (LOGOUT) İŞLEMİ
// ===============================
const cikisButonu = document.getElementById("logoutBtn");

if (cikisButonu) {
    cikisButonu.addEventListener("click", async function () {
        const aktifToken = localStorage.getItem("token");
        if (aktifToken) {
            try {
                // Sunucuya oturum kapatma isteği gönderilir
                await fetch('/api/logout', {
                    method: 'POST',
                    headers: { 'Authorization': aktifToken }
                });
            } catch (hata) {
                console.error("Çıkış yapılırken hata oluştu:", hata);
            }
        }
        
        // Tarayıcı hafızasındaki (localStorage) token ve rol silinir
        localStorage.removeItem("token");
        localStorage.removeItem("rol");

        // Kullanıcı giriş sayfasına yönlendirilir
        window.location.href = "/login";
    });
}