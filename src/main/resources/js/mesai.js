// ===============================
// FAZLA MESAİ İŞLEMLERİ
// ===============================

let currentPage = 1;
const recordsPerPage = 5;
let filteredData = [];

const sureHesaplaBtn = document.getElementById("sureHesaplaBtn");
if (sureHesaplaBtn) {
    sureHesaplaBtn.addEventListener("click", function () {
        const baslangicSaati = document.getElementById("baslangicSaati").value;
        const bitisSaati = document.getElementById("bitisSaati").value;
        const molaSuresi = Number(document.getElementById("molaSuresi").value);

        if (baslangicSaati === "" || bitisSaati === "") {
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
        let bitisDakika = Number(bitisParcalari[0]) * 60 + Number(bitisParcalari[1]);

        if (bitisDakika < baslangicDakika) {
            bitisDakika += 24 * 60;
        }
        const toplamDakika = bitisDakika - baslangicDakika - molaSuresi;
        if (toplamDakika <= 0) {
            alert("Mola çıkarıldıktan sonra mesai süresi sıfırdan büyük olmalıdır.");
            return;
        }
        document.getElementById("toplamMesaiSaati").value = (toplamDakika / 60).toFixed(2);
    });
}

const ucretHesaplaBtn = document.getElementById("ucretHesaplaBtn");
if (ucretHesaplaBtn) {
    ucretHesaplaBtn.addEventListener("click", function () {
        const aylikUcret = Number(document.getElementById("aylikUcret").value);
        const aylikCalismaSaati = Number(document.getElementById("aylikCalismaSaati").value);
        const katsayi = Number(document.getElementById("mesaiKatsayisi").value);
        const toplamMesaiSaati = Number(document.getElementById("toplamMesaiSaati").value);

        if (aylikUcret <= 0 || aylikCalismaSaati <= 0 || toplamMesaiSaati <= 0) {
            alert("Gireceğiniz değerler 0'ın altında olamaz!");
            return;
        }

        const normalSaatlikUcret = aylikUcret / aylikCalismaSaati;
        const mesaiSaatUcreti = normalSaatlikUcret * katsayi;
        const toplamMesaiUcreti = mesaiSaatUcreti * toplamMesaiSaati;

        document.getElementById("normalSaatlikUcret").value = normalSaatlikUcret.toFixed(2);
        document.getElementById("mesaiSaatUcreti").value = mesaiSaatUcreti.toFixed(2);
        document.getElementById("toplamMesaiUcreti").value = toplamMesaiUcreti.toFixed(2);
    });
}

function mesaiBilgileriniAl() {
    return {
        mesaiNo: document.getElementById("mesaiNo").value.trim(),
        sicilNo: document.getElementById("sicilNo").value.trim(),
        adSoyad: document.getElementById("adSoyad").value.trim(),
        departman: document.getElementById("departman").value,
        pozisyon: document.getElementById("pozisyon").value.trim(),
        mesaiTarihi: document.getElementById("mesaiTarihi").value,
        baslangicSaati: document.getElementById("baslangicSaati").value,
        bitisSaati: document.getElementById("bitisSaati").value,
        molaSuresi: Number(document.getElementById("molaSuresi").value),
        toplamMesaiSaati: Number(document.getElementById("toplamMesaiSaati").value),
        mesaiNedeni: document.getElementById("mesaiNedeni").value,
        yapilanIs: document.getElementById("yapilanIs").value.trim(),
        projeOperasyon: document.getElementById("projeOperasyon").value.trim(),
        subeDepo: document.getElementById("subeDepo").value.trim(),
        aciklama: document.getElementById("aciklama").value.trim(),
        aylikUcret: Number(document.getElementById("aylikUcret").value),
        aylikCalismaSaati: Number(document.getElementById("aylikCalismaSaati").value),
        mesaiKatsayisi: Number(document.getElementById("mesaiKatsayisi").value),
        normalSaatlikUcret: Number(document.getElementById("normalSaatlikUcret").value),
        mesaiSaatUcreti: Number(document.getElementById("mesaiSaatUcreti").value),
        toplamMesaiUcreti: Number(document.getElementById("toplamMesaiUcreti").value),
        rotaPlaniVarMi: document.getElementById("rotaPlaniVarMi").value,
        siraliTeslimat: document.getElementById("siraliTeslimat").value,
        teslimatSorunu: document.getElementById("teslimatSorunu").value,
        yerindeKapatma: document.getElementById("yerindeKapatma").value,
        irsaliyeAtfNo: document.getElementById("irsaliyeAtfNo").value.trim(),
        yonetici: document.getElementById("yonetici").value.trim(),
        onayDurumu: document.getElementById("onayDurumu").value,
        onayTarihi: document.getElementById("onayTarihi").value,
        odemeDurumu: document.getElementById("odemeDurumu").value,
        odemeTarihi: document.getElementById("odemeTarihi").value
    };
}

function mesaiFormuGecerliMi(mesai) {
    if (mesai.mesaiNo === "" || mesai.sicilNo === "" || mesai.adSoyad === "" || mesai.departman === "" || mesai.mesaiTarihi === "") {
        alert("Mesai No, Sicil No, Ad Soyad, Departman ve Mesai Tarihi boş bırakılamaz.");
        return false;
    }
    if (mesai.toplamMesaiSaati <= 0 || mesai.toplamMesaiUcreti <= 0) {
        alert("Toplam mesai saati ve ücreti hesaplanmalıdır.");
        return false;
    }
    return true;
}

function mesaiFormunuTemizle() {
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
    const mesaiNoAra = document.getElementById("mesaiNoAra");
    if (mesaiNoAra) mesaiNoAra.value = "";
}

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

// Mesai Butonları
const mesaiKaydetBtn = document.getElementById("mesaiKaydetBtn");
if (mesaiKaydetBtn) {
    mesaiKaydetBtn.addEventListener("click", function () {
        const mesai = mesaiBilgileriniAl();
        if (!mesaiFormuGecerliMi(mesai)) return;

        fetch("/api/mesai", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(mesai)
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); mesaiFormunuTemizle(); mesaileriYukle(); })
        .catch(err => alert(err.message));
    });
}

const mesaiAraBtn = document.getElementById("mesaiAraBtn");
if (mesaiAraBtn) {
    mesaiAraBtn.addEventListener("click", function () {
        const mesaiNo = document.getElementById("mesaiNoAra").value.trim();
        if (mesaiNo === "") {
            alert("Aramak için mesai numarası giriniz.");
            return;
        }
        fetch("/api/mesai?mesaiNo=" + encodeURIComponent(mesaiNo), {
            method: "GET",
            headers: { }
        })
        .then(res => { if (!res.ok) return res.text().then(m => { throw new Error(m); }); return res.json(); })
        .then(mesai => { mesaiFormunuDoldur(mesai); alert("Mesai kaydı bulundu ve forma aktarıldı."); })
        .catch(err => alert(err.message));
    });
}

const mesaiGuncelleBtn = document.getElementById("mesaiGuncelleBtn");
if (mesaiGuncelleBtn) {
    mesaiGuncelleBtn.addEventListener("click", function () {
        const mesai = mesaiBilgileriniAl();
        if (!mesaiFormuGecerliMi(mesai)) return;

        fetch("/api/mesai", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(mesai)
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); mesaiFormunuTemizle(); mesaileriYukle(); })
        .catch(err => alert(err.message));
    });
}

const mesaiSilBtn = document.getElementById("mesaiSilBtn");
if (mesaiSilBtn) {
    mesaiSilBtn.addEventListener("click", function () {
        const mesaiNo = document.getElementById("mesaiNo").value.trim();
        if (mesaiNo === "") {
            alert("Silmek için mesai numarası giriniz.");
            return;
        }
        if (!confirm(mesaiNo + " numaralı mesai kaydını silmek istediğinize emin misiniz?")) return;

        fetch("/api/mesai?mesaiNo=" + encodeURIComponent(mesaiNo), {
            method: "DELETE",
            headers: { }
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); mesaiFormunuTemizle(); mesaileriYukle(); })
        .catch(err => alert(err.message));
    });
}

const mesaiTemizleBtn = document.getElementById("mesaiTemizleBtn");
if (mesaiTemizleBtn) {
    mesaiTemizleBtn.addEventListener("click", function () {
        mesaiFormunuTemizle();
        alert("Mesai formu temizlendi.");
    });
}

function mesaileriYukle() {
    fetch("/api/mesai", {
        method: "GET",
        headers: { }
    })
    .then(res => { if (!res.ok) throw new Error("Mesai kayıtları veritabanından alınamadı."); return res.json(); })
    .then(mesailer => {
        filteredData = mesailer;
        currentPage = 1;
        renderTable();
    })
    .catch(err => console.error("Mesai kayıtları yüklenemedi:", err));
}

function renderTable() {
    const tablo = document.getElementById("mesaiTableBody");
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
    pageData.forEach(mesai => {
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
}

const mesaiTableBody = document.getElementById("mesaiTableBody");
if (mesaiTableBody) mesaileriYukle();

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