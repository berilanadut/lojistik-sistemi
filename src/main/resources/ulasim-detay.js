// ===============================
// URL'DEN PLAKAYI AL
// ===============================

const urlBilgileri =
    new URLSearchParams(window.location.search);

const plaka =
    urlBilgileri.get("plaka");

let tumGecmisler = [];
let duzenlenenUrunId = null;


// ===============================
// GÜNCEL ULAŞIM BİLGİLERİNİ GETİR
// ===============================

function ulasimDetayiniGetir() {

    if (!plaka) {

        alert("Plaka gönderilmedi.");

        return;
    }

    const adres =
        "/api/ulasim?plaka=" +
        encodeURIComponent(plaka);

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

        .then(function (ulasim) {

            ulasimDetayiniGoster(ulasim);

            ulasimGecmisiniYukle(
                ulasim.plaka
            );

        })

        .catch(function (hata) {

            console.error(
                "Ulaşım detayı alınamadı:",
                hata
            );

            alert(
                "Ulaşım bilgileri alınamadı."
            );

        });

}


// ===============================
// ULAŞIM DETAYINI EKRANA YAZ
// ===============================

function ulasimDetayiniGoster(ulasim) {

    document.getElementById("dPlaka").textContent =
        degerGoster(ulasim.plaka);

    document.getElementById("dSurucu").textContent =
        degerGoster(ulasim.surucu);

    document.getElementById("dBaslangic").textContent =
        degerGoster(ulasim.baslangic);

    document.getElementById("dVaris").textContent =
        degerGoster(ulasim.varis);

    document.getElementById("dRota").textContent =
        degerGoster(ulasim.rota);

    document.getElementById("dGuncelKonum").textContent =
        degerGoster(ulasim.guncelKonum);

    document.getElementById("dBaslangicZamani").textContent =
        degerGoster(ulasim.baslangicZamani);

    document.getElementById("dTahminiSure").textContent =
        degerGoster(ulasim.tahminiSure);

    document.getElementById("dToplamMesafe").textContent =
        degerGoster(ulasim.toplamMesafe);

    document.getElementById("dYakit").textContent =
        degerGoster(ulasim.yakit);

    document.getElementById("dRotadanCikti").textContent =
        degerGoster(ulasim.rotadanCikti);

    //document.getElementById("dTeslimAlindi").textContent =
      //  degerGoster(ulasim.teslimAlindi);

    //document.getElementById("dKoliNo").textContent =
    //    degerGoster(ulasim.koliNo);

    //document.getElementById("dTeslimAlmaZamani").textContent =
      //  degerGoster(ulasim.teslimAlmaZamani);

    //document.getElementById("dTeslimAlan").textContent =
      //  degerGoster(ulasim.teslimAlan);

    //document.getElementById("dTeslimAlinanFirma").textContent =
      //  degerGoster(ulasim.teslimAlinanFirma);

    //document.getElementById("dTeslimEdildi").textContent =
      //  degerGoster(ulasim.teslimEdildi);

    //document.getElementById("dTeslimZamani").textContent =
      //  degerGoster(ulasim.teslimZamani);

    //document.getElementById("dMusteri").textContent =
      //  degerGoster(ulasim.musteri);

    //document.getElementById("dOnayKodu").textContent =
      //  degerGoster(ulasim.onayKodu);

    document.getElementById("dRotaDurumu").textContent =
        degerGoster(ulasim.rotaDurumu);

    document.getElementById("dAciklama").textContent =
        degerGoster(ulasim.aciklama);

}


// ===============================
// ULAŞIM GEÇMİŞİNİ GETİR
// ===============================

function ulasimGecmisiniYukle(plakaDegeri) {

    const adres =
        "/api/ulasim-gecmis?plaka=" +
        encodeURIComponent(plakaDegeri);

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
                "Ulaşım geçmişi:",
                gecmisler
            );

            tumGecmisler = gecmisler;

            ulasimGecmisiniGoster(
                tumGecmisler
            );

        })

        .catch(function (hata) {

            console.error(
                "Ulaşım geçmişi alınamadı:",
                hata
            );

            const tabloGovdesi =
                document.getElementById(
                    "ulasimGecmisTableBody"
                );

            if (tabloGovdesi) {

                tabloGovdesi.innerHTML = `
                    <tr>
                        <td colspan="15">
                            Ulaşım geçmişi alınamadı.
                        </td>
                    </tr>
                `;

            }

        });

}


// ===============================
// ULAŞIM GEÇMİŞİNİ EKRANA YAZ
// ===============================

function ulasimGecmisiniGoster(gecmisler) {

    const tabloGovdesi =
        document.getElementById(
            "ulasimGecmisTableBody"
        );

    if (!tabloGovdesi) {

        console.error(
            "Ulaşım geçmişi tablosu bulunamadı."
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
                <td colspan="15">
                    Bu araca ait geçmiş kaydı bulunamadı.
                </td>
            </tr>
        `;

        return;
    }

    gecmisler.forEach(function (gecmis) {

        const satir =
            document.createElement("tr");

satir.innerHTML = `
    <td>${degerGoster(gecmis.islemTarihi)}</td>
    <td>${degerGoster(gecmis.islemTuru)}</td>
    <td>${degerGoster(gecmis.plaka)}</td>
    <td>${degerGoster(gecmis.surucu)}</td>
    <td>${degerGoster(gecmis.baslangic)}</td>
    <td>${degerGoster(gecmis.varis)}</td>
    <td>${degerGoster(gecmis.rota)}</td>
    <td>${degerGoster(gecmis.guncelKonum)}</td>
    <td>${degerGoster(gecmis.baslangicZamani)}</td>
    <td>${degerGoster(gecmis.tahminiSure)}</td>
    <td>${degerGoster(gecmis.toplamMesafe)}</td>
    <td>${degerGoster(gecmis.yakit)}</td>
    <td>${degerGoster(gecmis.rotadanCikti)}</td>
    <td>${degerGoster(gecmis.rotaDurumu)}</td>
    <td>${degerGoster(gecmis.aciklama)}</td>
`;
        tabloGovdesi.appendChild(
            satir
        );

    });

}


// ===============================
// ROTA DURUMUNA GÖRE FİLTRELE
// ===============================

const rotaDurumuFiltre =
    document.getElementById(
        "rotaDurumuFiltre"
    );

if (rotaDurumuFiltre) {

    rotaDurumuFiltre.addEventListener(
        "change",
        function () {

            const secilenDurum =
                this.value;

            if (secilenDurum === "Tümü") {

                ulasimGecmisiniGoster(
                    tumGecmisler
                );

                return;
            }

            const filtrelenmisGecmisler =
                tumGecmisler.filter(
                    function (gecmis) {

                        return gecmis.rotaDurumu ===
                            secilenDurum;

                    }
                );

            ulasimGecmisiniGoster(
                filtrelenmisGecmisler
            );

        }
    );

}


// ===============================
// ARAÇTAKİ ÜRÜNLERİ GETİR
// ===============================

function urunleriYukle() {

    if (!plaka) {

        console.error(
            "Ürünleri yüklemek için plaka bulunamadı."
        );

        return;
    }

    const adres =
        "/api/ulasim-urun?plaka=" +
        encodeURIComponent(plaka);

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

        .then(function (urunler) {

            console.log(
                "Araçtaki ürünler:",
                urunler
            );

            urunleriGoster(urunler);

        })

        .catch(function (hata) {

            console.error(
                "Araçtaki ürünler alınamadı:",
                hata
            );

            const tabloGovdesi =
                document.getElementById(
                    "urunTableBody"
                );

            if (tabloGovdesi) {

                tabloGovdesi.innerHTML = `
                    <tr>
                        <td colspan="6">
                            Araçtaki ürünler alınamadı.
                        </td>
                    </tr>
                `;

            }

        });

}


// ===============================
// ARAÇTAKİ ÜRÜNLERİ EKRANA YAZ
// ===============================

function urunleriGoster(urunler) {

    const tabloGovdesi =
        document.getElementById(
            "urunTableBody"
        );

    if (!tabloGovdesi) {

        console.error(
            "Ürün tablosu bulunamadı."
        );

        return;
    }

    tabloGovdesi.innerHTML = "";

    if (
        !urunler ||
        urunler.length === 0
    ) {

        tabloGovdesi.innerHTML = `
            <tr>
                <td colspan="6">
                    Bu araca henüz ürün eklenmedi.
                </td>
            </tr>
        `;

        return;
    }

    urunler.forEach(function (urun) {

        const satir =
            document.createElement("tr");

        satir.innerHTML = `
            <td>${degerGoster(urun.urunKodu)}</td>
            <td>${degerGoster(urun.urunAdi)}</td>
            <td>${degerGoster(urun.miktar)}</td>
            <td>${degerGoster(urun.koliNo)}</td>
            <td>${degerGoster(urun.teslimDurumu)}</td>
            <td>

                <button
                    type="button"
                    onclick="duzenlemeyeBasla(${JSON.stringify(urun).replace(/"/g, '&quot;')})">

                    Güncelle

                </button>

                <button
                    type="button"
                    onclick="urunSil(${urun.id})">

                    Sil

                </button>

            </td>
        `;

        tabloGovdesi.appendChild(
            satir
        );

    });

}


// ===============================
// ARACA YENİ ÜRÜN EKLE
// ===============================

function urunEkle() {

    const urunKodu =
        document.getElementById(
            "urunKodu"
        ).value.trim();

    const urunAdi =
        document.getElementById(
            "urunAdi"
        ).value.trim();

    const miktarDegeri =
        document.getElementById(
            "miktar"
        ).value;

    const koliNo =
        document.getElementById(
            "koliNo"
        ).value.trim();

    const teslimDurumu =
        document.getElementById(
            "teslimDurumu"
        ).value;

    if (!plaka) {

        alert(
            "Ürün eklemek için plaka bulunamadı."
        );

        return;
    }

    if (
        urunKodu === "" ||
        urunAdi === "" ||
        miktarDegeri === ""
    ) {

        alert(
            "Ürün kodu, ürün adı ve miktar alanları zorunludur."
        );

        return;
    }

    const miktar =
        Number(miktarDegeri);

    if (
        isNaN(miktar) ||
        miktar <= 0
    ) {

        alert(
            "Miktar sıfırdan büyük bir sayı olmalıdır."
        );

        return;
    }

    const urunVerisi = {

        id: duzenlenenUrunId,
        plaka: plaka,
        urunKodu: urunKodu,
        urunAdi: urunAdi,
        miktar: miktar,
        koliNo: koliNo,
        teslimDurumu: teslimDurumu

    };

    let istekMetodu;

    if (duzenlenenUrunId === null) {

        istekMetodu = "POST";

    } else {

        istekMetodu = "PUT";

    }

    fetch("/api/ulasim-urun", {

        method: istekMetodu,

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(urunVerisi)

    })

        .then(function (response) {

            if (!response.ok) {

                return response
                    .text()
                    .then(function (mesaj) {

                        throw new Error(mesaj);

                    });

            }

            return response.text();

        })

        .then(function (mesaj) {

            alert(mesaj);

            urunFormunuTemizle();

            urunleriYukle();

        })

        .catch(function (hata) {

            console.error(
                "Ürün eklenemedi:",
                hata
            );

            alert(
                "Ürün eklenemedi: " +
                hata.message
            );

        });

}

function duzenlemeyeBasla(urun) {

    duzenlenenUrunId = urun.id;

    document.getElementById("urunKodu").value =
        urun.urunKodu;

    document.getElementById("urunAdi").value =
        urun.urunAdi;

    document.getElementById("miktar").value =
        urun.miktar;

    document.getElementById("koliNo").value =
        urun.koliNo;

    document.getElementById("teslimDurumu").value =
        urun.teslimDurumu;

    document.getElementById("urunEkleButonu").textContent =
        "✏️ Ürünü Güncelle";

}
// ===============================
// ARAÇTAN ÜRÜN SİL
// ===============================

function urunSil(id) {

    const onay =
        confirm(
            "Bu ürünü araçtan silmek istediğinize emin misiniz?"
        );

    if (!onay) {

        return;
    }

    const adres =
        "/api/ulasim-urun?id=" +
        encodeURIComponent(id);

    fetch(adres, {
        method: "DELETE"
    })

        .then(function (response) {

            if (!response.ok) {

                return response
                    .text()
                    .then(function (mesaj) {

                        throw new Error(mesaj);

                    });

            }

            return response.text();

        })

        .then(function (mesaj) {

            alert(mesaj);

            urunleriYukle();

        })

        .catch(function (hata) {

            console.error(
                "Ürün silinemedi:",
                hata
            );

            alert(
                "Ürün silinemedi: " +
                hata.message
            );

        });

}


// ===============================
// ÜRÜN FORMUNU TEMİZLE
// ===============================

function urunFormunuTemizle() {

    document.getElementById(
        "urunKodu"
    ).value = "";

    document.getElementById(
        "urunAdi"
    ).value = "";

    document.getElementById(
        "miktar"
    ).value = "";

    document.getElementById(
        "koliNo"
    ).value = "";

    document.getElementById(
        "teslimDurumu"
    ).value = "Teslim Edilmedi";

    duzenlenenUrunId = null;

    document.getElementById(
        "urunEkleButonu"
    ).textContent = "➕ Ürün Ekle";


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
// SAYFA AÇILINCA ÇALIŞTIR
// ===============================

ulasimDetayiniGetir();
urunleriYukle();