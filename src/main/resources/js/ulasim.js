// ===============================
// ULAŞIM / ROTA İŞLEMLERİ
// ===============================

function ulasimBilgileriniAl() {
    return {
        plaka: document.getElementById("plaka").value.trim(),
        surucu: document.getElementById("surucu").value.trim(),
        baslangic: document.getElementById("baslangic").value.trim(),
        varis: document.getElementById("varis").value.trim(),
        rota: document.getElementById("rota").value.trim(),
        guncelKonum: document.getElementById("guncelKonum").value.trim(),
        baslangicZamani: document.getElementById("baslangicZamani").value,
        tahminiSure: Number(document.getElementById("tahminiSure").value),
        toplamMesafe: Number(document.getElementById("toplamMesafe").value),
        yakit: Number(document.getElementById("yakit").value),
        rotadanCikti: document.getElementById("rotadanCikti").value,
        teslimAlindi: document.getElementById("teslimAlindi").value,
        koliNo: document.getElementById("koliNo").value.trim(),
        teslimAlmaZamani: document.getElementById("teslimAlmaZamani").value,
        teslimAlan: document.getElementById("teslimAlan").value.trim(),
        teslimAlinanFirma: document.getElementById("teslimAlinanFirma").value.trim(),
        teslimEdildi: document.getElementById("teslimEdildi").value,
        teslimZamani: document.getElementById("teslimZamani").value,
        musteri: document.getElementById("musteri").value.trim(),
        onayKodu: document.getElementById("onayKodu").value.trim(),
        rotaDurumu: document.getElementById("rotaDurumu").value,
        aciklama: document.getElementById("aciklama").value.trim()
    };
}

function ulasimFormuGecerliMi(ulasim) {
    if (ulasim.plaka === "" || ulasim.surucu === "" || ulasim.baslangic === "" || ulasim.varis === "") {
        alert("Araç plakası, sürücü, başlangıç ve varış noktası boş bırakılamaz.");
        return false;
    }
    if (ulasim.tahminiSure < 0 || ulasim.toplamMesafe < 0 || ulasim.yakit < 0) {
        alert("Süre, mesafe veya yakıt miktarı negatif olamaz.");
        return false;
    }
    return true;
}

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
    const plakaAra = document.getElementById("plakaAra");
    if (plakaAra) plakaAra.value = "";
}

function ulasimFormunuDoldur(ulasim) {
    document.getElementById("plaka").value = ulasim.plaka || "";
    document.getElementById("surucu").value = ulasim.surucu || "";
    document.getElementById("baslangic").value = ulasim.baslangic || "";
    document.getElementById("varis").value = ulasim.varis || "";
    document.getElementById("rota").value = ulasim.rota || "";
    document.getElementById("guncelKonum").value = ulasim.guncelKonum || "";
    document.getElementById("baslangicZamani").value = ulasim.baslangicZamani || "";
    document.getElementById("tahminiSure").value = ulasim.tahminiSure ?? "";
    document.getElementById("toplamMesafe").value = ulasim.toplamMesafe ?? "";
    document.getElementById("yakit").value = ulasim.yakit ?? "";
    document.getElementById("rotadanCikti").value = ulasim.rotadanCikti || "Hayır";
    document.getElementById("teslimAlindi").value = ulasim.teslimAlindi || "Hayır";
    document.getElementById("koliNo").value = ulasim.koliNo || "";
    document.getElementById("teslimAlmaZamani").value = ulasim.teslimAlmaZamani || "";
    document.getElementById("teslimAlan").value = ulasim.teslimAlan || "";
    document.getElementById("teslimAlinanFirma").value = ulasim.teslimAlinanFirma || "";
    document.getElementById("teslimEdildi").value = ulasim.teslimEdildi || "Hayır";
    document.getElementById("teslimZamani").value = ulasim.teslimZamani || "";
    document.getElementById("musteri").value = ulasim.musteri || "";
    document.getElementById("onayKodu").value = ulasim.onayKodu || "";
    document.getElementById("rotaDurumu").value = ulasim.rotaDurumu || "Planlandı";
    document.getElementById("aciklama").value = ulasim.aciklama || "";
}

// Ulaşım Butonları
const ulasimKaydetBtn = document.getElementById("ulasimKaydetBtn");
if (ulasimKaydetBtn) {
    ulasimKaydetBtn.addEventListener("click", function () {
        const ulasim = ulasimBilgileriniAl();
        if (!ulasimFormuGecerliMi(ulasim)) return;

        fetch("/api/ulasim", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(ulasim)
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); ulasimFormunuTemizle(); ulasimlariYukle(); })
        .catch(err => alert(err.message));
    });
}

const ulasimAraBtn = document.getElementById("ulasimAraBtn");
if (ulasimAraBtn) {
    ulasimAraBtn.addEventListener("click", function () {
        const plaka = document.getElementById("plakaAra").value.trim();
        if (plaka === "") {
            alert("Aramak için araç plakası giriniz.");
            return;
        }
        fetch("/api/ulasim?plaka=" + encodeURIComponent(plaka), {
            method: "GET",
            headers: { "Authorization": localStorage.getItem("token") }
        })
        .then(res => { if (!res.ok) return res.text().then(m => { throw new Error(m); }); return res.json(); })
        .then(ulasim => { ulasimFormunuDoldur(ulasim); alert("Ulaşım kaydı bulundu ve forma aktarıldı."); })
        .catch(err => alert(err.message));
    });
}

const ulasimGuncelleBtn = document.getElementById("ulasimGuncelleBtn");
if (ulasimGuncelleBtn) {
    ulasimGuncelleBtn.addEventListener("click", function () {
        const ulasim = ulasimBilgileriniAl();
        if (!ulasimFormuGecerliMi(ulasim)) return;

        fetch("/api/ulasim", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(ulasim)
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); ulasimFormunuTemizle(); ulasimlariYukle(); })
        .catch(err => alert(err.message));
    });
}

const ulasimSilBtn = document.getElementById("ulasimSilBtn");
if (ulasimSilBtn) {
    ulasimSilBtn.addEventListener("click", function () {
        const plaka = document.getElementById("plaka").value.trim();
        if (plaka === "") {
            alert("Silmek için araç plakası giriniz.");
            return;
        }
        if (!confirm(plaka + " plakalı ulaşım kaydını silmek istediğinize emin misiniz?")) return;

        fetch("/api/ulasim?plaka=" + encodeURIComponent(plaka), {
            method: "DELETE",
            headers: { "Authorization": localStorage.getItem("token") }
        })
        .then(res => res.text().then(msg => { if (!res.ok) throw new Error(msg); return msg; }))
        .then(msg => { alert(msg); ulasimFormunuTemizle(); ulasimlariYukle(); })
        .catch(err => alert(err.message));
    });
}

function ulasimlariYukle() {
    fetch("/api/ulasim", {
        method: "GET",
        headers: { "Authorization": localStorage.getItem("token") }
    })
    .then(res => { if (!res.ok) throw new Error("Ulaşım kayıtları veritabanından alınamadı."); return res.json(); })
    .then(ulasimlar => {
        const tablo = document.getElementById("ulasimTableBody");
        if (!tablo) return;
        tablo.innerHTML = "";
        ulasimlar.forEach(ulasim => {
            const satir = document.createElement("tr");
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
    .catch(err => console.error("Ulaşım kayıtları yüklenemedi:", err));
}

const ulasimTableBody = document.getElementById("ulasimTableBody");
if (ulasimTableBody) ulasimlariYukle();