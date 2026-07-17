// ===============================
// KARGO İŞLEMLERİ
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

    return {
        kargoNo, gonderici, alici, gondericiSube, teslimatSube,
        desi, agirlik, durum, verilisTarihi, tahminiTeslim,
        teslimTarihi, plaka, surucu, takipNotu
    };
}

function formGecerliMi(kargo) {
    if (kargo.kargoNo === "" || kargo.gonderici === "" || kargo.alici === "") {
        alert("Kargo No, Gönderici ve Alıcı alanları boş bırakılamaz.");
        return false;
    }
    if (kargo.agirlik < 0) {
        alert("Ağırlık negatif olamaz.");
        return false;
    }
    if (kargo.verilisTarihi !== "" && kargo.tahminiTeslim !== "" && kargo.tahminiTeslim < kargo.verilisTarihi) {
        alert("Tahmini teslim tarihi, veriliş tarihinden önce olamaz.");
        return false;
    }
    return true;
}

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

// Buton Olayları ve API Entegrasyonları
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

const temizleBtn = document.getElementById("temizleBtn");
if (temizleBtn) {
    temizleBtn.addEventListener("click", function () {
        formuTemizle();
        alert("Form temizlendi.");
    });
}

const kaydetBtn = document.getElementById("kaydetBtn");
if (kaydetBtn) {
    kaydetBtn.addEventListener("click", function () {
        const kargo = formBilgileriniAl();
        if (!formGecerliMi(kargo)) return;

        fetch("/api/kargo", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(kargo)
        })
        .then(response => response.text().then(mesaj => {
            if (!response.ok) throw new Error(mesaj);
            return mesaj;
        }))
        .then(mesaj => {
            alert(mesaj);
            formuTemizle();
            kargolariYukle();
        })
        .catch(hata => alert(hata.message));
    });
}

const araBtn = document.getElementById("araBtn");
if (araBtn) {
    araBtn.addEventListener("click", function () {
        const arananKargoNo = document.getElementById("aramaKargoNo").value.trim();
        if (arananKargoNo === "") {
            alert("Aramak için kargo numarası giriniz.");
            return;
        }
        fetch("/api/kargo?kargoNo=" + encodeURIComponent(arananKargoNo), {
            method: "GET",
            headers: { "Authorization": localStorage.getItem("token") }
        })
        .then(response => {
            if (!response.ok) return response.text().then(m => { throw new Error(m); });
            return response.json();
        })
        .then(kargo => {
            formuDoldur(kargo);
            alert("Kargo bulundu ve forma aktarıldı.");
        })
        .catch(hata => alert("Kargo bulunamadı."));
    });
}

const guncelleBtn = document.getElementById("guncelleBtn");
if (guncelleBtn) {
    guncelleBtn.addEventListener("click", function () {
        const kargo = formBilgileriniAl();
        if (!formGecerliMi(kargo)) return;

        fetch("/api/kargo", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(kargo)
        })
        .then(response => response.text().then(mesaj => {
            if (!response.ok) throw new Error(mesaj);
            return mesaj;
        }))
        .then(mesaj => {
            alert(mesaj);
            formuTemizle();
            kargolariYukle();
        })
        .catch(hata => alert(hata.message));
    });
}

const silBtn = document.getElementById("silBtn");
if (silBtn) {
    silBtn.addEventListener("click", function () {
        const kargoNo = document.getElementById("kargoNo").value.trim();
        if (kargoNo === "") {
            alert("Silmek için kargo numarası gereklidir.");
            return;
        }
        if (!confirm(kargoNo + " numaralı kargoyu silmek istediğine emin misin?")) return;

        fetch("/api/kargo?kargoNo=" + encodeURIComponent(kargoNo), {
            method: "DELETE",
            headers: { "Authorization": localStorage.getItem("token") }
        })
        .then(response => response.text().then(mesaj => {
            if (!response.ok) throw new Error(mesaj);
            return mesaj;
        }))
        .then(mesaj => {
            alert(mesaj);
            formuTemizle();
            kargolariYukle();
        })
        .catch(hata => alert(hata.message));
    });
}

// Sayfalama ve Listeleme
let aktifSayfa = 1;
const kayitSayisi = 5;
let toplamSayfa = 5;

function kargolariYukle() {
    fetch("/api/kargo", {
        method: "GET",
        headers: { "Authorization": localStorage.getItem("token") }
    })
    .then(response => {
        if (!response.ok) throw new Error("Kargolar veritabanından alınamadı.");
        return response.json();
    })
    .then(kargolar => {
        toplamSayfa = Math.ceil(kargolar.length / kayitSayisi);
        const tablo = document.getElementById("kargoTableBody");
        if (!tablo) return;

        tablo.innerHTML = "";
        const baslangic = (aktifSayfa - 1) * kayitSayisi;
        const gosterilecekKargolar = kargolar.slice(baslangic, baslangic + kayitSayisi);

        gosterilecekKargolar.forEach(kargo => {
            const yeniSatir = document.createElement("tr");
            yeniSatir.innerHTML = `
                <td><a href="/kargo-detay?kargoNo=${encodeURIComponent(kargo.kargoNo)}">${kargo.kargoNo || ""}</a></td>
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
    .catch(hata => console.error("Kargolar yüklenemedi:", hata));
}

const kargoTableBody = document.getElementById("kargoTableBody");
if (kargoTableBody) kargolariYukle();

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