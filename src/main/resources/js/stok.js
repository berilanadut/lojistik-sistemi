// ===============================
// ÜRÜN / STOK İŞLEMLERİ
// ===============================

function urunBilgileriniAl() {
    return {
        urunKodu: document.getElementById("urunKodu").value.trim(),
        urunAdi: document.getElementById("urunAdi").value.trim(),
        kategori: document.getElementById("kategori").value.trim(),
        marka: document.getElementById("marka").value.trim(),
        tedarikci: document.getElementById("tedarikci").value.trim(),
        depo: document.getElementById("depo").value.trim(),
        rafNo: document.getElementById("rafNo").value.trim(),
        birim: document.getElementById("birim").value,
        stokMiktari: Number(document.getElementById("stokMiktari").value),
        kritikLimit: Number(document.getElementById("kritikLimit").value),
        girisTarihi: document.getElementById("girisTarihi").value
    };
}

function urunFormuGecerliMi(urun) {
    if (urun.urunKodu === "" || urun.urunAdi === "" || urun.kategori === "") {
        alert("Ürün Kodu, Ürün Adı ve Kategori alanları boş bırakılamaz.");
        return false;
    }
    if (urun.stokMiktari < 0 || urun.kritikLimit < 0) {
        alert("Stok miktarı ve kritik limit negatif olamaz.");
        return false;
    }
    return true;
}

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

function urunFormunuDoldur(urun) {
    document.getElementById("urunKodu").value = urun.urunKodu || "";
    document.getElementById("urunAdi").value = urun.urunAdi || "";
    document.getElementById("kategori").value = urun.kategori || "";
    document.getElementById("marka").value = urun.marka || "";
    document.getElementById("tedarikci").value = urun.tedarikci || "";
    document.getElementById("depo").value = urun.depo || "";
    document.getElementById("rafNo").value = urun.rafNo || "";
    document.getElementById("birim").value = urun.birim || "Adet";
    document.getElementById("stokMiktari").value = urun.stokMiktari ?? "";
    document.getElementById("kritikLimit").value = urun.kritikLimit ?? "";
    document.getElementById("girisTarihi").value = urun.girisTarihi || "";
}

// Ürün Butonları
const urunKaydetBtn = document.getElementById("urunKaydetBtn");
if (urunKaydetBtn) {
    urunKaydetBtn.addEventListener("click", function () {
        const urun = urunBilgileriniAl();
        if (!urunFormuGecerliMi(urun)) return;

        fetch("/api/urun", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(urun)
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); urunFormunuTemizle(); urunleriYukle(); })
        .catch(err => alert(err.message));
    });
}

const urunAraBtn = document.getElementById("urunAraBtn");
if (urunAraBtn) {
    urunAraBtn.addEventListener("click", function () {
        const urunKodu = document.getElementById("urunKoduAra").value.trim();
        if (urunKodu === "") {
            alert("Aramak için ürün kodu giriniz.");
            return;
        }
        fetch("/api/urun?urunKodu=" + encodeURIComponent(urunKodu), {
            method: "GET",
            headers: { "Authorization": localStorage.getItem("token") }
        })
        .then(res => { if (!res.ok) return res.text().then(m => { throw new Error(m); }); return res.json(); })
        .then(urun => { urunFormunuDoldur(urun); alert("Ürün bulundu ve forma aktarıldı."); })
        .catch(err => alert(err.message));
    });
}

const urunGuncelleBtn = document.getElementById("urunGuncelleBtn");
if (urunGuncelleBtn) {
    urunGuncelleBtn.addEventListener("click", function () {
        const urun = urunBilgileriniAl();
        if (!urunFormuGecerliMi(urun)) return;

        fetch("/api/urun", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(urun)
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); urunFormunuTemizle(); urunleriYukle(); })
        .catch(err => alert(err.message));
    });
}

const urunSilBtn = document.getElementById("urunSilBtn");
if (urunSilBtn) {
    urunSilBtn.addEventListener("click", function () {
        const urunKodu = document.getElementById("urunKodu").value.trim();
        if (urunKodu === "") {
            alert("Silmek için ürün kodu giriniz.");
            return;
        }
        if (!confirm(urunKodu + " kodlu ürünü silmek istediğine emin misin?")) return;

        fetch("/api/urun?urunKodu=" + encodeURIComponent(urunKodu), {
            method: "DELETE",
            headers: { "Authorization": localStorage.getItem("token") }
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); urunFormunuTemizle(); urunleriYukle(); })
        .catch(err => alert(err.message));
    });
}

const urunTemizleBtn = document.getElementById("urunTemizleBtn");
if (urunTemizleBtn) {
    urunTemizleBtn.addEventListener("click", function () {
        urunFormunuTemizle();
        alert("Ürün formu temizlendi.");
    });
}

function urunleriYukle() {
    fetch("/api/urun", {
        method: "GET",
        headers: { "Authorization": localStorage.getItem("token") }
    })
    .then(res => { if (!res.ok) throw new Error("Ürünler veritabanından alınamadı."); return res.json(); })
    .then(urunler => {
        const tablo = document.getElementById("urunTableBody");
        if (!tablo) return;
        tablo.innerHTML = "";
        urunler.forEach(urun => {
            const durum = urun.stokMiktari <= urun.kritikLimit ? "🔴 Kritik Stok" : "🟢 Stok Yeterli";
            const satir = document.createElement("tr");
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
                <td>-</td>
            `;
            tablo.appendChild(satir);
        });
    })
    .catch(err => console.error("Ürünler yüklenemedi:", err));
}

const urunTableBody = document.getElementById("urunTableBody");
if (urunTableBody) urunleriYukle();