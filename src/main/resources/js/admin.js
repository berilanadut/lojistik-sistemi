// ===============================
// YÖNETİCİ ONAY PANELİ İŞLEMLERİ
// ===============================

window.onload = function() {
    const rol = localStorage.getItem('rol');
    if (rol !== 'ADMIN') {
        alert('Bu sayfaya erişim yetkiniz yok!');
        window.location.href = '/';
        return;
    }
    bekleyenleriGetir();
};

async function bekleyenleriGetir() {
    try {
        const response = await fetch('/api/admin/bekleyenler', {
            headers: { 'Authorization': localStorage.getItem('token') }
        });

        if (response.ok) {
            const liste = await response.json();
            const tbody = document.getElementById('bekleyenlerTablosu');
            tbody.innerHTML = '';

            if (liste.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:#64748B;">Onay bekleyen kullanıcı bulunmuyor.</td></tr>';
                return;
            }

            liste.forEach(k => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${k.kullanici_no}</td>
                    <td>${k.kullanici_adi}</td>
                    <td>${k.email}</td>
                    <td>${k.kullanici_rol}</td>
                    <td>
                        <button class="edit-btn" onclick="kullaniciyiOnayla(${k.kullanici_no})">Onayla</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch (err) {
        console.error('Bağlantı hatası:', err);
    }
}

async function kullaniciyiOnayla(kullaniciNo) {
    const mesajEl = document.getElementById('mesaj');
    mesajEl.innerText = '';

    try {
        const response = await fetch('/api/admin/onayla', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify({ kullanici_no: kullaniciNo })
        });

        const result = await response.json();

        if (response.ok) {
            mesajEl.style.color = '#10B981';
            mesajEl.innerText = result.mesaj;
            bekleyenleriGetir();
        } else {
            mesajEl.style.color = '#EF4444';
            mesajEl.innerText = result.hata || 'Onaylama başarısız.';
        }
    } catch (err) {
        mesajEl.style.color = '#EF4444';
        mesajEl.innerText = 'Sunucuyla bağlantı kurulamadı.';
    }
}