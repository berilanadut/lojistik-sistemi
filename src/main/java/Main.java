import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // EKLENDİ: SQLException hatasını çözer

import java.io.InputStream;
import java.util.List;

public class Main {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {

        // =========================================
        // 1. VERİTABANI TABLOLARINI HAZIRLA
        // =========================================
        Database.createCargoTable();
        Database.createUrunTable();
        Database.createUlasimTable();
        Database.createMesaiTable();
        Database.createCargoGecmisTable();

        System.out.println("Toplam kargo sayısı: " + CargoDAO.findAll().size());
        System.out.println("Toplam ürün sayısı: " + UrunDAO.findAll().size());
        System.out.println("Toplam ulaşım kaydı: " + UlasimDAO.findAll().size());
        System.out.println("Toplam mesai kaydı:" + MesaiDAO.findAll().size());
        System.out.println("SQLite bağlantısı başarılı.");

        // =========================================
        // 2. SUNUCUYU OLUŞTUR VE BAŞLAT
        // =========================================
        Javalin app = Javalin.create(config -> {
            // Statik dosyaları (CSS, JS) otomatik olarak serve et
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/";
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(5000);

        System.out.println("Sunucu çalışıyor: http://localhost:5000");


        // ==========================================
        // GÜVENLİK FİLTRESİ (MIDDLEWARE)
        // ==========================================
        app.before("/api/*", ctx -> {
            String path = ctx.path();

            // 1. İstisna Rotalar: Login ve Register işlemlerinde token aranmaz
            if (path.equals("/api/login") || path.equals("/api/register")) {
                return; // Filtreyi atla ve işleme devam et
            }

            // 2. İstek başlığından (Header) token'ı al
            String token = ctx.header("Authorization");

            // Token hiç gönderilmemişse veya boşsa
            if (token == null || token.trim().isEmpty()) {
                ctx.status(401).json("{\"hata\": \"Yetkisiz erişim: Lütfen sisteme giriş yapın.\"}");
                throw new io.javalin.http.UnauthorizedResponse();
            }

            // Eğer frontend token'ı "Bearer UUID" formatında gönderirse "Bearer " kısmını temizle
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 3. Veritabanında Token Doğrulaması
            boolean yetkiliMi = false;

            // DÜZELTME: getConnection() yerine Database.getConnection() kullanıldı.
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT kullanici_no, kullanici_rol FROM kullanici WHERE aktif_token = ? AND onay_durumu = 'APPROVED'")) {

                // NOT: Yukarıdaki sorguda token kontrolünü 'aktif_token' sütununa göre yapıyoruz.
                stmt.setString(1, token);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Token geçerli ve kullanıcı onaylanmış.
                        yetkiliMi = true;

                        // Kullanıcı bilgilerini Javalin Context (ctx) içerisine gömüyoruz.
                        ctx.attribute("kullanici_no", rs.getInt("kullanici_no"));
                        ctx.attribute("kullanici_rol", rs.getString("kullanici_rol"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                ctx.status(500).json("{\"hata\": \"Veritabanı hatası oluştu.\"}");
                throw new io.javalin.http.InternalServerErrorResponse();
            }

            // 4. Yetkisiz İse İsteği Reddet
            if (!yetkiliMi) {
                ctx.status(401).json("{\"hata\": \"Geçersiz veya süresi dolmuş oturum. Tekrar giriş yapın.\"}");
                throw new io.javalin.http.UnauthorizedResponse();
            }
        });

        // =========================================
        // 3. HTML SAYFA YÖNLENDİRMELERİ
        // =========================================
        app.get("/", ctx -> htmlGonder(ctx, "/index.html"));
        app.get("/kargo", ctx -> htmlGonder(ctx, "/kargo.html"));
        app.get("/kargo-detay", ctx -> htmlGonder(ctx, "/kargo-detay.html"));
        app.get("/stok", ctx -> htmlGonder(ctx, "/stok.html"));
        app.get("/ulasim", ctx -> htmlGonder(ctx, "/ulasim.html"));
        app.get("/mesai", ctx -> htmlGonder(ctx, "/mesai.html"));
        app.get("/login", ctx -> htmlGonder(ctx, "/login.html"));
        app.get("/admin", ctx -> htmlGonder(ctx, "/admin.html"));

        // =========================================
        // 4. KARGO API
        // =========================================
        app.get("/api/kargo", ctx -> {
            String kargoNo = ctx.queryParam("kargoNo");
            if (kargoNo != null && !kargoNo.isBlank()) {
                Cargo cargo = CargoDAO.findByKargoNo(kargoNo);
                if (cargo != null) jsonGonder(ctx, 200, cargo);
                else ctx.status(404).result("Kargo bulunamadı.");
            } else {
                jsonGonder(ctx, 200, CargoDAO.findAll());
            }
        });

        app.post("/api/kargo", ctx -> {
            Cargo cargo = gson.fromJson(ctx.body(), Cargo.class);
            if (CargoDAO.save(cargo)) ctx.status(200).result("Kargo veritabanına kaydedildi.");
            else ctx.status(400).result("Kargo kaydedilemedi. Numara kullanılmış olabilir.");
        });

        app.put("/api/kargo", ctx -> {
            Cargo cargo = gson.fromJson(ctx.body(), Cargo.class);
            if (CargoDAO.update(cargo)) ctx.status(200).result("Kargo güncellendi.");
            else ctx.status(404).result("Güncellenecek kargo bulunamadı.");
        });

        app.delete("/api/kargo", ctx -> {
            String kargoNo = ctx.queryParam("kargoNo");
            if (kargoNo == null) ctx.status(400).result("Kargo numarası gönderilmelidir.");
            else if (CargoDAO.delete(kargoNo)) ctx.status(200).result("Kargo silindi.");
            else ctx.status(404).result("Silinecek kargo bulunamadı.");
        });

        // =========================================
        // 5. KARGO GEÇMİŞİ API
        // =========================================
        app.get("/api/kargo-gecmis", ctx -> {
            String kargoNo = ctx.queryParam("kargoNo");
            if (kargoNo == null || kargoNo.isBlank()) {
                ctx.status(400).result("Kargo numarası gönderilmelidir.");
            } else {
                CargoGecmisDAO dao = new CargoGecmisDAO();
                jsonGonder(ctx, 200, dao.kargoGecmisiniGetir(kargoNo));
            }
        });

        // =========================================
        // 6. ÜRÜN API
        // =========================================
        app.get("/api/urun", ctx -> {
            String urunKodu = ctx.queryParam("urunKodu");
            if (urunKodu != null && !urunKodu.isBlank()) {
                Urun urun = UrunDAO.findByUrunKodu(urunKodu);
                if (urun != null) jsonGonder(ctx, 200, urun);
                else ctx.status(404).result("Ürün bulunamadı.");
            } else {
                jsonGonder(ctx, 200, UrunDAO.findAll());
            }
        });

        app.post("/api/urun", ctx -> {
            Urun urun = gson.fromJson(ctx.body(), Urun.class);
            if (UrunDAO.save(urun)) ctx.status(200).result("Ürün kaydedildi.");
            else ctx.status(400).result("Ürün kaydedilemedi.");
        });

        app.put("/api/urun", ctx -> {
            Urun urun = gson.fromJson(ctx.body(), Urun.class);
            if (UrunDAO.update(urun)) ctx.status(200).result("Ürün güncellendi.");
            else ctx.status(404).result("Güncellenecek ürün bulunamadı.");
        });

        app.delete("/api/urun", ctx -> {
            String urunKodu = ctx.queryParam("urunKodu");
            if (urunKodu == null) ctx.status(400).result("Ürün kodu gerekli.");
            else if (UrunDAO.delete(urunKodu)) ctx.status(200).result("Ürün silindi.");
            else ctx.status(404).result("Silinecek ürün bulunamadı.");
        });

        // =========================================
        // 7. ULAŞIM API
        // =========================================
        app.get("/api/ulasim", ctx -> {
            String plaka = ctx.queryParam("plaka");
            if (plaka != null && !plaka.isBlank()) {
                Ulasim ulasim = UlasimDAO.findByPlaka(plaka);
                if (ulasim != null) jsonGonder(ctx, 200, ulasim);
                else ctx.status(404).result("Ulaşım kaydı bulunamadı.");
            } else {
                jsonGonder(ctx, 200, UlasimDAO.findAll());
            }
        });

        app.post("/api/ulasim", ctx -> {
            Ulasim ulasim = gson.fromJson(ctx.body(), Ulasim.class);
            if (UlasimDAO.save(ulasim)) ctx.status(200).result("Ulaşım kaydedildi.");
            else ctx.status(400).result("Kaydedilemedi. Plaka kullanılmış olabilir.");
        });

        app.post("/api/register", ctx -> {
            try {
                java.util.Map<String, String> kayitVerisi = ctx.bodyAsClass(java.util.Map.class);
                String kullaniciAdi = kayitVerisi.get("kullanici_adi");
                String eposta = kayitVerisi.get("email");
                String sifre = kayitVerisi.get("sifre");

                // Giriş doğrulama (validation) işlemleri
                if (kullaniciAdi == null || kullaniciAdi.trim().isEmpty() ||
                    eposta == null || eposta.trim().isEmpty() ||
                    sifre == null || sifre.trim().isEmpty()) {
                    ctx.status(400).json(java.util.Map.of("hata", "Tüm alanların doldurulması zorunludur!"));
                    return;
                }

                kullaniciAdi = kullaniciAdi.trim();
                eposta = eposta.trim();

                if (kullaniciAdi.length() < 3) {
                    ctx.status(400).json(java.util.Map.of("hata", "Kullanıcı adı en az 3 karakter olmalıdır!"));
                    return;
                }

                if (!eposta.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    ctx.status(400).json(java.util.Map.of("hata", "Geçersiz e-posta adresi formatı!"));
                    return;
                }

                if (sifre.length() < 6) {
                    ctx.status(400).json(java.util.Map.of("hata", "Şifre en az 6 karakter olmalıdır!"));
                    return;
                }

                // Şifreyi hash'leme işlemi
                String sifreHash = SifrelemeYardimcisi.sifreyiHashle(sifre, kullaniciAdi);

                String sql = "INSERT INTO kullanici (kullanici_adi, kullanici_rol, email, sifre, onay_durumu) VALUES (?, ?, ?, ?, 'PENDING')";

                try (Connection conn = Database.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, kullaniciAdi);
                    pstmt.setString(2, "PERSONEL"); // Varsayılan rol
                    pstmt.setString(3, eposta);
                    pstmt.setString(4, sifreHash);
                    pstmt.executeUpdate();

                    ctx.status(201).json(java.util.Map.of("mesaj", "Kayıt başarılı! Yönetici onayından sonra giriş yapabilirsiniz."));
                }
            } catch (Exception e) {
                ctx.status(400).json(java.util.Map.of("hata", "Kayıt oluşturulamadı (Bu kullanıcı adı veya e-posta zaten kullanımda olabilir)."));
            }
        });

        app.post("/api/login", ctx -> {
            try {
                java.util.Map<String, String> kimlikBilgileri = ctx.bodyAsClass(java.util.Map.class);
                String gelenKullaniciAdi = kimlikBilgileri.get("kullanici_adi");
                String gelenSifre = kimlikBilgileri.get("sifre");

                if (gelenKullaniciAdi == null || gelenKullaniciAdi.trim().isEmpty() ||
                    gelenSifre == null || gelenSifre.trim().isEmpty()) {
                    ctx.status(400).json(java.util.Map.of("hata", "Kullanıcı adı ve şifre girilmelidir!"));
                    return;
                }

                String sql = "SELECT kullanici_no, kullanici_rol, sifre FROM kullanici WHERE kullanici_adi = ? AND onay_durumu = 'APPROVED'";

                try (Connection conn = Database.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, gelenKullaniciAdi);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            int id = rs.getInt("kullanici_no");
                            String rol = rs.getString("kullanici_rol");
                            String kayitliSifre = rs.getString("sifre");

                            boolean sifreDogruMu = false;
                            boolean guncellemeGerekliMi = false;

                            // Eğer şifre 64 karakter uzunluğunda hex dizesiyse hash'lenmiştir
                            if (kayitliSifre != null && kayitliSifre.matches("^[0-9a-fA-F]{64}$")) {
                                sifreDogruMu = SifrelemeYardimcisi.sifreyiDogrula(gelenSifre, gelenKullaniciAdi, kayitliSifre);
                            } else {
                                // Eski düz metin şifre uyumluluğu
                                if (kayitliSifre != null && kayitliSifre.equals(gelenSifre)) {
                                    sifreDogruMu = true;
                                    guncellemeGerekliMi = true;
                                }
                            }

                            if (sifreDogruMu) {
                                // Eğer şifre eski tip düz metinse, veritabanını hash ile güncelle
                                if (guncellemeGerekliMi) {
                                    String yeniHash = SifrelemeYardimcisi.sifreyiHashle(gelenSifre, gelenKullaniciAdi);
                                    String guncellemeSql = "UPDATE kullanici SET sifre = ? WHERE kullanici_no = ?";
                                    try (PreparedStatement guncellemeStmt = conn.prepareStatement(guncellemeSql)) {
                                        guncellemeStmt.setString(1, yeniHash);
                                        guncellemeStmt.setInt(2, id);
                                        guncellemeStmt.executeUpdate();
                                    }
                                }

                                // Token veritabanına kaydedilirken "Bearer " kısmı OLMADAN, sadece ham UUID olarak kaydedilmelidir.
                                // Böylece middleware doğrulamasıyla tam uyumlu olur.
                                String hamToken = java.util.UUID.randomUUID().toString();
                                String yeniToken = "Bearer " + hamToken;

                                String updateSql = "UPDATE kullanici SET aktif_token = ? WHERE kullanici_no = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                    updateStmt.setString(1, hamToken); // DB'ye ham UUID yazılır
                                    updateStmt.setInt(2, id);
                                    updateStmt.executeUpdate();
                                }

                                java.util.Map<String, Object> yanit = new java.util.HashMap<>();
                                yanit.put("mesaj", "Giriş Başarılı");
                                yanit.put("token", yeniToken); // Arayüze standarda uygun Bearer token döndürülür
                                yanit.put("rol", rol);

                                ctx.status(200).json(yanit);
                            } else {
                                ctx.status(401).json(java.util.Map.of("hata", "Kullanıcı adı veya şifre hatalı!"));
                            }
                        } else {
                            ctx.status(401).json(java.util.Map.of("hata", "Kullanıcı adı veya şifre hatalı!"));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Login Hatası: " + e.getMessage());
                ctx.status(500).json(java.util.Map.of("hata", "Sunucu hatası: Lütfen bağlantıları kontrol edin."));
            }
        });

        app.post("/api/logout", ctx -> {
            try {
                // before filtresi kullanici_no bilgisini ctx'e eklemişti
                Integer kullaniciNo = ctx.attribute("kullanici_no");
                if (kullaniciNo != null) {
                    String sql = "UPDATE kullanici SET aktif_token = NULL WHERE kullanici_no = ?";
                    try (Connection baglanti = Database.getConnection();
                         PreparedStatement ifade = baglanti.prepareStatement(sql)) {
                        ifade.setInt(1, kullaniciNo);
                        ifade.executeUpdate();
                    }
                }
                ctx.status(200).json(java.util.Map.of("mesaj", "Oturum başarıyla sonlandırıldı."));
            } catch (Exception hata) {
                ctx.status(500).json(java.util.Map.of("hata", "Sunucu hatası: Oturum sonlandırılamadı."));
            }
        });

        app.put("/api/ulasim", ctx -> {
            Ulasim ulasim = gson.fromJson(ctx.body(), Ulasim.class);
            if (UlasimDAO.update(ulasim)) ctx.status(200).result("Ulaşım güncellendi.");
            else ctx.status(404).result("Kayıt bulunamadı.");
        });

        app.delete("/api/ulasim", ctx -> {
            String plaka = ctx.queryParam("plaka");
            if (plaka == null) ctx.status(400).result("Plaka gönderilmelidir.");
            else if (UlasimDAO.delete(plaka)) ctx.status(200).result("Kayıt silindi.");
            else ctx.status(404).result("Silinecek kayıt bulunamadı.");
        });

        // =========================================
        // 8. MESAİ API
        // =========================================
        app.get("/api/mesai", ctx -> {
            String mesaiNo = ctx.queryParam("mesaiNo");
            if (mesaiNo != null && !mesaiNo.isBlank()) {
                Mesai mesai = MesaiDAO.findByMesaiNo(mesaiNo);
                if (mesai != null) jsonGonder(ctx, 200, mesai);
                else ctx.status(404).result("Mesai kaydı bulunamadı.");
            } else {
                jsonGonder(ctx, 200, MesaiDAO.findAll());
            }
        });

        app.post("/api/mesai", ctx -> {
            Mesai mesai = gson.fromJson(ctx.body(), Mesai.class);

            String hataMesaji = mesai.verileriDogrula();
            if (hataMesaji != null) {
                ctx.status(400).result(hataMesaji);
                return;
            }

            if (MesaiDAO.save(mesai)) ctx.status(200).result("Mesai oluşturuldu.");
            else ctx.status(400).result("Mesai oluşturulamadı.");
        });

        app.put("/api/mesai", ctx -> {
            Mesai mesai = gson.fromJson(ctx.body(), Mesai.class);

            String hataMesaji = mesai.verileriDogrula();
            if (hataMesaji != null) {
                ctx.status(400).result(hataMesaji);
                return;
            }

            if (MesaiDAO.update(mesai)) ctx.status(200).result("Mesai güncellendi.");
            else ctx.status(404).result("Kayıt bulunamadı.");
        });

        app.delete("/api/mesai", ctx -> {
            String mesaiNo = ctx.queryParam("mesaiNo");
            if (mesaiNo == null) ctx.status(400).result("Mesai numarası gönderilmelidir.");
            else if (MesaiDAO.delete(mesaiNo)) ctx.status(200).result("Mesai silindi.");
            else ctx.status(404).result("Kayıt bulunamadı.");
        });


        // =========================================
        // 9. YÖNETİCİ (ADMIN) ONAY API ROTALARI
        // =========================================

        app.get("/api/admin/bekleyenler", ctx -> {
            // EKLENDİ: GÜVENLİK KONTROLÜ
            String rol = ctx.attribute("kullanici_rol");
            if (!"ADMIN".equals(rol)) {
                ctx.status(403).json(java.util.Map.of("hata", "Bu işlem için yönetici yetkisi gereklidir."));
                return;
            }

            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT kullanici_no, kullanici_adi, email, kullanici_rol FROM kullanici WHERE onay_durumu = 'PENDING'")) {

                try (ResultSet rs = pstmt.executeQuery()) {
                    java.util.List<java.util.Map<String, Object>> bekleyenler = new java.util.ArrayList<>();
                    while (rs.next()) {
                        java.util.Map<String, Object> k = new java.util.HashMap<>();
                        k.put("kullanici_no", rs.getInt("kullanici_no"));
                        k.put("kullanici_adi", rs.getString("kullanici_adi"));
                        k.put("email", rs.getString("email"));
                        k.put("kullanici_rol", rs.getString("kullanici_rol"));
                        bekleyenler.add(k);
                    }
                    ctx.status(200).json(bekleyenler);
                }
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("hata", "Bekleyen kullanıcılar getirilemedi."));
            }
        });

        app.post("/api/admin/onayla", ctx -> {
            // EKLENDİ: GÜVENLİK KONTROLÜ
            String rol = ctx.attribute("kullanici_rol");
            if (!"ADMIN".equals(rol)) {
                ctx.status(403).json(java.util.Map.of("hata", "Bu işlem için yönetici yetkisi gereklidir."));
                return;
            }

            try {
                java.util.Map<String, Object> data = ctx.bodyAsClass(java.util.Map.class);
                Object idObj = data.get("kullanici_no");
                int kullaniciNo = idObj instanceof Number ? ((Number) idObj).intValue() : Integer.parseInt(idObj.toString());

                String sql = "UPDATE kullanici SET onay_durumu = 'APPROVED' WHERE kullanici_no = ?";

                try (Connection conn = Database.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, kullaniciNo);
                    int etkilenen = pstmt.executeUpdate();

                    if (etkilenen > 0) {
                        ctx.status(200).json(java.util.Map.of("mesaj", "Kullanıcı başarıyla onaylandı!"));
                    } else {
                        ctx.status(404).json(java.util.Map.of("hata", "Kullanıcı bulunamadı."));
                    }
                }
            } catch (Exception e) {
                ctx.status(400).json(java.util.Map.of("hata", "Onaylama işlemi başarısız: " + e.getMessage()));
            }
        });
    }

    // =========================================
    // YARDIMCI METOTLAR
    // =========================================

    private static void jsonGonder(io.javalin.http.Context ctx, int status, Object data) {
        ctx.status(status)
                .contentType("application/json; charset=UTF-8")
                .result(gson.toJson(data));
    }

    private static void htmlGonder(io.javalin.http.Context ctx, String dosyaYolu) {
        try {
            InputStream is = Main.class.getResourceAsStream(dosyaYolu);
            if (is != null) {
                ctx.contentType("text/html; charset=UTF-8").result(is.readAllBytes());
            } else {
                ctx.status(404).result("Sayfa bulunamadı: " + dosyaYolu);
            }
        } catch (Exception e) {
            ctx.status(500).result("Sunucu hatası: Dosya okunamadı.");
        }
    }
}