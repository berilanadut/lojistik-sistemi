function switchAuthTab(tab) {
        const tabs = document.querySelectorAll('.auth-tab');
        const loginSec = document.getElementById('loginSection');
        const regSec = document.getElementById('registerSection');
        document.getElementById('messageBox').innerText = '';

        if (tab === 'login') {
            tabs[0].classList.add('active');
            tabs[1].classList.remove('active');
            loginSec.classList.remove('hidden');
            regSec.classList.add('hidden');
        } else {
            tabs[1].classList.add('active');
            tabs[0].classList.remove('active');
            regSec.classList.remove('hidden');
            loginSec.classList.add('hidden');
        }
    }

    async function handleLogin(e) {
        e.preventDefault();
        const msg = document.getElementById('messageBox');
        msg.innerText = '';

        const data = {
            kullanici_adi: document.getElementById('loginKullaniciAdi').value,
            sifre: document.getElementById('loginSifre').value
        };

        try {
            const res = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            const result = await res.json();

            if (res.ok) {
                localStorage.setItem('token', result.token);
                localStorage.setItem('rol', result.rol);
                msg.className = 'message success';
                msg.innerText = 'Giriş başarılı! Ana menüye yönlendiriliyorsunuz...';
                setTimeout(() => window.location.href = '/', 1000);
            } else {
                msg.className = 'message error';
                msg.innerText = result.hata || 'Giriş başarısız.';
            }
        } catch (err) {
            msg.className = 'message error';
            msg.innerText = 'Sunucuyla bağlantı kurulamadı.';
        }
    }

    async function handleRegister(e) {
        e.preventDefault();
        const msg = document.getElementById('messageBox');
        msg.innerText = '';

        const data = {
            kullanici_adi: document.getElementById('regKullaniciAdi').value,
            email: document.getElementById('regEmail').value,
            sifre: document.getElementById('regSifre').value
        };

        try {
            const res = await fetch('/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            const result = await res.json();

            if (res.ok) {
                msg.className = 'message success';
                msg.innerText = result.mesaj;
                document.getElementById('registerSection').querySelector('form').reset();
            } else {
                msg.className = 'message error';
                msg.innerText = result.hata || 'Kayıt oluşturulamadı.';
            }
        } catch (err) {
            msg.className = 'message error';
            msg.innerText = 'Sunucuyla bağlantı kurulamadı.';
        }
    }