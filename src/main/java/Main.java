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
        Database.createStokHareketTable();

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




        // =========================================
        // 3. HTML SAYFA YÖNLENDİRMELERİ
        // =========================================
        app.get("/", ctx -> htmlGonder(ctx, "/index.html"));
        app.get("/kargo", ctx -> htmlGonder(ctx, "/kargo.html"));
        app.get("/kargo-detay", ctx -> htmlGonder(ctx, "/kargo-detay.html"));
        app.get("/stok", ctx -> htmlGonder(ctx, "/stok.html"));
        app.get("/stok-detay", ctx -> htmlGonder(ctx, "/stok-detay.html"));
        app.get("/ulasim", ctx -> htmlGonder(ctx, "/ulasim.html"));
        app.get("/mesai", ctx -> htmlGonder(ctx, "/mesai.html"));

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
            Urun yeniUrun = gson.fromJson(ctx.body(), Urun.class);
            Urun eskiUrun = UrunDAO.findByUrunKodu(yeniUrun.getUrunKodu());

            if (eskiUrun == null) {
                ctx.status(404).result("Güncellenecek ürün bulunamadı.");
                return;
            }

            if (UrunDAO.update(yeniUrun)) {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                String islemTarihi = java.time.LocalDateTime.now().format(formatter);
                StokHareket hareket = new StokHareket();
                hareket.setUrunKodu(eskiUrun.getUrunKodu());
                hareket.setUrunAdi(eskiUrun.getUrunAdi());
                hareket.setKategori(eskiUrun.getKategori());
                hareket.setMarka(eskiUrun.getMarka());
                hareket.setTedarikci(eskiUrun.getTedarikci());
                hareket.setDepo(eskiUrun.getDepo());
                hareket.setRafNo(eskiUrun.getRafNo());
                hareket.setBirim(eskiUrun.getBirim());
                hareket.setStokMiktari(eskiUrun.getStokMiktari());
                hareket.setKritikLimit(eskiUrun.getKritikLimit());
                hareket.setGirisTarihi(eskiUrun.getGirisTarihi());
                hareket.setIslemTuru("GÜNCELLEME ÖNCESİ");
                hareket.setMiktar(Math.abs(yeniUrun.getStokMiktari() - eskiUrun.getStokMiktari()));
                hareket.setOncekiStok(eskiUrun.getStokMiktari());
                hareket.setYeniStok(yeniUrun.getStokMiktari());
                hareket.setIslemTarihi(islemTarihi);
                StokHareketDAO.hareketiKaydet(hareket);

                ctx.status(200).result("Ürün güncellendi.");
            } else {
                ctx.status(400).result("Ürün güncellenemedi.");
            }
        });

        app.delete("/api/urun", ctx -> {
            String urunKodu = ctx.queryParam("urunKodu");
            if (urunKodu == null) ctx.status(400).result("Ürün kodu gerekli.");
            else if (UrunDAO.delete(urunKodu)) ctx.status(200).result("Ürün silindi.");
            else ctx.status(404).result("Silinecek ürün bulunamadı.");
        });

        app.post("/api/stok-ekle", ctx -> {
            String urunKodu = ctx.queryParam("urunKodu");
            String miktarStr = ctx.queryParam("miktar");
            if (urunKodu != null && miktarStr != null) {
                if (UrunDAO.stokEkle(urunKodu, Integer.parseInt(miktarStr))) {
                    ctx.status(200).result("Stok başarıyla eklendi.");
                } else {
                    ctx.status(400).result("Stok eklenemedi.");
                }
            } else {
                ctx.status(400).result("Ürün kodu ve miktar gerekli.");
            }
        });

        app.post("/api/stok-cikar", ctx -> {
            String urunKodu = ctx.queryParam("urunKodu");
            String miktarStr = ctx.queryParam("miktar");
            if (urunKodu != null && miktarStr != null) {
                if (UrunDAO.stokCikar(urunKodu, Integer.parseInt(miktarStr))) {
                    ctx.status(200).result("Stok başarıyla çıkarıldı.");
                } else {
                    ctx.status(400).result("Stok çıkarılamadı.");
                }
            } else {
                ctx.status(400).result("Ürün kodu ve miktar gerekli.");
            }
        });

        app.get("/api/stok-gecmis", ctx -> {
            String urunKodu = ctx.queryParam("urunKodu");
            if (urunKodu == null || urunKodu.isBlank()) {
                ctx.status(400).result("Ürün kodu gerekli.");
            } else {
                jsonGonder(ctx, 200, StokHareketDAO.stokGecmisiniGetir(urunKodu));
            }
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