// ===============================
// ANA SAYFA KARGO KARTI
// ===============================
const kargoCard = document.getElementById("kargoCard");
if (kargoCard) {
    kargoCard.addEventListener("click", function () {
        window.location.href = "/kargo";
    });
}
// ===============================
// ANA SAYFA STOK KARTI
// ===============================
const stokCard =
    document.getElementById("stokCard");
if (stokCard) {
    stokCard.addEventListener("click", function () {
        window.location.href = "/stok";
    });
}
// ===============================
// ANA SAYFA ULAŞIM KARTI
// ===============================

const ulasimCard =
    document.getElementById("ulasimCard");
if (ulasimCard) {
    ulasimCard.addEventListener("click", function () {
        window.location.href = "/ulasim";
    });
}
// ===============================
// ANA SAYFA MESAİ KARTI
// ===============================

const mesaiCard =
    document.getElementById("mesaiCard");
if (mesaiCard) {
    mesaiCard.addEventListener("click", function () {
        window.location.href = "/mesai";
    });
}
// ===============================
// FORM BİLGİLERİNİ AL
// ===============================

function formBilgileriniAl() {

    const kargoNo = document.getElementById("kargoNo").value.trim();
    const gonderici = document.getElementById("gonderici").value.trim();
    const alici = document.getElementById("alici").value.trim();
    const gondericiSube = document.getElementById("gondericiSube").value.trim();
    const teslimatSube = document.getElementById("teslimatSube").value.trim();
    const desi = Number(document.getElementById("desi").value);
    const agirlik = Number(document.getElementById("agirlik").value);
    const durum = document.getElementById("durum").value;
    const verilisTarihi = document.getElementById("verilisTarihi").value;
    const tahminiTeslim = document.getElementById("tahminiTeslim").value;
    const teslimTarihi = document.getElementById("teslimTarihi").value;
    const plaka = document.getElementById("plaka").value.trim();
    const surucu = document.getElementById("surucu").value.trim();
    const takipNotu = document.getElementById("takipNotu").value.trim();

    const kargo = {
        kargoNo: kargoNo,
        gonderici: gonderici,
        alici: alici,
        gondericiSube: gondericiSube,
        teslimatSube: teslimatSube,
        desi: desi,
        agirlik: agirlik,
        durum: durum,
        verilisTarihi: verilisTarihi,
        tahminiTeslim: tahminiTeslim,
        teslimTarihi: teslimTarihi,
        plaka: plaka,
        surucu: surucu,
        takipNotu: takipNotu
    };

    return kargo;
}
// ===============================
// FORM KONTROLÜ
// ===============================

function formGecerliMi(kargo) {
    if (
        kargo.kargoNo === "" || kargo.gonderici === "" || kargo.alici === "")
        {
        alert("Kargo No, Gönderici ve Alıcı alanları boş bırakılamaz.");
        return false;
    }
    if (kargo.agirlik < 0) {
        alert("Ağırlık negatif olamaz.");
        return false;
    }
    if (
        kargo.verilisTarihi !== "" && kargo.tahminiTeslim !== "" && kargo.tahminiTeslim < kargo.verilisTarihi
    ) {
        alert("Tahmini teslim tarihi, veriliş tarihinden önce olamaz.");
        return false;
    }
    return true;
}
// ===============================
// FORMU TEMİZLE
// ===============================

function formuTemizle() {

    document.getElementById("kargoNo").value = "";
    document.getElementById("gonderici").value = "";
    document.getElementById("alici").value = "";
    document.getElementById("gondericiSube").value = "";
    document.getElementById("teslimatSube").value = "";
    document.getElementById("desi").value = "";
    document.getElementById("agirlik").value = "";
    document.getElementById("en").value = "";
    document.getElementById("boy").value = "";
    document.getElementById("yukseklik").value = "";
    document.getElementById("durum").value = "Bekliyor";
    document.getElementById("verilisTarihi").value = "";
    document.getElementById("tahminiTeslim").value = "";
    document.getElementById("teslimTarihi").value = "";
    document.getElementById("plaka").value = "";
    document.getElementById("surucu").value = "";
    document.getElementById("takipNotu").value = "";
}
// ===============================
// FORMU KARGO BİLGİLERİYLE DOLDUR
// ===============================

function formuDoldur(kargo) {

    document.getElementById("kargoNo").value = kargo.kargoNo || "";
    document.getElementById("gonderici").value = kargo.gonderici || "";
    document.getElementById("alici").value = kargo.alici || "";
    document.getElementById("gondericiSube").value = kargo.gondericiSube || "";
    document.getElementById("teslimatSube").value = kargo.teslimatSube || "";
    document.getElementById("desi").value = kargo.desi || "";
    document.getElementById("agirlik").value = kargo.agirlik || "";
    document.getElementById("durum").value = kargo.durum || "Bekliyor";
    document.getElementById("verilisTarihi").value = kargo.verilisTarihi || "";
    document.getElementById("tahminiTeslim").value = kargo.tahminiTeslim || "";
    document.getElementById("teslimTarihi").value = kargo.teslimTarihi || "";
    document.getElementById("plaka").value = kargo.plaka || "";
    document.getElementById("surucu").value = kargo.surucu || "";
    document.getElementById("takipNotu").value = kargo.takipNotu || "";
}
// ===============================
// DESİ HESAPLA
// ===============================

const desiHesaplaBtn = document.getElementById("desiHesaplaBtn");

if (desiHesaplaBtn) {

    desiHesaplaBtn.addEventListener("click", function () {

        const en = Number(document.getElementById("en").value);
        const boy = Number(document.getElementById("boy").value);
        const yukseklik = Number(document.getElementById("yukseklik").value);
        if (en <= 0 || boy <= 0 || yukseklik <= 0) {
            alert("En, boy ve yükseklik sıfırdan büyük olmalıdır.");
            return;
        }
        const desi = (en * boy * yukseklik) / 3000;

        document.getElementById("desi").value = desi.toFixed(2);
    });
}
// ===============================
// TEMİZLE BUTONU
// ===============================

const temizleBtn = document.getElementById("temizleBtn");

if (temizleBtn) {

    temizleBtn.addEventListener("click", function () {

        formuTemizle();

        alert("Form temizlendi.");
    });
}
// ===============================
// KARGO KAYDET ve POST
// ===============================
const kaydetBtn = document.getElementById("kaydetBtn");

if (kaydetBtn) {

    kaydetBtn.addEventListener("click", function () {

        const kargo = formBilgileriniAl();

        if (!formGecerliMi(kargo)) {
            return;
        }

        fetch("/api/kargo", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(kargo)
        })
            .then(function (response) {
                return response.text().then(function (mesaj) {
                    if (!response.ok) {
                        throw new Error(mesaj);
                    }
                    return mesaj;
                });
            })
            .then(function (mesaj) {
                alert(mesaj);
                formuTemizle();
                kargolariYukle();
            })
            .catch(function (hata) {
                console.error("Kaydetme hatası:", hata);
                alert(hata.message);
            });
    });
}
// ===============================
// KARGO ARA ve GET
// ===============================

const araBtn = document.getElementById("araBtn");

if (araBtn) {

    araBtn.addEventListener("click", function () {

        const arananKargoNo =
            document.getElementById("aramaKargoNo").value.trim();

        if (arananKargoNo === "") {
            alert("Aramak için kargo numarası giriniz.");
            return;
        }
        const adres =
            "/api/kargo?kargoNo=" +
            encodeURIComponent(arananKargoNo);

        fetch(adres, {

            method: "GET"

        })
            .then(function (response) {

                if (!response.ok) {

                    return response.text().then(function (mesaj) {
                        throw new Error(mesaj);
                    });
                }

                return response.json();
            })
            .then(function (kargo) {

                formuDoldur(kargo);

                alert("Kargo bulundu ve forma aktarıldı.");
            })
            .catch(function (hata) {

                console.error("Arama hatası:", hata);

                alert("Kargo bulunamadı.");
            });
    });
}


// ===============================
// KARGO GÜNCELLE ve PUT
// ===============================

const guncelleBtn = document.getElementById("guncelleBtn");

if (guncelleBtn) {

    guncelleBtn.addEventListener("click", function () {

        const kargo = formBilgileriniAl();

        if (!formGecerliMi(kargo)) {
            return;
        }

        fetch("/api/kargo", {

            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(kargo)

        })
            .then(function (response) {

                return response.text().then(function (mesaj) {

                    if (!response.ok) {
                        throw new Error(mesaj);
                    }

                    return mesaj;
                });
            })
            .then(function (mesaj) {

                alert(mesaj);

                formuTemizle();

                kargolariYukle();
            })
            .catch(function (hata) {

                console.error("Güncelleme hatası:", hata);

                alert(hata.message);
            });
    });
}

// ===============================
// KARGO SİL ve DELETE
// ===============================

const silBtn = document.getElementById("silBtn");

if (silBtn) {

    silBtn.addEventListener("click", function () {

        const kargoNo =document.getElementById("kargoNo").value.trim();

        if (kargoNo === "") {
            alert("Silmek için kargo numarası gereklidir.");
            return;
        }

        const silmeOnayi = confirm( kargoNo + " numaralı kargoyu silmek istediğine emin misin?");

        if (!silmeOnayi) {
            return;
        }

        const adres =
            "/api/kargo?kargoNo=" +
            encodeURIComponent(kargoNo);

        fetch(adres, {

            method: "DELETE"

        })
            .then(function (response) {

                return response.text().then(function (mesaj) {

                    if (!response.ok) {
                        throw new Error(mesaj);
                    }

                    return mesaj;
                });
            })
            .then(function (mesaj) {

                alert(mesaj);

                formuTemizle();

                kargolariYukle();
            })
            .catch(function (hata) {

                console.error("Silme hatası:", hata);

                alert(hata.message);
            });
    });
}

// =========================================
// VERİTABANINDAKİ KARGOLARI YÜKLE ve  GET
// =========================================
let aktifSayfa = 1;
const kayitSayisi = 5;
let toplamSayfa = 5;

function kargolariYukle() {

    fetch("/api/kargo", {

        method: "GET"

    })
        .then(function (response) {

            if (!response.ok) {
                throw new Error("Kargolar veritabanından alınamadı.");
            }

            return response.json();
        })
        .then(function (kargolar) {
         toplamSayfa = Math.ceil(kargolar.length / kayitSayisi);

            const tablo =
                document.getElementById("kargoTableBody");

            if (!tablo) {
                return;
            }

            tablo.innerHTML = "";

            const baslangic = (aktifSayfa - 1) * kayitSayisi;
            const bitis = baslangic + kayitSayisi;
            const gosterilecekKargolar = kargolar.slice(baslangic, bitis);

            gosterilecekKargolar.forEach(function (kargo) {

                const yeniSatir =
                    document.createElement("tr");

               yeniSatir.innerHTML = `
                   <td>
                       <a href="/kargo-detay?kargoNo=${encodeURIComponent(kargo.kargoNo)}">
                           ${kargo.kargoNo || ""}
                       </a>
                   </td>
                   <td>${kargo.gonderici || ""}</td>
                   <td>${kargo.alici || ""}</td>
                   <td>${kargo.gondericiSube || ""}</td>
                   <td>${kargo.teslimatSube || ""}</td>
                   <td>${kargo.desi || ""}</td>
                   <td>${kargo.agirlik || ""}</td>
                   <td>${kargo.durum || ""}</td>
                   <td>${kargo.verilisTarihi || ""}</td>
                   <td>${kargo.tahminiTeslim || ""}</td>
                   <td>${kargo.teslimTarihi || ""}</td>


               `;

                tablo.appendChild(yeniSatir);
            });
        })
        .catch(function (hata) {

            console.error("Kargolar yüklenemedi:", hata);
        });
}

// ===============================
// SAYFA AÇILINCA KARGOLARI YÜKLE
// ===============================

const kargoTableBody =
    document.getElementById("kargoTableBody");

if (kargoTableBody) {
    kargolariYukle();
}
// ===============================
// KARGO SAYFALAMA
// ===============================

const oncekiSayfaBtn = document.getElementById("oncekiSayfaBtn");
const sonrakiSayfaBtn = document.getElementById("sonrakiSayfaBtn");

if (oncekiSayfaBtn) {
    oncekiSayfaBtn.addEventListener("click", function () {

        if (aktifSayfa > 1) {
            aktifSayfa--;
            kargolariYukle();
        }

    });
}

if (sonrakiSayfaBtn) {
    sonrakiSayfaBtn.addEventListener("click", function () {



               if (aktifSayfa < toplamSayfa) {
                   aktifSayfa++;
                   kargolariYukle();
               }

           });
       }

// ===============================
// ÜRÜN BİLGİLERİNİ AL
// ===============================

function urunBilgileriniAl() {
    const urunKodu =document.getElementById("urunKodu").value.trim();
    const urunAdi = document.getElementById("urunAdi").value.trim();
    const kategori = document.getElementById("kategori").value.trim();
    const marka = document.getElementById("marka").value.trim();
    const tedarikci = document.getElementById("tedarikci").value.trim();
    const depo = document.getElementById("depo").value.trim();
    const rafNo = document.getElementById("rafNo").value.trim();
    const birim =document.getElementById("birim").value;
    const stokMiktari =Number(document.getElementById("stokMiktari").value);
    const kritikLimit =Number(document.getElementById("kritikLimit").value);
    const girisTarihi = document.getElementById("girisTarihi").value;

    const urun = {
        urunKodu: urunKodu,
        urunAdi: urunAdi,
        kategori: kategori,
        marka: marka,
        tedarikci: tedarikci,
        depo: depo,
        rafNo: rafNo,
        birim: birim,
        stokMiktari: stokMiktari,
        kritikLimit: kritikLimit,
        girisTarihi: girisTarihi
    };

    return urun;
}
// ===============================
// ÜRÜN FORM KONTROLÜ
// ===============================

function urunFormuGecerliMi(urun) {

    if (
        urun.urunKodu === "" ||
        urun.urunAdi === "" ||
        urun.kategori === ""
    ) {

        alert(
            "Ürün Kodu, Ürün Adı ve Kategori alanları boş bırakılamaz."
        );

        return false;
    }

    if (urun.stokMiktari < 0) {

        alert("Stok miktarı negatif olamaz.");

        return false;
    }

    if (urun.kritikLimit < 0) {

        alert("Kritik limit negatif olamaz.");

        return false;
    }

    return true;
}
// ===============================
// ÜRÜN FORMUNU TEMİZLE
// ===============================

function urunFormunuTemizle() {

    document.getElementById("urunKodu").value = "";
    document.getElementById("urunAdi").value = "";
    document.getElementById("kategori").value = "";
    document.getElementById("marka").value = "";
    document.getElementById("tedarikci").value = "";
    document.getElementById("depo").value = "";
    document.getElementById("rafNo").value = "";
    document.getElementById("birim").value = "Adet";
    document.getElementById("stokMiktari").value = "";
    document.getElementById("kritikLimit").value = "";
    document.getElementById("girisTarihi").value = "";
}
// ===============================
// FORMU ÜRÜN BİLGİLERİYLE DOLDUR
// ===============================

function urunFormunuDoldur(urun) {

    document.getElementById("urunKodu").value =
        urun.urunKodu || "";

    document.getElementById("urunAdi").value =
        urun.urunAdi || "";

    document.getElementById("kategori").value =
        urun.kategori || "";

    document.getElementById("marka").value =
        urun.marka || "";

    document.getElementById("tedarikci").value =
        urun.tedarikci || "";

    document.getElementById("depo").value =
        urun.depo || "";

    document.getElementById("rafNo").value =
        urun.rafNo || "";

    document.getElementById("birim").value =
        urun.birim || "Adet";

    document.getElementById("stokMiktari").value =
        urun.stokMiktari ?? "";

    document.getElementById("kritikLimit").value =
        urun.kritikLimit ?? "";

    document.getElementById("girisTarihi").value =
        urun.girisTarihi || "";
}
// ===============================
// ÜRÜN KAYDET ve  POST
// ===============================

const urunKaydetBtn =
    document.getElementById("urunKaydetBtn");

if (urunKaydetBtn) {

    urunKaydetBtn.addEventListener("click", function () {

        const urun = urunBilgileriniAl();

        if (!urunFormuGecerliMi(urun)) {
            return;
        }

        fetch("/api/urun", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(urun)

        })
            .then(function (response) {

                return response.text().then(function (mesaj) {

                    if (!response.ok) {
                        throw new Error(mesaj);
                    }

                    return mesaj;
                });
            })
            .then(function (mesaj) {

                alert(mesaj);

                urunFormunuTemizle();

                urunleriYukle();
            })
            .catch(function (hata) {

                console.error("Ürün kaydetme hatası:", hata);

                alert(hata.message);
            });
    });
}
// ===============================
// ÜRÜN ARA ve GET
// ===============================

const urunAraBtn =
    document.getElementById("urunAraBtn");

if (urunAraBtn) {

    urunAraBtn.addEventListener("click", function () {

        const urunKodu =
            document.getElementById("urunKoduAra").value.trim();

        if (urunKodu === "") {

            alert("Aramak için ürün kodu giriniz.");

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

                    return response.text().then(function (mesaj) {

                        throw new Error(mesaj);
                    });
                }
                return response.json();
            })
            .then(function (urun) {

                urunFormunuDoldur(urun);

                alert("Ürün bulundu ve forma aktarıldı.");
            })
            .catch(function (hata) {

                console.error("Ürün arama hatası:", hata);

                alert(hata.message);
            });
    });
}
// ===============================
// ÜRÜN GÜNCELLE ve PUT
// ===============================
const urunGuncelleBtn =
    document.getElementById("urunGuncelleBtn");
if (urunGuncelleBtn) {
    urunGuncelleBtn.addEventListener("click", function () {
        const urun = urunBilgileriniAl();
        if (!urunFormuGecerliMi(urun)) {
            return;
        }
        fetch("/api/urun", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(urun)
        })
            .then(function (response) {
                return response.text().then(function (mesaj) {
                    if (!response.ok) {
                        throw new Error(mesaj);
                    }
                    return mesaj;
                });
            })
            .then(function (mesaj) {
                alert(mesaj);
                urunFormunuTemizle();
                urunleriYukle();
            })
            .catch(function (hata) {
                console.error("Ürün güncelleme hatası:", hata);
                alert(hata.message);
            });
    });
}
// ===============================
// ÜRÜN SİL ve  DELETE
// ===============================
const urunSilBtn =
    document.getElementById("urunSilBtn");
if (urunSilBtn) {
    urunSilBtn.addEventListener("click", function () {
        const urunKodu =
            document.getElementById("urunKodu").value.trim();
        if (urunKodu === "") {
            alert("Silmek için ürün kodu giriniz.");
            return;
        }
        const silmeOnayi = confirm(
            urunKodu +
            " kodlu ürünü silmek istediğine emin misin?"
        );
        if (!silmeOnayi) {
            return;
        }
        const adres =
            "/api/urun?urunKodu=" +
            encodeURIComponent(urunKodu);
        fetch(adres, {
            method: "DELETE"
        })
            .then(function (response) {
                return response.text().then(function (mesaj) {
                    if (!response.ok) {
                        throw new Error(mesaj);
                    }
                    return mesaj;
                });
            })
            .then(function (mesaj) {

                alert(mesaj);

                urunFormunuTemizle();

                urunleriYukle();
            })
            .catch(function (hata) {
                console.error("Ürün silme hatası:", hata);
                alert(hata.message);
            });
    });
}
// ===============================
// ÜRÜN TEMİZLE
// ===============================

const urunTemizleBtn =
    document.getElementById("urunTemizleBtn");

if (urunTemizleBtn) {

    urunTemizleBtn.addEventListener("click", function () {

        urunFormunuTemizle();

        alert("Ürün formu temizlendi.");
    });
}
// ======================================
// VERİTABANINDAKİ ÜRÜNLERİ YÜKLE ve GET
// ======================================

function urunleriYukle() {

    fetch("/api/urun", {

        method: "GET"
    })
        .then(function (response) {

            if (!response.ok) {

                throw new Error(
                    "Ürünler veritabanından alınamadı."
                );
            }
            return response.json();
        })
        .then(function (urunler) {
            const tablo =
                document.getElementById("urunTableBody");

            if (!tablo) {
                return;
            }
            tablo.innerHTML = "";
            urunler.forEach(function (urun) {
                const durum =
                    urun.stokMiktari <= urun.kritikLimit
                        ? "🔴 Kritik Stok"
                        : "🟢 Stok Yeterli";
                const satir =
                    document.createElement("tr");
                satir.innerHTML = `
                    <td>${urun.urunKodu || ""}</td>
                    <td>${urun.urunAdi || ""}</td>
                    <td>${urun.kategori || ""}</td>
                    <td>${urun.marka || ""}</td>
                    <td>${urun.tedarikci || ""}</td>
                    <td>${urun.depo || ""}</td>
                    <td>${urun.rafNo || ""}</td>
                    <td>${urun.birim || ""}</td>
                    <td>${urun.stokMiktari ?? ""}</td>
                    <td>${urun.kritikLimit ?? ""}</td>
                    <td>${urun.girisTarihi || ""}</td>
                    <td>${durum}</td>
                    <td>-</td> `;
                tablo.appendChild(satir);
            });
        })
        .catch(function (hata) {

            console.error("Ürünler yüklenemedi:", hata);
        });
}
// ===============================
// SAYFA AÇILINCA ÜRÜNLERİ YÜKLE
// ===============================
const urunTableBody =
    document.getElementById("urunTableBody");

if (urunTableBody) {

    urunleriYukle();
}
// ===============================
// ULAŞIM BİLGİLERİNİ AL
// ===============================

function ulasimBilgileriniAl() {

    const plaka = document.getElementById("plaka").value.trim();

    const surucu = document.getElementById("surucu").value.trim();

    const baslangic = document.getElementById("baslangic").value.trim();

    const varis = document.getElementById("varis").value.trim();

    const rota = document.getElementById("rota").value.trim();

    const guncelKonum = document.getElementById("guncelKonum").value.trim();

    const baslangicZamani = document.getElementById("baslangicZamani").value;

    const tahminiSure = Number(document.getElementById("tahminiSure").value);

    const toplamMesafe = Number(document.getElementById("toplamMesafe").value);

    const yakit = Number(document.getElementById("yakit").value);

    const rotadanCikti = document.getElementById("rotadanCikti").value;

    const teslimAlindi = document.getElementById("teslimAlindi").value;

    const koliNo = document.getElementById("koliNo").value.trim();

    const teslimAlmaZamani = document.getElementById("teslimAlmaZamani").value;

    const teslimAlan = document.getElementById("teslimAlan").value.trim();

    const teslimAlinanFirma = document.getElementById("teslimAlinanFirma").value.trim();

    const teslimEdildi = document.getElementById("teslimEdildi").value;

    const teslimZamani = document.getElementById("teslimZamani").value;

    const musteri = document.getElementById("musteri").value.trim();

    const onayKodu = document.getElementById("onayKodu").value.trim();

    const rotaDurumu = document.getElementById("rotaDurumu").value;

    const aciklama = document.getElementById("aciklama").value.trim();


    const ulasim = {
        plaka: plaka,
        surucu: surucu,
        baslangic: baslangic,
        varis: varis,
        rota: rota,
        guncelKonum: guncelKonum,
        baslangicZamani: baslangicZamani,
        tahminiSure: tahminiSure,
        toplamMesafe: toplamMesafe,
        yakit: yakit,
        rotadanCikti: rotadanCikti,
        teslimAlindi: teslimAlindi,
        koliNo: koliNo,
        teslimAlmaZamani: teslimAlmaZamani,
        teslimAlan: teslimAlan,
        teslimAlinanFirma: teslimAlinanFirma,
        teslimEdildi: teslimEdildi,
        teslimZamani: teslimZamani,
        musteri: musteri,
        onayKodu: onayKodu,
        rotaDurumu: rotaDurumu,
        aciklama: aciklama
    };

    return ulasim;
}
// ===============================
// ULAŞIM FORM KONTROLÜ
// ===============================

function ulasimFormuGecerliMi(ulasim) {

    if (
        ulasim.plaka === "" ||
        ulasim.surucu === "" ||
        ulasim.baslangic === "" ||
        ulasim.varis === ""
    ) {

        alert(
            "Araç plakası, sürücü, başlangıç ve varış noktası boş bırakılamaz."
        );

        return false;
    }

    if (ulasim.tahminiSure < 0) {

        alert("Tahmini süre negatif olamaz.");

        return false;
    }

    if (ulasim.toplamMesafe < 0) {

        alert("Toplam mesafe negatif olamaz.");

        return false;
    }

    if (ulasim.yakit < 0) {

        alert("Yakıt miktarı negatif olamaz.");

        return false;
    }

    return true;
}
// ===============================
// ULAŞIM FORMUNU TEMİZLE
// ===============================

function ulasimFormunuTemizle() {

    document.getElementById("plaka").value = "";
    document.getElementById("surucu").value = "";
    document.getElementById("baslangic").value = "";
    document.getElementById("varis").value = "";
    document.getElementById("rota").value = "";
    document.getElementById("guncelKonum").value = "";
    document.getElementById("baslangicZamani").value = "";
    document.getElementById("tahminiSure").value = "";
    document.getElementById("toplamMesafe").value = "";
    document.getElementById("yakit").value = "";

    document.getElementById("rotadanCikti").value = "Hayır";
    document.getElementById("teslimAlindi").value = "Hayır";

    document.getElementById("koliNo").value = "";
    document.getElementById("teslimAlmaZamani").value = "";
    document.getElementById("teslimAlan").value = "";
    document.getElementById("teslimAlinanFirma").value = "";

    document.getElementById("teslimEdildi").value = "Hayır";
    document.getElementById("teslimZamani").value = "";
    document.getElementById("musteri").value = "";
    document.getElementById("onayKodu").value = "";

    document.getElementById("rotaDurumu").value = "Planlandı";
    document.getElementById("aciklama").value = "";

    document.getElementById("plakaAra").value = "";
}

// ===============================
// ULAŞIM FORMUNU DOLDUR
// ===============================

function ulasimFormunuDoldur(ulasim) {

    document.getElementById("plaka").value =
        ulasim.plaka || "";

    document.getElementById("surucu").value =
        ulasim.surucu || "";

    document.getElementById("baslangic").value =
        ulasim.baslangic || "";

    document.getElementById("varis").value =
        ulasim.varis || "";

    document.getElementById("rota").value =
        ulasim.rota || "";

    document.getElementById("guncelKonum").value =
        ulasim.guncelKonum || "";

    document.getElementById("baslangicZamani").value =
        ulasim.baslangicZamani || "";

    document.getElementById("tahminiSure").value =
        ulasim.tahminiSure ?? "";

    document.getElementById("toplamMesafe").value =
        ulasim.toplamMesafe ?? "";

    document.getElementById("yakit").value =
        ulasim.yakit ?? "";

    document.getElementById("rotadanCikti").value =
        ulasim.rotadanCikti || "Hayır";

    document.getElementById("teslimAlindi").value =
        ulasim.teslimAlindi || "Hayır";

    document.getElementById("koliNo").value =
        ulasim.koliNo || "";

    document.getElementById("teslimAlmaZamani").value =
        ulasim.teslimAlmaZamani || "";

    document.getElementById("teslimAlan").value =
        ulasim.teslimAlan || "";

    document.getElementById("teslimAlinanFirma").value =
        ulasim.teslimAlinanFirma || "";

    document.getElementById("teslimEdildi").value =
        ulasim.teslimEdildi || "Hayır";

    document.getElementById("teslimZamani").value =
        ulasim.teslimZamani || "";

    document.getElementById("musteri").value =
        ulasim.musteri || "";

    document.getElementById("onayKodu").value =
        ulasim.onayKodu || "";

    document.getElementById("rotaDurumu").value =
        ulasim.rotaDurumu || "Planlandı";

    document.getElementById("aciklama").value =
        ulasim.aciklama || "";
}
// ===============================
// ULAŞIM KAYDET
// POST
// ===============================

const ulasimKaydetBtn =
    document.getElementById("ulasimKaydetBtn");

if (ulasimKaydetBtn) {

    ulasimKaydetBtn.addEventListener("click", function () {

        const ulasim = ulasimBilgileriniAl();

        if (!ulasimFormuGecerliMi(ulasim)) {
            return;
        }

        fetch("/api/ulasim", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(ulasim)

        })
            .then(function (response) {

                return response.text().then(function (mesaj) {

                    if (!response.ok) {
                        throw new Error(mesaj);
                    }

                    return mesaj;
                });

            })
            .then(function (mesaj) {

                alert(mesaj);

                ulasimFormunuTemizle();

                ulasimlariYukle();

            })
            .catch(function (hata) {

                console.error("Ulaşım kaydetme hatası:", hata);

                alert(hata.message);

            });

    });

}
// ===============================
// ULAŞIM ARA
// GET
// ===============================

const ulasimAraBtn =
    document.getElementById("ulasimAraBtn");

if (ulasimAraBtn) {

    ulasimAraBtn.addEventListener("click", function () {

        const plaka =
            document.getElementById("plakaAra").value.trim();

        if (plaka === "") {

            alert("Aramak için araç plakası giriniz.");

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

                    return response.text().then(function (mesaj) {

                        throw new Error(mesaj);

                    });

                }

                return response.json();

            })
            .then(function (ulasim) {

                ulasimFormunuDoldur(ulasim);

                alert("Ulaşım kaydı bulundu ve forma aktarıldı.");

            })
            .catch(function (hata) {

                console.error("Ulaşım arama hatası:", hata);

                alert(hata.message);

            });

    });

}
// ===============================
// ULAŞIM GÜNCELLE
// PUT
// ===============================

const ulasimGuncelleBtn =
    document.getElementById("ulasimGuncelleBtn");

if (ulasimGuncelleBtn) {

    ulasimGuncelleBtn.addEventListener("click", function () {

        const ulasim = ulasimBilgileriniAl();

        if (!ulasimFormuGecerliMi(ulasim)) {
            return;
        }

        fetch("/api/ulasim", {

            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(ulasim)

        })
            .then(function (response) {

                return response.text().then(function (mesaj) {

                    if (!response.ok) {
                        throw new Error(mesaj);
                    }

                    return mesaj;

                });

            })
            .then(function (mesaj) {

                alert(mesaj);

                ulasimFormunuTemizle();

                ulasimlariYukle();

            })
            .catch(function (hata) {

                console.error("Ulaşım güncelleme hatası:", hata);

                alert(hata.message);

            });

    });

}
// ===============================
// ULAŞIM SİL
// DELETE
// ===============================

const ulasimSilBtn =
    document.getElementById("ulasimSilBtn");

if (ulasimSilBtn) {

    ulasimSilBtn.addEventListener("click", function () {

        const plaka =
            document.getElementById("plaka").value.trim();

        if (plaka === "") {

            alert("Silmek için araç plakası giriniz.");

            return;
        }

        const silmeOnayi = confirm(
            plaka +
            " plakalı ulaşım kaydını silmek istediğinize emin misiniz?"
        );

        if (!silmeOnayi) {
            return;
        }

        const adres =
            "/api/ulasim?plaka=" +
            encodeURIComponent(plaka);

        fetch(adres, {

            method: "DELETE"

        })
            .then(function (response) {

                return response.text().then(function (mesaj) {

                    if (!response.ok) {
                        throw new Error(mesaj);
                    }

                    return mesaj;
                });

            })
            .then(function (mesaj) {

                alert(mesaj);

                ulasimFormunuTemizle();

                ulasimlariYukle();

            })
            .catch(function (hata) {

                console.error("Ulaşım silme hatası:", hata);

                alert(hata.message);

            });

    });

}
// ===============================
// VERİTABANINDAKİ ULAŞIMLARI YÜKLE
// GET
// ===============================

function ulasimlariYukle() {

    fetch("/api/ulasim", {

        method: "GET"

    })
        .then(function (response) {

            if (!response.ok) {

                throw new Error(
                    "Ulaşım kayıtları veritabanından alınamadı."
                );

            }

            return response.json();

        })
        .then(function (ulasimlar) {

            const tablo =
                document.getElementById("ulasimTableBody");

            if (!tablo) {
                return;
            }

            tablo.innerHTML = "";

            ulasimlar.forEach(function (ulasim) {

                const satir =
                    document.createElement("tr");

                satir.innerHTML = `
                    <td>${ulasim.plaka || ""}</td>
                    <td>${ulasim.surucu || ""}</td>
                    <td>${ulasim.baslangic || ""}</td>
                    <td>${ulasim.varis || ""}</td>
                    <td>${ulasim.rota || ""}</td>
                    <td>${ulasim.guncelKonum || ""}</td>
                    <td>${ulasim.tahminiSure ?? ""}</td>
                    <td>${ulasim.toplamMesafe ?? ""}</td>
                    <td>${ulasim.yakit ?? ""}</td>
                    <td>${ulasim.rotadanCikti || ""}</td>
                    <td>${ulasim.koliNo || ""}</td>
                    <td>${ulasim.teslimAlindi || ""}</td>
                    <td>${ulasim.teslimEdildi || ""}</td>
                    <td>${ulasim.rotaDurumu || ""}</td>
                `;

                tablo.appendChild(satir);

            });

        })
        .catch(function (hata) {

            console.error(
                "Ulaşım kayıtları yüklenemedi:",
                hata
            );

        });

}
// ===============================
// SAYFA AÇILINCA ULAŞIMLARI YÜKLE
// ===============================

const ulasimTableBody =
    document.getElementById("ulasimTableBody");

if (ulasimTableBody) {

    ulasimlariYukle();

}
// ===============================
//        FAZLA MESAİ
// ===============================

// ===============================
// MESAİ SÜRESİNİ HESAPLA
// ===============================

const sureHesaplaBtn =
    document.getElementById("sureHesaplaBtn");

if (sureHesaplaBtn) {

    sureHesaplaBtn.addEventListener("click", function () {

        const baslangicSaati =document.getElementById("baslangicSaati").value;
        const bitisSaati = document.getElementById("bitisSaati").value;
        const molaSuresi = Number(document.getElementById("molaSuresi").value);

        if (
            baslangicSaati === "" ||bitisSaati === "")
         {
            alert("Başlangıç ve bitiş saatlerini giriniz.");
            return;
        }

        if (molaSuresi < 0) {
            alert("Mola süresi negatif olamaz.");
            return;
        }
        const baslangicParcalari = baslangicSaati.split(":");
        const bitisParcalari = bitisSaati.split(":");
        const baslangicDakika = Number(baslangicParcalari[0]) * 60 + Number(baslangicParcalari[1]);
         let bitisDakika =   Number(bitisParcalari[0]) * 60 + Number(bitisParcalari[1]);
        if (bitisDakika < baslangicDakika) {
            bitisDakika =bitisDakika + 24 * 60;
        }
        const toplamDakika = bitisDakika -  baslangicDakika -  molaSuresi;

        if (toplamDakika <= 0) {
           alert( "Mola çıkarıldıktan sonra mesai süresi sıfırdan büyük olmalıdır." );
            return;
        }
        const toplamSaat =toplamDakika / 60;
        document.getElementById("toplamMesaiSaati") .value = toplamSaat.toFixed(2);
    });

}
// ===============================
// MESAİ ÜCRETİNİ HESAPLA
// ===============================

const ucretHesaplaBtn = document.getElementById("ucretHesaplaBtn");

if (ucretHesaplaBtn) {

    ucretHesaplaBtn.addEventListener("click", function () {

        const aylikUcret = Number(document.getElementById("aylikUcret").value);
        const aylikCalismaSaati = Number(document.getElementById("aylikCalismaSaati").value);
        const katsayi = Number(document.getElementById("mesaiKatsayisi").value);
        const toplamMesaiSaati = Number(document.getElementById("toplamMesaiSaati").value);

        if (aylikUcret <= 0 || aylikCalismaSaati <= 0 || toplamMesaiSaati <= 0) {
            alert("Lütfen hesaplama için gerekli bilgileri giriniz.");
            return;
        }

        const normalSaatlikUcret = aylikUcret / aylikCalismaSaati;
        const mesaiSaatUcreti = normalSaatlikUcret * katsayi;
        const toplamMesaiUcreti = mesaiSaatUcreti * toplamMesaiSaati;

        document.getElementById("normalSaatlikUcret").value =normalSaatlikUcret.toFixed(2);

        document.getElementById("mesaiSaatUcreti").value = mesaiSaatUcreti.toFixed(2);

        document.getElementById("toplamMesaiUcreti").value =  toplamMesaiUcreti.toFixed(2);

    });

}
// ===============================
// MESAİ BİLGİLERİNİ AL
// ===============================

function mesaiBilgileriniAl() {
    const mesaiNo = document.getElementById("mesaiNo").value.trim();
    const sicilNo = document.getElementById("sicilNo").value.trim();
    const adSoyad = document.getElementById("adSoyad").value.trim();
    const departman = document.getElementById("departman").value;
    const pozisyon = document.getElementById("pozisyon").value.trim();
    const mesaiTarihi = document.getElementById("mesaiTarihi").value;
    const baslangicSaati = document.getElementById("baslangicSaati").value;
    const bitisSaati = document.getElementById("bitisSaati").value;
    const molaSuresi = Number(document.getElementById("molaSuresi").value);
    const toplamMesaiSaati = Number(document.getElementById("toplamMesaiSaati").value);
    const mesaiNedeni = document.getElementById("mesaiNedeni").value;
    const yapilanIs = document.getElementById("yapilanIs").value.trim();
    const projeOperasyon = document.getElementById("projeOperasyon").value.trim();
    const subeDepo = document.getElementById("subeDepo").value.trim();
    const aciklama = document.getElementById("aciklama").value.trim();
    const aylikUcret = Number(document.getElementById("aylikUcret").value);
    const aylikCalismaSaati = Number(document.getElementById("aylikCalismaSaati").value);
    const mesaiKatsayisi = Number(document.getElementById("mesaiKatsayisi").value);
    const normalSaatlikUcret = Number(document.getElementById("normalSaatlikUcret").value);
    const mesaiSaatUcreti = Number(document.getElementById("mesaiSaatUcreti").value);
    const toplamMesaiUcreti = Number(document.getElementById("toplamMesaiUcreti").value);
    const rotaPlaniVarMi = document.getElementById("rotaPlaniVarMi").value;
    const siraliTeslimat = document.getElementById("siraliTeslimat").value;
    const teslimatSorunu = document.getElementById("teslimatSorunu").value;
    const yerindeKapatma = document.getElementById("yerindeKapatma").value;
    const irsaliyeAtfNo = document.getElementById("irsaliyeAtfNo").value.trim();
    const yonetici = document.getElementById("yonetici").value.trim();
    const onayDurumu = document.getElementById("onayDurumu").value;
    const onayTarihi = document.getElementById("onayTarihi").value;
    const odemeDurumu = document.getElementById("odemeDurumu").value;
    const odemeTarihi = document.getElementById("odemeTarihi").value;

    return {
        mesaiNo,
        sicilNo,
        adSoyad,
        departman,
        pozisyon,
        mesaiTarihi,
        baslangicSaati,
        bitisSaati,
        molaSuresi,
        toplamMesaiSaati,
        mesaiNedeni,
        yapilanIs,
        projeOperasyon,
        subeDepo,
        aciklama,
        aylikUcret,
        aylikCalismaSaati,
        mesaiKatsayisi,
        normalSaatlikUcret,
        mesaiSaatUcreti,
        toplamMesaiUcreti,
        rotaPlaniVarMi,
        siraliTeslimat,
        teslimatSorunu,
        yerindeKapatma,
        irsaliyeAtfNo,
        yonetici,
        onayDurumu,
        onayTarihi,
        odemeDurumu,
        odemeTarihi
    };
}
// ===============================
// MESAİ FORM KONTROLÜ
// ===============================

function mesaiFormuGecerliMi(mesai) {
    if (
        mesai.mesaiNo === "" ||
        mesai.sicilNo === "" ||
        mesai.adSoyad === "" ||
        mesai.departman === "" ||
        mesai.mesaiTarihi === ""
    ) {
        alert("Mesai No, Sicil No, Ad Soyad, Departman ve Mesai Tarihi boş bırakılamaz.");
        return false;
    }

    if (mesai.molaSuresi < 0) {
        alert("Mola süresi negatif olamaz.");
        return false;
    }

    if (mesai.toplamMesaiSaati <= 0) {
        alert("Toplam mesai saati hesaplanmalıdır.");
        return false;
    }

    if (mesai.aylikUcret <= 0 || mesai.aylikCalismaSaati <= 0) {
        alert("Aylık ücret ve aylık çalışma saati sıfırdan büyük olmalıdır.");
        return false;
    }

    if (mesai.toplamMesaiUcreti <= 0) {
        alert("Fazla mesai ücreti hesaplanmalıdır.");
        return false;
    }

    return true;
}
 // ==================================
 // MESAİ FORMU TEMİZLE
 // ==================================

 function mesaiFormunuTemizle(){
     document.getElementById("mesaiNo").value = "";
     document.getElementById("sicilNo").value = "";
     document.getElementById("adSoyad").value = "";
     document.getElementById("departman").value = "";
     document.getElementById("pozisyon").value = "";
     document.getElementById("mesaiTarihi").value = "";
     document.getElementById("baslangicSaati").value = "";
     document.getElementById("bitisSaati").value = "";
     document.getElementById("molaSuresi").value = "";
     document.getElementById("toplamMesaiSaati").value = "";
     document.getElementById("mesaiNedeni").value = "";
     document.getElementById("yapilanIs").value = "";
     document.getElementById("projeOperasyon").value = "";
     document.getElementById("subeDepo").value = "";
     document.getElementById("aciklama").value = "";
     document.getElementById("aylikUcret").value = "";
     document.getElementById("aylikCalismaSaati").value = "225";
     document.getElementById("mesaiKatsayisi").value = "1.5";
     document.getElementById("normalSaatlikUcret").value = "";
     document.getElementById("mesaiSaatUcreti").value = "";
     document.getElementById("toplamMesaiUcreti").value = "";
     document.getElementById("rotaPlaniVarMi").value = "Hayır";
     document.getElementById("siraliTeslimat").value = "Hayır";
     document.getElementById("teslimatSorunu").value = "Hayır";
     document.getElementById("yerindeKapatma").value = "Hayır";
     document.getElementById("irsaliyeAtfNo").value = "";
     document.getElementById("yonetici").value = "";
     document.getElementById("onayDurumu").value = "Onay Bekliyor";
     document.getElementById("onayTarihi").value = "";
     document.getElementById("odemeDurumu").value = "Ödeme Bekliyor";
     document.getElementById("odemeTarihi").value = "";
     document.getElementById("mesaiNoAra").value = "";
 }
 // ===============================
 // MESAİ FORMUNU DOLDUR
 // ===============================

 function mesaiFormunuDoldur(mesai) {
     document.getElementById("mesaiNo").value = mesai.mesaiNo || "";
     document.getElementById("sicilNo").value = mesai.sicilNo || "";
     document.getElementById("adSoyad").value = mesai.adSoyad || "";
     document.getElementById("departman").value = mesai.departman || "";
     document.getElementById("pozisyon").value = mesai.pozisyon || "";
     document.getElementById("mesaiTarihi").value = mesai.mesaiTarihi || "";
     document.getElementById("baslangicSaati").value = mesai.baslangicSaati || "";
     document.getElementById("bitisSaati").value = mesai.bitisSaati || "";
     document.getElementById("molaSuresi").value = mesai.molaSuresi ?? "";
     document.getElementById("toplamMesaiSaati").value = mesai.toplamMesaiSaati ?? "";
     document.getElementById("mesaiNedeni").value = mesai.mesaiNedeni || "";
     document.getElementById("yapilanIs").value = mesai.yapilanIs || "";
     document.getElementById("projeOperasyon").value = mesai.projeOperasyon || "";
     document.getElementById("subeDepo").value = mesai.subeDepo || "";
     document.getElementById("aciklama").value = mesai.aciklama || "";
     document.getElementById("aylikUcret").value = mesai.aylikUcret ?? "";
     document.getElementById("aylikCalismaSaati").value = mesai.aylikCalismaSaati ?? "";
     document.getElementById("mesaiKatsayisi").value = mesai.mesaiKatsayisi ?? "1.5";
     document.getElementById("normalSaatlikUcret").value = mesai.normalSaatlikUcret ?? "";
     document.getElementById("mesaiSaatUcreti").value = mesai.mesaiSaatUcreti ?? "";
     document.getElementById("toplamMesaiUcreti").value = mesai.toplamMesaiUcreti ?? "";
     document.getElementById("rotaPlaniVarMi").value = mesai.rotaPlaniVarMi || "Hayır";
     document.getElementById("siraliTeslimat").value = mesai.siraliTeslimat || "Hayır";
     document.getElementById("teslimatSorunu").value = mesai.teslimatSorunu || "Hayır";
     document.getElementById("yerindeKapatma").value = mesai.yerindeKapatma || "Hayır";
     document.getElementById("irsaliyeAtfNo").value = mesai.irsaliyeAtfNo || "";
     document.getElementById("yonetici").value = mesai.yonetici || "";
     document.getElementById("onayDurumu").value = mesai.onayDurumu || "Onay Bekliyor";
     document.getElementById("onayTarihi").value = mesai.onayTarihi || "";
     document.getElementById("odemeDurumu").value = mesai.odemeDurumu || "Ödeme Bekliyor";
     document.getElementById("odemeTarihi").value = mesai.odemeTarihi || "";
 }
 // ===============================
 // MESAİ KAYDET
 // POST
 // ===============================

 const mesaiKaydetBtn = document.getElementById("mesaiKaydetBtn");

 if (mesaiKaydetBtn) {

     mesaiKaydetBtn.addEventListener("click", function () {

         const mesai = mesaiBilgileriniAl();

         if (!mesaiFormuGecerliMi(mesai)) {
             return;
         }

         fetch("/api/mesai", {

             method: "POST",

             headers: {
                 "Content-Type": "application/json"
             },

             body: JSON.stringify(mesai)

         })
         .then(function (response) {

             return response.text().then(function (mesaj) {

                 if (!response.ok) {
                     throw new Error(mesaj);
                 }

                 return mesaj;

             });

         })
         .then(function (mesaj) {

             alert(mesaj);

             mesaiFormunuTemizle();

             mesaileriYukle();

         })
         .catch(function (hata) {

             console.error("Mesai kaydetme hatası:", hata);

             alert(hata.message);

         });

     });

 }
 // ===============================
 // MESAİ ARA
 // GET
 // ===============================

 const mesaiAraBtn = document.getElementById("mesaiAraBtn");

 if (mesaiAraBtn) {

     mesaiAraBtn.addEventListener("click", function () {

         const mesaiNo = document.getElementById("mesaiNoAra").value.trim();

         if (mesaiNo === "") {

             alert("Aramak için mesai numarası giriniz.");

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

                 return response.text().then(function (mesaj) {

                     throw new Error(mesaj);

                 });

             }

             return response.json();

         })
         .then(function (mesai) {

             mesaiFormunuDoldur(mesai);

             alert("Mesai kaydı bulundu ve forma aktarıldı.");

         })
         .catch(function (hata) {

             console.error("Mesai arama hatası:", hata);

             alert(hata.message);

         });

     });

 }
 // ===============================
 // MESAİ GÜNCELLE
 // PUT
 // ===============================

 const mesaiGuncelleBtn = document.getElementById("mesaiGuncelleBtn");

 if (mesaiGuncelleBtn) {
     mesaiGuncelleBtn.addEventListener("click", function () {
         const mesai = mesaiBilgileriniAl();

         if (!mesaiFormuGecerliMi(mesai)) {
             return;
         }

         fetch("/api/mesai", {
             method: "PUT",
             headers: {
                 "Content-Type": "application/json"
             },
             body: JSON.stringify(mesai)
         })
         .then(function (response) {
             return response.text().then(function (mesaj) {
                 if (!response.ok) {
                     throw new Error(mesaj);
                 }
                 return mesaj;
             });
         })
         .then(function (mesaj) {
             alert(mesaj);
             mesaiFormunuTemizle();
             mesaileriYukle();
         })
         .catch(function (hata) {
             console.error("Mesai güncelleme hatası:", hata);
             alert(hata.message);
         });
     });
 }

 // ===============================
 // MESAİ SİL
 // DELETE
 // ===============================

 const mesaiSilBtn = document.getElementById("mesaiSilBtn");

 if (mesaiSilBtn) {
     mesaiSilBtn.addEventListener("click", function () {
         const mesaiNo = document.getElementById("mesaiNo").value.trim();

         if (mesaiNo === "") {
             alert("Silmek için mesai numarası giriniz.");
             return;
         }

         const silmeOnayi = confirm(
             mesaiNo + " numaralı mesai kaydını silmek istediğinize emin misiniz?"
         );

         if (!silmeOnayi) {
             return;
         }

         const adres =
             "/api/mesai?mesaiNo=" +
             encodeURIComponent(mesaiNo);

         fetch(adres, {
             method: "DELETE"
         })
         .then(function (response) {
             return response.text().then(function (mesaj) {
                 if (!response.ok) {
                     throw new Error(mesaj);
                 }
                 return mesaj;
             });
         })
         .then(function (mesaj) {
             alert(mesaj);
             mesaiFormunuTemizle();
             mesaileriYukle();
         })
         .catch(function (hata) {
             console.error("Mesai silme hatası:", hata);
             alert(hata.message);
         });
     });
 }

 // ===============================
 // MESAİ TEMİZLE
 // ===============================

 const mesaiTemizleBtn = document.getElementById("mesaiTemizleBtn");

 if (mesaiTemizleBtn) {
     mesaiTemizleBtn.addEventListener("click", function () {
         mesaiFormunuTemizle();
         alert("Mesai formu temizlendi.");
     });
 }

 // ===============================
 // VERİTABANINDAKİ MESAİLERİ YÜKLE
 // GET
 // ===============================

 function mesaileriYukle() {
     fetch("/api/mesai", {
         method: "GET"
     })
     .then(function (response) {
         if (!response.ok) {
             throw new Error("Mesai kayıtları veritabanından alınamadı.");
         }

         return response.json();
     })
     .then(function (mesailer) {
         const tablo = document.getElementById("mesaiTableBody");

         if (!tablo) {
             return;
         }

         tablo.innerHTML = "";

         mesailer.forEach(function (mesai) {
             const satir = document.createElement("tr");

             satir.innerHTML = `
                 <td>${mesai.mesaiNo || ""}</td>
                 <td>${mesai.sicilNo || ""}</td>
                 <td>${mesai.adSoyad || ""}</td>
                 <td>${mesai.departman || ""}</td>
                 <td>${mesai.mesaiTarihi || ""}</td>
                 <td>${mesai.baslangicSaati || ""}</td>
                 <td>${mesai.bitisSaati || ""}</td>
                 <td>${mesai.toplamMesaiSaati ?? ""}</td>
                 <td>${mesai.mesaiKatsayisi ?? ""}</td>
                 <td>${mesai.toplamMesaiUcreti ?? ""}</td>
                 <td>${mesai.onayDurumu || ""}</td>
                 <td>${mesai.odemeDurumu || ""}</td>
             `;

             tablo.appendChild(satir);
         });
     })
     .catch(function (hata) {
         console.error("Mesai kayıtları yüklenemedi:", hata);
     });
 }

 // ===============================
 // SAYFA AÇILINCA MESAİLERİ YÜKLE
 // ===============================

 const mesaiTableBody = document.getElementById("mesaiTableBody");

 if (mesaiTableBody) {
     mesaileriYukle();
 }
