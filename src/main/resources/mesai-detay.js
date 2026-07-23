// ===============================
// URL'DEN MESAİ NUMARASINI AL
// ===============================

const urlBilgileri =
    new URLSearchParams(window.location.search);

const mesaiNo =
    urlBilgileri.get("mesaiNo");


// ===============================
// GÜNCEL MESAİ BİLGİLERİNİ GETİR
// ===============================

function mesaiDetayiniGetir() {

    if (!mesaiNo) {

        alert(
            "Mesai numarası gönderilmedi."
        );

        return;
    }

    const adres =
        "/api/mesai?mesaiNo=" +
        encodeURIComponent(mesaiNo);

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

        .then(function (mesai) {

            console.log(
                "Gelen mesai bilgisi:",
                mesai
            );

            mesaiDetayiniGoster(
                mesai
            );

            mesaiGecmisiniYukle(
                mesai.mesaiNo
            );

        })

        .catch(function (hata) {

            console.error(
                "Mesai detayı alınamadı:",
                hata
            );

            alert(
                "Mesai bilgileri alınamadı: " +
                hata.message
            );

        });

}


// ===============================
// MESAİ DETAYINI EKRANA YAZ
// ===============================

function mesaiDetayiniGoster(mesai) {

    mesaiKayitBilgileriniGoster(
        mesai
    );

    mesaiUcretBilgileriniGoster(
        mesai
    );

    mesaiOperasyonBilgileriniGoster(
        mesai
    );

    mesaiOnayBilgileriniGoster(
        mesai
    );

}


// ===============================
// MESAİ KAYIT BİLGİLERİNİ GÖSTER
// ===============================

function mesaiKayitBilgileriniGoster(mesai) {

    const tabloGovdesi =
        document.getElementById(
            "mesaiDetayTableBody"
        );

    if (!tabloGovdesi) {

        console.error(
            "Mesai detay tablosu bulunamadı."
        );

        return;
    }

    tabloGovdesi.innerHTML = `
        <tr>
            <td>${degerGoster(mesai.mesaiNo)}</td>
            <td>${degerGoster(mesai.sicilNo)}</td>
            <td>${degerGoster(mesai.adSoyad)}</td>
            <td>${degerGoster(mesai.departman)}</td>
            <td>${degerGoster(mesai.pozisyon)}</td>
            <td>${degerGoster(mesai.mesaiTarihi)}</td>
            <td>${degerGoster(mesai.baslangicSaati)}</td>
            <td>${degerGoster(mesai.bitisSaati)}</td>
            <td>${degerGoster(mesai.molaSuresi)}</td>
            <td>${degerGoster(mesai.toplamMesaiSaati)}</td>
            <td>${degerGoster(mesai.mesaiNedeni)}</td>
            <td>${degerGoster(mesai.yapilanIs)}</td>
            <td>${degerGoster(mesai.projeOperasyon)}</td>
            <td>${degerGoster(mesai.subeDepo)}</td>
            <td>${degerGoster(mesai.aciklama)}</td>
        </tr>
    `;

}


// ===============================
// ÜCRET BİLGİLERİNİ GÖSTER
// ===============================

function mesaiUcretBilgileriniGoster(mesai) {

    const tabloGovdesi =
        document.getElementById(
            "mesaiUcretDetayTableBody"
        );

    if (!tabloGovdesi) {

        console.error(
            "Mesai ücret tablosu bulunamadı."
        );

        return;
    }

    tabloGovdesi.innerHTML = `
        <tr>
            <td>${paraGoster(mesai.aylikUcret)}</td>
            <td>${degerGoster(mesai.aylikCalismaSaati)}</td>
            <td>${degerGoster(mesai.mesaiKatsayisi)}</td>
            <td>${paraGoster(mesai.normalSaatlikUcret)}</td>
            <td>${paraGoster(mesai.mesaiSaatUcreti)}</td>
            <td>${paraGoster(mesai.toplamMesaiUcreti)}</td>
        </tr>
    `;

}


// ===============================
// LOJİSTİK OPERASYON BİLGİLERİNİ GÖSTER
// ===============================

function mesaiOperasyonBilgileriniGoster(mesai) {

    const tabloGovdesi =
        document.getElementById(
            "mesaiOperasyonDetayTableBody"
        );

    if (!tabloGovdesi) {

        console.error(
            "Mesai operasyon tablosu bulunamadı."
        );

        return;
    }

    tabloGovdesi.innerHTML = `
        <tr>
            <td>${degerGoster(mesai.rotaPlaniVarMi)}</td>
            <td>${degerGoster(mesai.siraliTeslimat)}</td>
            <td>${degerGoster(mesai.teslimatSorunu)}</td>
            <td>${degerGoster(mesai.yerindeKapatma)}</td>
            <td>${degerGoster(mesai.irsaliyeAtfNo)}</td>
        </tr>
    `;

}


// ===============================
// ONAY VE ÖDEME BİLGİLERİNİ GÖSTER
// ===============================

function mesaiOnayBilgileriniGoster(mesai) {

    const tabloGovdesi =
        document.getElementById(
            "mesaiOnayDetayTableBody"
        );

    if (!tabloGovdesi) {

        console.error(
            "Mesai onay tablosu bulunamadı."
        );

        return;
    }

    tabloGovdesi.innerHTML = `
        <tr>
            <td>${degerGoster(mesai.yonetici)}</td>
            <td>${degerGoster(mesai.onayDurumu)}</td>
            <td>${degerGoster(mesai.onayTarihi)}</td>
            <td>${degerGoster(mesai.odemeDurumu)}</td>
            <td>${degerGoster(mesai.odemeTarihi)}</td>
        </tr>
    `;

}


// ===============================
// MESAİ GEÇMİŞİNİ GETİR
// ===============================

function mesaiGecmisiniYukle(mesaiNoDegeri) {

    const adres =
        "/api/mesai-gecmis?mesaiNo=" +
        encodeURIComponent(mesaiNoDegeri);

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

        .then(function (gecmisler) {

            console.log(
                "Mesai geçmişi:",
                gecmisler
            );

            mesaiGecmisiniGoster(
                gecmisler
            );

        })

        .catch(function (hata) {

            console.error(
                "Mesai geçmişi alınamadı:",
                hata
            );

            const tabloGovdesi =
                document.getElementById(
                    "mesaiGecmisTableBody"
                );

            if (tabloGovdesi) {

                tabloGovdesi.innerHTML = `
                    <tr>
                        <td colspan="9">
                            Mesai geçmişi alınamadı.
                        </td>
                    </tr>
                `;

            }

        });

}


// ===============================
// MESAİ GEÇMİŞİNİ EKRANA YAZ
// ===============================

function mesaiGecmisiniGoster(gecmisler) {

    const tabloGovdesi =
        document.getElementById(
            "mesaiGecmisTableBody"
        );

    if (!tabloGovdesi) {

        console.error(
            "Mesai geçmiş tablosu bulunamadı."
        );

        return;
    }

    tabloGovdesi.innerHTML = "";

    if (
        !gecmisler ||
        gecmisler.length === 0
    ) {

        tabloGovdesi.innerHTML = `
            <tr>
                <td colspan="9">
                    Mesai geçmişi alınamadı: ${degerGoster(hata.message)}
                </td>
            </tr>
        `;

        return;
    }

    gecmisler.forEach(function (gecmis) {

        const satir =
            document.createElement("tr");

        satir.innerHTML = `
            <td>${degerGoster(gecmis.mesaiNo)}</td>
            <td>${degerGoster(gecmis.islemTuru)}</td>
            <td>${degerGoster(gecmis.adSoyad)}</td>
            <td>${degerGoster(gecmis.mesaiTarihi)}</td>
            <td>${degerGoster(gecmis.toplamMesaiSaati)}</td>
            <td>${paraGoster(gecmis.toplamMesaiUcreti)}</td>
            <td>${degerGoster(gecmis.onayDurumu)}</td>
            <td>${degerGoster(gecmis.odemeDurumu)}</td>
            <td>${degerGoster(gecmis.islemTarihi)}</td>
        `;

        tabloGovdesi.appendChild(
            satir
        );

    });

}


// ===============================
// BOŞ DEĞERLERİ DÜZENLE
// ===============================

function degerGoster(deger) {

    if (
        deger === null ||
        deger === undefined ||
        deger === ""
    ) {

        return "-";
    }

    return deger;
}


// ===============================
// PARA DEĞERLERİNİ DÜZENLE
// ===============================

function paraGoster(deger) {

    if (
        deger === null ||
        deger === undefined ||
        deger === ""
    ) {

        return "-";
    }

    const sayi =
        Number(deger);

    if (isNaN(sayi)) {

        return deger;
    }

    return sayi.toFixed(2) + " TL";
}


// ===============================
// SAYFA AÇILINCA ÇALIŞTIR
// ===============================

mesaiDetayiniGetir();