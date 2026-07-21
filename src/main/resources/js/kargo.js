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
                "Content-Type": "application/json"
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
            headers: { }
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
                "Content-Type": "application/json"
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
            headers: { }
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
let currentPage = 1;
const recordsPerPage = 5;
let filteredData = [];

const durumFiltre = document.getElementById("durumFiltre");
if (durumFiltre) {
    durumFiltre.addEventListener("change", function () {
        currentPage = 1;
        kargolariYukle();
    });
}

function kargolariYukle() {
    fetch("/api/kargo", {
        method: "GET",
        headers: { }
    })
    .then(response => {
        if (!response.ok) throw new Error("Kargolar veritabanından alınamadı.");
        return response.json();
    })
    .then(kargolar => {
        const durumFiltre = document.getElementById("durumFiltre");
        if (durumFiltre && durumFiltre.value !== "Tümü") {
            const secilenDurum = durumFiltre.value;
            filteredData = kargolar.filter(kargo => kargo.durum === secilenDurum);
        } else {
            filteredData = kargolar;
        }
        currentPage = 1;
        renderTable();
    })
    .catch(hata => console.error("Kargolar yüklenemedi:", hata));
}

function renderTable() {
    const tablo = document.getElementById("kargoTableBody");
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
    pageData.forEach(kargo => {
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
}

const kargoTableBody = document.getElementById("kargoTableBody");
if (kargoTableBody) kargolariYukle();

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