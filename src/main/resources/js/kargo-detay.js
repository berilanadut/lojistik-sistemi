// ===============================
// URL'DEN KARGO NUMARASINI AL
// ===============================

const urlBilgileri = new URLSearchParams(window.location.search);
const kargoNo = urlBilgileri.get("kargoNo");

let currentPage = 1;
const recordsPerPage = 5;
let filteredData = [];
// KARGO DETAYINI GETİR
// ===============================

function kargoDetayiniGetir() {
    const detayAlani = document.getElementById("kargoDetay");

    if (!kargoNo) {
        detayAlani.innerHTML = "<p>Kargo numarası gönderilmedi.</p>";
        return;
    }

    const adres =
        "/api/kargo?kargoNo=" +
        encodeURIComponent(kargoNo);

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
            kargoDetayiniGoster(kargo);
        })
        .catch(function (hata) {
            console.error("Kargo detayı alınamadı:", hata);
            detayAlani.innerHTML =
                "<p>Kargo bilgileri alınamadı: " +
                hata.message +
                "</p>";
        });
}

// ===============================
// KARGO DETAYINI EKRANA YAZ
// ===============================

function kargoDetayiniGoster(kargo) {
    const detayAlani = document.getElementById("kargoDetay");

    detayAlani.innerHTML = `
        <div class="cargo-form">
            <h2>📋 Güncel Kargo Bilgileri</h2>

            <p><strong>Kargo No:</strong> ${kargo.kargoNo || ""}</p>
            <p><strong>Gönderici:</strong> ${kargo.gonderici || ""}</p>
            <p><strong>Alıcı:</strong> ${kargo.alici || ""}</p>
            <p><strong>Gönderici Şube:</strong> ${kargo.gondericiSube || ""}</p>
            <p><strong>Teslimat Şubesi:</strong> ${kargo.teslimatSube || ""}</p>
            <p><strong>Desi:</strong> ${kargo.desi ?? ""}</p>
            <p><strong>Ağırlık:</strong> ${kargo.agirlik ?? ""} kg</p>
            <p><strong>Durum:</strong> ${kargo.durum || ""}</p>
            <p><strong>Veriliş Tarihi:</strong> ${kargo.verilisTarihi || ""}</p>
            <p><strong>Tahmini Teslim:</strong> ${kargo.tahminiTeslim || ""}</p>
            <p><strong>Teslim Tarihi:</strong> ${kargo.teslimTarihi || ""}</p>
            <p><strong>Araç Plakası:</strong> ${kargo.plaka || ""}</p>
            <p><strong>Sürücü:</strong> ${kargo.surucu || ""}</p>
            <p><strong>Takip Notu:</strong> ${kargo.takipNotu || ""}</p>
        </div>

        <div class="back-button">
            <a href="/kargo">← Kargo Listesine Dön</a>
        </div>
    `;
}
// ===============================
// KARGO GEÇMİŞİNİ GETİR
// ===============================

function kargoGecmisiniGetir() {

    const tabloGovdesi =
        document.getElementById("gecmisTableBody");

    if (!kargoNo) {
        tabloGovdesi.innerHTML = `
            <tr>
                <td colspan="15">
                    Kargo numarası gönderilmedi.
                </td>
            </tr>
        `;
        return;
    }

    const adres =
        "/api/kargo-gecmis?kargoNo=" +
        encodeURIComponent(kargoNo);

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
    .then(function (gecmisler) {
        filteredData = gecmisler;
        currentPage = 1;
        renderTable();
    })
    .catch(function (hata) {
        console.error("Kargo geçmişi alınamadı:", hata);
        document.getElementById("gecmisTableBody").innerHTML = `
            <tr><td colspan="15">Kargo geçmişi alınamadı: ${hata.message}</td></tr>
        `;
    });
}

function renderTable() {
    const tablo = document.getElementById("gecmisTableBody");
    const pageInfo = document.getElementById("pageInfo");
    if (!tablo) return;

    if (filteredData.length === 0) {
        tablo.innerHTML = `<tr><td colspan="15">Bu kargoya ait geçmiş kayıt bulunamadı.</td></tr>`;
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
    pageData.forEach(gecmis => {
        const satir = document.createElement("tr");
        satir.innerHTML = `
            <td>${gecmis.degisiklikTarihi || ""}</td>
            <td>${gecmis.islemTuru || ""}</td>
            <td>${gecmis.gonderici || ""}</td>
            <td>${gecmis.alici || ""}</td>
            <td>${gecmis.gondericiSube || ""}</td>
            <td>${gecmis.teslimatSube || ""}</td>
            <td>${gecmis.desi ?? ""}</td>
            <td>${gecmis.agirlik ?? ""}</td>
            <td>${gecmis.durum || ""}</td>
            <td>${gecmis.verilisTarihi || ""}</td>
            <td>${gecmis.tahminiTeslim || ""}</td>
            <td>${gecmis.teslimTarihi || ""}</td>
            <td>${gecmis.plaka || ""}</td>
            <td>${gecmis.surucu || ""}</td>
            <td>${gecmis.takipNotu || ""}</td>
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

// ===============================
// SAYFA AÇILINCA DETAYI GETİR
// ===============================

kargoDetayiniGetir();
kargoGecmisiniGetir();