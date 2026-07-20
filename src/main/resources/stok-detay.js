// ===============================
// URL'DEN ÜRÜN KODUNU AL
// ===============================

const urlBilgileri =
    new URLSearchParams(window.location.search);

const urunKodu =
    urlBilgileri.get("urunKodu");

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

            tabloGovdesi.innerHTML = "";

            // İşlem türüne göre filtrele
            if (secilenIslem !== "Tümü") {

                hareketler =
                    hareketler.filter(function (hareket) {

                        return hareket.islemTuru === secilenIslem;

                    });

            }

            if (hareketler.length === 0) {

                tabloGovdesi.innerHTML = `
                    <tr>
                        <td colspan="15">
                            Bu kritere uygun kayıt bulunamadı.
                        </td>
                    </tr>
                `;

                return;
            }

            hareketler.forEach(function (hareket) {

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

// ===============================
// SAYFA AÇILINCA
// ===============================

urunDetayiniGetir();
stokGecmisiniGetir();