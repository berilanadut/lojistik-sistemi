// ===============================
// ÜRÜN / STOK İŞLEMLERİ
// ===============================

let currentPage = 1;
const recordsPerPage = 5;
let filteredData = [];

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
                "Content-Type": "application/json"
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
            headers: { }
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
                "Content-Type": "application/json"
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
            headers: { }
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
        headers: { }
    })
    .then(res => { if (!res.ok) throw new Error("Ürünler veritabanından alınamadı."); return res.json(); })
    .then(urunler => {
        filteredData = urunler;
        currentPage = 1;
        renderTable();
    })
    .catch(err => console.error("Ürünler yüklenemedi:", err));
}

function renderTable() {
    const tablo = document.getElementById("urunTableBody");
    const pageInfo = document.getElementById("pageInfo");
    if (!tablo) return;

    if (filteredData.length === 0) {
        tablo.innerHTML = `<tr><td colspan="15">Kayıt bulunamadı.</td></tr>`;
        if(pageInfo) pageInfo.textContent = "Sayfa 1 / 1";
        return;
    }

    const totalPages = Math.ceil(filteredData.length / recordsPerPage);
    if (currentPage > totalPages) currentPage = totalPages;
    if (currentPage < 1) currentPage = 1;

    if(pageInfo) pageInfo.textContent = "Sayfa " + currentPage + " / " + totalPages;

    const startIdx = (currentPage - 1) * recordsPerPage;
    const endIdx = startIdx + recordsPerPage;
    const pageData = filteredData.slice(startIdx, endIdx);

    tablo.innerHTML = "";
    pageData.forEach(urun => {
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
            <td>
                <button onclick="window.location.href='/stok-detay?urunKodu=${encodeURIComponent(urun.urunKodu)}'">
                    🔍 Detay
                </button>
                <button onclick="stokEkle('${urun.urunKodu}')">➕ Ekle</button>
                <button onclick="stokCikar('${urun.urunKodu}')">➖ Çıkar</button>
            </td>
        `;
        tablo.appendChild(satir);
    });
}

const prevBtn = document.getElementById("prevPageBtn");
if (prevBtn) prevBtn.addEventListener("click", () => { if (currentPage > 1) { currentPage--; renderTable(); } });
const nextBtn = document.getElementById("nextPageBtn");
if (nextBtn) nextBtn.addEventListener("click", () => { const totalPages = Math.ceil(filteredData.length / recordsPerPage); if (currentPage < totalPages) { currentPage++; renderTable(); } });
const jumpBtn = document.getElementById("jumpPageBtn");
if (jumpBtn) jumpBtn.addEventListener("click", () => {
    const jumpInput = document.getElementById("jumpPageInput").value;
    const page = parseInt(jumpInput, 10);
    const totalPages = Math.ceil(filteredData.length / recordsPerPage);
    if (!isNaN(page) && page >= 1 && page <= totalPages) { currentPage = page; renderTable(); }
    else { alert("Lütfen 1 ile " + totalPages + " arasında geçerli bir sayfa numarası giriniz."); }
});

const urunTableBody = document.getElementById("urunTableBody");
if (urunTableBody) urunleriYukle();

window.stokEkle = function(urunKodu) {
    const miktarStr = prompt("Eklenecek stok miktarını giriniz:");
    if (!miktarStr) return;
    const miktar = parseInt(miktarStr, 10);
    if (isNaN(miktar) || miktar <= 0) {
        alert("Lütfen geçerli pozitif bir sayı giriniz.");
        return;
    }
    fetch("/api/stok-ekle?urunKodu=" + encodeURIComponent(urunKodu) + "&miktar=" + miktar, {
        method: "POST"
    })
    .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
    .then(msg => {
        alert(msg);
        urunleriYukle();
    })
    .catch(err => alert("Hata: " + err.message));
};

window.stokCikar = function(urunKodu) {
    const miktarStr = prompt("Çıkarılacak stok miktarını giriniz:");
    if (!miktarStr) return;
    const miktar = parseInt(miktarStr, 10);
    if (isNaN(miktar) || miktar <= 0) {
        alert("Lütfen geçerli pozitif bir sayı giriniz.");
        return;
    }
    fetch("/api/stok-cikar?urunKodu=" + encodeURIComponent(urunKodu) + "&miktar=" + miktar, {
        method: "POST"
    })
    .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
    .then(msg => {
        alert(msg);
        urunleriYukle();
    })
    .catch(err => alert("Hata: " + err.message));
};