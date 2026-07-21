// ===============================
// URL'DEN ÜRÜN KODUNU AL
// ===============================

const urlBilgileri =
    new URLSearchParams(window.location.search);

const urunKodu =
    urlBilgileri.get("urunKodu");

let currentPage = 1;
const recordsPerPage = 5;
let filteredHareketler = [];

// ===============================
// GÜNCEL ÜRÜN BİLGİLERİNİ GETİR
// ===============================

function urunDetayiniGetir() {

    const detayAlani =
        document.getElementById("urunDetay");

    if (!urunKodu) {

        detayAlani.innerHTML =
            "<p>Ürün kodu gönderilmedi.</p>";

        return;
    }

    const adres =
        "/api/urun?urunKodu=" +
        encodeURIComponent(urunKodu);

    fetch(adres, {
        method: "GET"
    })
        .then(function (response) {

            if (!response.ok) {

                return response
                    .text()
                    .then(function (mesaj) {

                        throw new Error(mesaj);
                    });
            }

            return response.json();
        })
        .then(function (urun) {

            urunDetayiniGoster(urun);
        })
        .catch(function (hata) {

            console.error(
                "Ürün detayı alınamadı:",
                hata
            );

            detayAlani.innerHTML =
                "<p>Ürün bilgileri alınamadı: " +
                hata.message +
                "</p>";
        });
}

// ===============================
// ÜRÜN DETAYINI EKRANA YAZ
// ===============================

function urunDetayiniGoster(urun) {

    const detayAlani =
        document.getElementById("urunDetay");

    let stokDurumu;

    if (urun.stokMiktari <= urun.kritikLimit) {

        stokDurumu = "🔴 Kritik Stok";

    } else {

        stokDurumu = "🟢 Stok Yeterli";
    }

    detayAlani.innerHTML = `
        <div class="cargo-form">

            <h2>📦 Güncel Ürün Bilgileri</h2>

            <p><strong>Ürün Kodu:</strong> ${urun.urunKodu || ""}</p>
            <p><strong>Ürün Adı:</strong> ${urun.urunAdi || ""}</p>
            <p><strong>Kategori:</strong> ${urun.kategori || ""}</p>
            <p><strong>Marka:</strong> ${urun.marka || ""}</p>
            <p><strong>Tedarikçi:</strong> ${urun.tedarikci || ""}</p>
            <p><strong>Depo:</strong> ${urun.depo || ""}</p>
            <p><strong>Raf No:</strong> ${urun.rafNo || ""}</p>
            <p><strong>Birim:</strong> ${urun.birim || ""}</p>
            <p><strong>Stok Miktarı:</strong> ${urun.stokMiktari ?? ""}</p>
            <p><strong>Kritik Limit:</strong> ${urun.kritikLimit ?? ""}</p>
            <p><strong>Giriş Tarihi:</strong> ${urun.girisTarihi || ""}</p>
            <p><strong>Durum:</strong> ${stokDurumu}</p>

        </div>
    `;
}

// ===============================
// STOK GEÇMİŞİNİ GETİR
// ===============================

function stokGecmisiniGetir() {

    const tabloGovdesi =
        document.getElementById("stokGecmisTableBody");

    const secilenIslem =
        document.getElementById("islemTuruFiltre").value;

    if (!urunKodu) {

        tabloGovdesi.innerHTML = `
            <tr>
                <td colspan="15">
                    Ürün kodu gönderilmedi.
                </td>
            </tr>
        `;

        return;
    }

    const adres =
        "/api/stok-gecmis?urunKodu=" +
        encodeURIComponent(urunKodu);

    fetch(adres, {
        method: "GET"
    })
        .then(function (response) {

            if (!response.ok) {

                return response
                    .text()
                    .then(function (mesaj) {

                        throw new Error(mesaj);
                    });
            }

            return response.json();
        })
        .then(function (hareketler) {

            // İşlem türüne göre filtrele
            if (secilenIslem !== "Tümü") {
                hareketler = hareketler.filter(function (hareket) {
                    return hareket.islemTuru === secilenIslem;
                });
            }

            filteredHareketler = hareketler;
            currentPage = 1;
            renderTable();

        })
        .catch(function (hata) {

            console.error(
                "Stok geçmişi alınamadı:",
                hata
            );

            tabloGovdesi.innerHTML = `
                <tr>
                    <td colspan="15">
                        Stok geçmişi alınamadı:
                        ${hata.message}
                    </td>
                </tr>
            `;
        });
}

// ===============================
// FİLTRE DEĞİŞİNCE LİSTEYİ YENİLE
// ===============================

document
    .getElementById("islemTuruFiltre")
    .addEventListener(
        "change",
        stokGecmisiniGetir
    );

function renderTable() {
    const tabloGovdesi = document.getElementById("stokGecmisTableBody");
    const pageInfo = document.getElementById("pageInfo");
    
    if (filteredHareketler.length === 0) {
        tabloGovdesi.innerHTML = `<tr><td colspan="15">Bu kritere uygun kayıt bulunamadı.</td></tr>`;
        pageInfo.textContent = "Sayfa 1 / 1";
        return;
    }
    
    const totalPages = Math.ceil(filteredHareketler.length / recordsPerPage);
    if (currentPage > totalPages) currentPage = totalPages;
    if (currentPage < 1) currentPage = 1;
    
    pageInfo.textContent = "Sayfa " + currentPage + " / " + totalPages;
    
    const startIdx = (currentPage - 1) * recordsPerPage;
    const endIdx = startIdx + recordsPerPage;
    const pageData = filteredHareketler.slice(startIdx, endIdx);
    
    tabloGovdesi.innerHTML = "";
    pageData.forEach(function (hareket) {
        tabloGovdesi.innerHTML += `
            <tr>
                <td>${hareket.islemTarihi || ""}</td>
                <td>${hareket.islemTuru || ""}</td>
                <td>${hareket.urunAdi || ""}</td>
                <td>${hareket.kategori || ""}</td>
                <td>${hareket.marka || ""}</td>
                <td>${hareket.tedarikci || ""}</td>
                <td>${hareket.depo || ""}</td>
                <td>${hareket.rafNo || ""}</td>
                <td>${hareket.birim || ""}</td>
                <td>${hareket.stokMiktari ?? ""}</td>
                <td>${hareket.kritikLimit ?? ""}</td>
                <td>${hareket.girisTarihi || ""}</td>
                <td>${hareket.miktar ?? ""}</td>
                <td>${hareket.oncekiStok ?? ""}</td>
                <td>${hareket.yeniStok ?? ""}</td>
            </tr>
        `;
    });
}

document.getElementById("prevPageBtn").addEventListener("click", function() {
    if (currentPage > 1) {
        currentPage--;
        renderTable();
    }
});

document.getElementById("nextPageBtn").addEventListener("click", function() {
    const totalPages = Math.ceil(filteredHareketler.length / recordsPerPage);
    if (currentPage < totalPages) {
        currentPage++;
        renderTable();
    }
});

document.getElementById("jumpPageBtn").addEventListener("click", function() {
    const jumpInput = document.getElementById("jumpPageInput").value;
    const page = parseInt(jumpInput, 10);
    const totalPages = Math.ceil(filteredHareketler.length / recordsPerPage);
    
    if (!isNaN(page) && page >= 1 && page <= totalPages) {
        currentPage = page;
        renderTable();
    } else {
        alert("Lütfen 1 ile " + totalPages + " arasında geçerli bir sayfa numarası giriniz.");
    }
});

// ===============================
// SAYFA AÇILINCA
// ===============================

urunDetayiniGetir();
stokGecmisiniGetir();