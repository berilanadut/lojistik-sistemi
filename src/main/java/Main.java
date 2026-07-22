import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {

        // =========================================
        // VERİTABANI TABLOLARINI HAZIRLA
        // =========================================

        Database.createCargoTable();
        Database.createUrunTable();
        Database.createUlasimTable();
        Database.createMesaiTable();
        Database.createCargoGecmisTable();
        Database.createStokHareketTable();
        Database.createUlasimGecmisTable();
        Database.createUlasimUrunTable();

        List<Cargo> kargolar = CargoDAO.findAll();
        List<Urun> urunler = UrunDAO.findAll();
        List<Ulasim> ulasimlar = UlasimDAO.findAll();
        List<Mesai> mesailer = MesaiDAO.findAll();

        System.out.println("Toplam kargo sayısı: " + kargolar.size());
        System.out.println("Toplam ürün sayısı: " + urunler.size());
        System.out.println("Toplam ulaşım kaydı: " + ulasimlar.size());
        System.out.println("Toplam mesai kaydı:" + mesailer.size());
        System.out.println("SQLite bağlantısı başarılı.");


        // =========================================
        // SUNUCUYU OLUŞTUR
        // =========================================

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );


        // =========================================
        // HTML, CSS VE JAVASCRIPT SAYFALARI
        // =========================================

        server.createContext("/", exchange ->
                dosyaGonder(
                        exchange,
                        "/index.html",
                        "text/html; charset=UTF-8"
                )
        );

        server.createContext("/style.css", exchange ->
                dosyaGonder(
                        exchange,
                        "/style.css",
                        "text/css; charset=UTF-8"
                )
        );

        server.createContext("/script.js", exchange ->
                dosyaGonder(
                        exchange,
                        "/script.js",
                        "application/javascript; charset=UTF-8"
                )
        );
        server.createContext("/kargo-detay.js", exchange ->
                dosyaGonder(
                        exchange,
                        "/kargo-detay.js",
                        "application/javascript; charset=UTF-8"
                )
        );
        server.createContext("/stok-detay.js", exchange ->
                dosyaGonder(
                        exchange,
                        "/stok-detay.js",
                        "application/javascript; charset=UTF-8"
                )
        );
        server.createContext("/ulasim-detay.js", exchange ->
                dosyaGonder(
                        exchange,
                        "/ulasim-detay.js",
                        "application/javascript; charset=UTF-8"
                )
        );

        server.createContext("/kargo", exchange ->
                dosyaGonder(
                        exchange,
                        "/kargo.html",
                        "text/html; charset=UTF-8"
                )
        );
        server.createContext("/kargo-detay", exchange ->
                dosyaGonder(
                        exchange,
                        "/kargo-detay.html",
                        "text/html; charset=UTF-8"
                )
        );

        server.createContext("/stok", exchange ->
                dosyaGonder(
                        exchange,
                        "/stok.html",
                        "text/html; charset=UTF-8"
                )
        );
        server.createContext("/stok-detay", exchange ->
                dosyaGonder(
                        exchange,
                        "/stok-detay.html",
                        "text/html; charset=UTF-8"
                )
        );

        server.createContext("/ulasim", exchange ->
                dosyaGonder(
                        exchange,
                        "/ulasim.html",
                        "text/html; charset=UTF-8"
                )
        );
        server.createContext("/ulasim-detay", exchange ->
                dosyaGonder(
                        exchange,
                        "/ulasim-detay.html",
                        "text/html; charset=UTF-8"
                )
        );
        server.createContext("/mesai", exchange ->
                dosyaGonder(
                        exchange,
                        "/mesai.html",
                        "text/html; charset=UTF-8"
                )
        );


        // =========================================
        // API ADRESLERİ
        // =========================================

        server.createContext(
                "/api/kargo",
                Main::kargoApi
        );
        server.createContext(
                "/api/kargo-gecmis",
                Main::kargoGecmisApi
        );

        server.createContext(
                "/api/urun",
                Main::urunApi
        );
        server.createContext(
                "/api/stok-ekle",
                Main::stokEkleApi
        );

        server.createContext(
                "/api/stok-cikar",
                Main::stokCikarApi
        );

        server.createContext(
                "/api/stok-gecmis",
                Main::stokGecmisApi
        );

        server.createContext(
                "/api/ulasim",
                Main::ulasimApi
        );
        server.createContext(
                "/api/ulasim-gecmis",
                Main::ulasimGecmisApi
        );
        server.createContext(
                "/api/ulasim-urun",
                Main::ulasimUrunApi
        );
        server.createContext(
                "/api/mesai",
                Main::mesaiApi
        );



        // =========================================
        // SUNUCUYU BAŞLAT
        // =========================================

        server.start();

        System.out.println(
                "Sunucu çalışıyor: http://localhost:8080"
        );
    }


    // =========================================
    // KARGO API
    // =========================================

    private static void kargoApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        String kargoNo =
                sorguDegeriniAl(
                        exchange,
                        "kargoNo"
                );


        // =========================================
        // POST: KARGO KAYDET
        // =========================================

        if ("POST".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Cargo cargo =
                    gson.fromJson(
                            gelenVeri,
                            Cargo.class
                    );

            boolean kaydedildi =
                    CargoDAO.save(cargo);

            if (kaydedildi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Kargo SQLite veritabanına kaydedildi."
                );

            } else {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Kargo kaydedilemedi. Kargo numarası daha önce kullanılmış olabilir."
                );
            }

            return;
        }


        // =========================================
        // GET: KARGO ARA VEYA HEPSİNİ GETİR
        // =========================================

        if ("GET".equals(method)) {

            if (
                    kargoNo != null &&
                            !kargoNo.isBlank()
            ) {

                Cargo cargo =
                        CargoDAO.findByKargoNo(
                                kargoNo
                        );

                if (cargo != null) {

                    cevapGonder(
                            exchange,
                            200,
                            "application/json; charset=UTF-8",
                            gson.toJson(cargo)
                    );

                } else {

                    cevapGonder(
                            exchange,
                            404,
                            "text/plain; charset=UTF-8",
                            "Kargo bulunamadı."
                    );
                }

            } else {

                List<Cargo> kargolar =
                        CargoDAO.findAll();

                cevapGonder(
                        exchange,
                        200,
                        "application/json; charset=UTF-8",
                        gson.toJson(kargolar)
                );
            }

            return;
        }


        // =========================================
        // PUT: KARGO GÜNCELLE
        // =========================================

        if ("PUT".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Cargo cargo =
                    gson.fromJson(
                            gelenVeri,
                            Cargo.class
                    );

            boolean guncellendi =
                    CargoDAO.update(cargo);

            if (guncellendi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Kargo bilgileri güncellendi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Güncellenecek kargo bulunamadı."
                );
            }

            return;
        }


        // =========================================
        // DELETE: KARGO SİL
        // =========================================

        if ("DELETE".equals(method)) {

            if (
                    kargoNo == null ||
                            kargoNo.isBlank()
            ) {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Silmek için kargo numarası gönderilmelidir."
                );

                return;
            }

            boolean silindi =
                    CargoDAO.delete(kargoNo);

            if (silindi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Kargo veritabanından silindi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Silinecek kargo bulunamadı."
                );
            }

            return;
        }


        cevapGonder(
                exchange,
                405,
                "text/plain; charset=UTF-8",
                "Bu istek türü desteklenmiyor."
        );
    }
    // =========================================
    // KARGO GEÇMİŞİ API
    // =========================================

    private static void kargoGecmisApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        if (!"GET".equals(method)) {

            cevapGonder(
                    exchange,
                    405,
                    "text/plain; charset=UTF-8",
                    "Bu istek türü desteklenmiyor."
            );

            return;
        }

        String kargoNo =
                sorguDegeriniAl(
                        exchange,
                        "kargoNo"
                );

        if (
                kargoNo == null ||
                        kargoNo.isBlank()
        ) {

            cevapGonder(
                    exchange,
                    400,
                    "text/plain; charset=UTF-8",
                    "Kargo numarası gönderilmelidir."
            );

            return;
        }

        CargoGecmisDAO cargoGecmisDAO =
                new CargoGecmisDAO();

        List<CargoGecmis> gecmisler =
                cargoGecmisDAO.kargoGecmisiniGetir(
                        kargoNo
                );

        cevapGonder(
                exchange,
                200,
                "application/json; charset=UTF-8",
                gson.toJson(gecmisler)
        );
    }


    // =========================================
    // ÜRÜN API
    // =========================================

    private static void urunApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        String urunKodu =
                sorguDegeriniAl(
                        exchange,
                        "urunKodu"
                );


        // =========================================
        // POST: ÜRÜN KAYDET
        // =========================================

        if ("POST".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Urun urun =
                    gson.fromJson(
                            gelenVeri,
                            Urun.class
                    );

            boolean kaydedildi =
                    UrunDAO.save(urun);

            if (kaydedildi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ürün SQLite veritabanına kaydedildi."
                );

            } else {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Ürün kaydedilemedi. Ürün kodu daha önce kullanılmış olabilir."
                );
            }

            return;
        }


        // =========================================
        // GET: ÜRÜN ARA VEYA HEPSİNİ GETİR
        // =========================================

        if ("GET".equals(method)) {

            if (
                    urunKodu != null &&
                            !urunKodu.isBlank()
            ) {

                Urun urun =
                        UrunDAO.findByUrunKodu(
                                urunKodu
                        );

                if (urun != null) {

                    cevapGonder(
                            exchange,
                            200,
                            "application/json; charset=UTF-8",
                            gson.toJson(urun)
                    );

                } else {

                    cevapGonder(
                            exchange,
                            404,
                            "text/plain; charset=UTF-8",
                            "Ürün bulunamadı."
                    );
                }

            } else {

                List<Urun> urunler =
                        UrunDAO.findAll();

                cevapGonder(
                        exchange,
                        200,
                        "application/json; charset=UTF-8",
                        gson.toJson(urunler)
                );
            }

            return;
        }


        // =========================================
        // PUT: ÜRÜN GÜNCELLE
        // =========================================

        if ("PUT".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Urun yeniUrun =
                    gson.fromJson(
                            gelenVeri,
                            Urun.class
                    );

            // Güncellemeden önce ürünün eski hâlini alıyoruz.
            Urun eskiUrun =
                    UrunDAO.findByUrunKodu(
                            yeniUrun.getUrunKodu()
                    );

            if (eskiUrun == null) {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Güncellenecek ürün bulunamadı."
                );

                return;
            }

            boolean guncellendi =
                    UrunDAO.update(yeniUrun);

            if (guncellendi) {

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "dd.MM.yyyy HH:mm:ss"
                        );

                String islemTarihi =
                        LocalDateTime.now().format(formatter);

                StokHareket hareket =
                        new StokHareket();

                // Güncellenmeden önceki ürün bilgileri
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

                hareket.setMiktar(
                        Math.abs(
                                yeniUrun.getStokMiktari()
                                        - eskiUrun.getStokMiktari()
                        )
                );

                hareket.setOncekiStok(
                        eskiUrun.getStokMiktari()
                );

                hareket.setYeniStok(
                        yeniUrun.getStokMiktari()
                );

                hareket.setIslemTarihi(islemTarihi);

                StokHareketDAO.hareketiKaydet(hareket);

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ürün bilgileri güncellendi ve eski bilgiler geçmişe kaydedildi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Ürün güncellenemedi."
                );
            }

            return;
        }


        // =========================================
        // DELETE: ÜRÜN SİL
        // =========================================

        if ("DELETE".equals(method)) {

            if (
                    urunKodu == null ||
                            urunKodu.isBlank()
            ) {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Silmek için ürün kodu gönderilmelidir."
                );

                return;
            }

            boolean silindi =
                    UrunDAO.delete(urunKodu);

            if (silindi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ürün veritabanından silindi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Silinecek ürün bulunamadı."
                );
            }

            return;
        }


        cevapGonder(
                exchange,
                405,
                "text/plain; charset=UTF-8",
                "Bu istek türü desteklenmiyor."
        );
    }

    // =========================================
    // STOK EKLE API
    // =========================================

    private static void stokEkleApi(HttpExchange exchange)
            throws IOException {

        if (!"POST".equals(exchange.getRequestMethod())) {

            cevapGonder(
                    exchange,
                    405,
                    "text/plain; charset=UTF-8",
                    "Bu istek türü desteklenmiyor."
            );

            return;
        }

        String urunKodu =
                sorguDegeriniAl(exchange, "urunKodu");

        String miktarStr =
                sorguDegeriniAl(exchange, "miktar");

        if (urunKodu == null || miktarStr == null) {

            cevapGonder(
                    exchange,
                    400,
                    "text/plain; charset=UTF-8",
                    "Ürün kodu ve miktar gönderilmelidir."
            );

            return;
        }

        int miktar = Integer.parseInt(miktarStr);

        boolean basarili =
                UrunDAO.stokEkle(
                        urunKodu,
                        miktar
                );

        if (basarili) {

            cevapGonder(
                    exchange,
                    200,
                    "text/plain; charset=UTF-8",
                    "Stok başarıyla eklendi."
            );

        } else {

            cevapGonder(
                    exchange,
                    400,
                    "text/plain; charset=UTF-8",
                    "Stok eklenemedi."
            );
        }
    }

// =========================================
// STOK ÇIKAR API
// =========================================

    private static void stokCikarApi(HttpExchange exchange)
            throws IOException {

        if (!"POST".equals(exchange.getRequestMethod())) {

            cevapGonder(
                    exchange,
                    405,
                    "text/plain; charset=UTF-8",
                    "Bu istek türü desteklenmiyor."
            );

            return;
        }

        String urunKodu =
                sorguDegeriniAl(exchange, "urunKodu");

        String miktarStr =
                sorguDegeriniAl(exchange, "miktar");

        if (urunKodu == null || miktarStr == null) {

            cevapGonder(
                    exchange,
                    400,
                    "text/plain; charset=UTF-8",
                    "Ürün kodu ve miktar gönderilmelidir."
            );

            return;
        }

        int miktar =
                Integer.parseInt(miktarStr);

        boolean basarili =
                UrunDAO.stokCikar(
                        urunKodu,
                        miktar
                );

        if (basarili) {

            cevapGonder(
                    exchange,
                    200,
                    "text/plain; charset=UTF-8",
                    "Stok başarıyla çıkarıldı."
            );

        } else {

            cevapGonder(
                    exchange,
                    400,
                    "text/plain; charset=UTF-8",
                    "Stok çıkarılamadı."
            );
        }
    }

// =========================================
// STOK GEÇMİŞİ API
// =========================================

    private static void stokGecmisApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        if (!"GET".equals(method)) {

            cevapGonder(
                    exchange,
                    405,
                    "text/plain; charset=UTF-8",
                    "Bu istek türü desteklenmiyor."
            );

            return;
        }

        String urunKodu =
                sorguDegeriniAl(
                        exchange,
                        "urunKodu"
                );

        if (
                urunKodu == null ||
                        urunKodu.isBlank()
        ) {

            cevapGonder(
                    exchange,
                    400,
                    "text/plain; charset=UTF-8",
                    "Ürün kodu gönderilmelidir."
            );

            return;
        }

        StokHareketDAO stokHareketDAO =
                new StokHareketDAO();

        List<StokHareket> hareketler =
                stokHareketDAO.stokGecmisiniGetir(
                        urunKodu
                );

        cevapGonder(
                exchange,
                200,
                "application/json; charset=UTF-8",
                gson.toJson(hareketler)
        );
    }


    // =========================================
    // ULAŞIM API
    // =========================================

    private static void ulasimApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        String plaka =
                sorguDegeriniAl(
                        exchange,
                        "plaka"
                );


        // =========================================
        // POST: ULAŞIM KAYDET
        // =========================================

        if ("POST".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Ulasim ulasim =
                    gson.fromJson(
                            gelenVeri,
                            Ulasim.class
                    );

            boolean kaydedildi =
                    UlasimDAO.save(ulasim);

            if (kaydedildi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ulaşım kaydı SQLite veritabanına kaydedildi."
                );

            } else {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Ulaşım kaydı oluşturulamadı. Bu plaka daha önce kullanılmış olabilir."
                );
            }

            return;
        }


        // =========================================
        // GET: ULAŞIM ARA VEYA HEPSİNİ GETİR
        // =========================================

        if ("GET".equals(method)) {

            if (
                    plaka != null &&
                            !plaka.isBlank()
            ) {

                Ulasim ulasim =
                        UlasimDAO.findByPlaka(
                                plaka
                        );

                if (ulasim != null) {

                    cevapGonder(
                            exchange,
                            200,
                            "application/json; charset=UTF-8",
                            gson.toJson(ulasim)
                    );

                } else {

                    cevapGonder(
                            exchange,
                            404,
                            "text/plain; charset=UTF-8",
                            "Ulaşım kaydı bulunamadı."
                    );
                }

            } else {

                List<Ulasim> ulasimlar =
                        UlasimDAO.findAll();

                cevapGonder(
                        exchange,
                        200,
                        "application/json; charset=UTF-8",
                        gson.toJson(ulasimlar)
                );
            }

            return;
        }


        // =========================================
        // PUT: ULAŞIM GÜNCELLE
        // =========================================

        if ("PUT".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Ulasim ulasim =
                    gson.fromJson(
                            gelenVeri,
                            Ulasim.class
                    );

            boolean guncellendi =
                    UlasimDAO.update(ulasim);

            if (guncellendi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ulaşım kaydı güncellendi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Güncellenecek ulaşım kaydı bulunamadı."
                );
            }

            return;
        }


        // =========================================
        // DELETE: ULAŞIM SİL
        // =========================================

        if ("DELETE".equals(method)) {

            if (
                    plaka == null ||
                            plaka.isBlank()
            ) {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Silmek için araç plakası gönderilmelidir."
                );

                return;
            }

            boolean silindi =
                    UlasimDAO.delete(plaka);

            if (silindi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ulaşım kaydı veritabanından silindi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Silinecek ulaşım kaydı bulunamadı."
                );
            }

            return;
        }


        cevapGonder(
                exchange,
                405,
                "text/plain; charset=UTF-8",
                "Bu istek türü desteklenmiyor."
        );
    }
    // =========================================
    // ULAŞIM GEÇMİŞİ API
    // =========================================

    private static void ulasimGecmisApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        if (!"GET".equals(method)) {

            cevapGonder(
                    exchange,
                    405,
                    "text/plain; charset=UTF-8",
                    "Bu istek türü desteklenmiyor."
            );

            return;
        }

        String plaka =
                sorguDegeriniAl(
                        exchange,
                        "plaka"
                );

        if (
                plaka == null ||
                        plaka.isBlank()
        ) {

            cevapGonder(
                    exchange,
                    400,
                    "text/plain; charset=UTF-8",
                    "Araç plakası gönderilmelidir."
            );

            return;
        }

        UlasimGecmisDAO ulasimGecmisDAO =
                new UlasimGecmisDAO();

        List<UlasimGecmis> gecmisler =
                ulasimGecmisDAO.ulasimGecmisiniGetir(
                        plaka
                );

        cevapGonder(
                exchange,
                200,
                "application/json; charset=UTF-8",
                gson.toJson(gecmisler)
        );
    }

    private static void ulasimUrunApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        String plaka =
                sorguDegeriniAl(
                        exchange,
                        "plaka"
                );
        String idStr =
                sorguDegeriniAl(
                        exchange,
                        "id"
                );

        // =========================================
        // POST: ARACA ÜRÜN EKLE
        // =========================================

        if ("POST".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            UlasimUrun urun =
                    gson.fromJson(
                            gelenVeri,
                            UlasimUrun.class
                    );

            UlasimUrunDAO dao =
                    new UlasimUrunDAO();

            try {

                dao.urunEkle(urun);

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ürün araca eklendi."
                );

            } catch (IllegalArgumentException e) {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        e.getMessage()
                );

            }

            return;
        }
        //==========================================
        //GET:PLAKAYA AİT ÜRÜNLERİ GİR
        //==========================================

        if ("GET".equals(method)) {

            if (
                    plaka == null ||
                            plaka.isBlank()
            ) {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Araç plakası gönderilmelidir."
                );

                return;
            }

            UlasimUrunDAO dao =
                    new UlasimUrunDAO();

            List<UlasimUrun> urunler =
                    dao.plakayaGoreUrunleriGetir(plaka);

            cevapGonder(
                    exchange,
                    200,
                    "application/json; charset=UTF-8",
                    gson.toJson(urunler)
            );

            return;
        }
        // =========================================
// PUT: ÜRÜN GÜNCELLE
// =========================================

        if ("PUT".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            UlasimUrun urun =
                    gson.fromJson(
                            gelenVeri,
                            UlasimUrun.class
                    );

            UlasimUrunDAO dao =
                    new UlasimUrunDAO();

            boolean guncellendi =
                    dao.urunGuncelle(urun);

            if (guncellendi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ürün güncellendi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Güncellenecek ürün bulunamadı."
                );

            }

            return;
        }
// =========================================
// DELETE: ARAÇTAN ÜRÜN SİL
// =========================================
        if ( "DELETE".equals(method)){
            if (
                    idStr == null || idStr.isBlank()
            ){
                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Silmek için ürün id değeri gönderilmelidir."

                );
                return;
            }
            int id;
            try{
                id = Integer.parseInt(idStr);

            }catch (NumberFormatException e){
                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Ürün id değeri sayı olmalıdır."
                );

                return;

            }
            UlasimUrunDAO dao = new UlasimUrunDAO();
            boolean silindi =
                    dao.urunSil(id);

            if (silindi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Ürün araçtan silindi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Silinecek ürün bulunamadı."
                );

            }

            return;
        }

        cevapGonder(
                exchange,
                405,
                "text/plain; charset=UTF-8",
                "Bu istek türü desteklenmiyor."
        );
    }


    // =========================================
    // MESAİ API
    // =========================================

    private static void mesaiApi(
            HttpExchange exchange
    ) throws IOException {

        String method =
                exchange.getRequestMethod();

        String mesaiNo =
                sorguDegeriniAl(
                        exchange,
                        "mesaiNo"
                );
        // =========================================
        // POST: MESAİ KAYDET
        // =========================================

        if ("POST".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Mesai mesai =
                    gson.fromJson(
                            gelenVeri,
                            Mesai.class
                    );

            boolean kaydedildi =
                    MesaiDAO.save(mesai);

            if (kaydedildi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Mesai kaydı oluşturuldu."
                );

            } else {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Mesai kaydı oluşturulamadı."
                );
            }

            return;
        }
        // =========================================
        // GET: MESAİ ARA VEYA HEPSİNİ GETİR
        // =========================================

        if ("GET".equals(method)) {

            if (
                    mesaiNo != null &&
                            !mesaiNo.isBlank()
            ) {

                Mesai mesai =
                        MesaiDAO.findByMesaiNo(
                                mesaiNo
                        );

                if (mesai != null) {

                    cevapGonder(
                            exchange,
                            200,
                            "application/json; charset=UTF-8",
                            gson.toJson(mesai)
                    );

                } else {

                    cevapGonder(
                            exchange,
                            404,
                            "text/plain; charset=UTF-8",
                            "Mesai kaydı bulunamadı."
                    );
                }

            } else {

                List<Mesai> mesailer =
                        MesaiDAO.findAll();

                cevapGonder(
                        exchange,
                        200,
                        "application/json; charset=UTF-8",
                        gson.toJson(mesailer)
                );
            }

            return;
        }
        // =========================================
        // PUT: MESAİ GÜNCELLE
        // =========================================

        if ("PUT".equals(method)) {

            String gelenVeri =
                    istekGovdesiniOku(exchange);

            Mesai mesai =
                    gson.fromJson(
                            gelenVeri,
                            Mesai.class
                    );

            boolean guncellendi =
                    MesaiDAO.update(mesai);

            if (guncellendi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Mesai kaydı güncellendi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Güncellenecek mesai kaydı bulunamadı."
                );
            }

            return;
        }
        // =========================================
        // DELETE: MESAİ SİL
        // =========================================

        if ("DELETE".equals(method)) {

            if (
                    mesaiNo == null ||
                            mesaiNo.isBlank()
            ) {

                cevapGonder(
                        exchange,
                        400,
                        "text/plain; charset=UTF-8",
                        "Silmek için mesai numarası gönderilmelidir."
                );

                return;
            }

            boolean silindi =
                    MesaiDAO.delete(mesaiNo);

            if (silindi) {

                cevapGonder(
                        exchange,
                        200,
                        "text/plain; charset=UTF-8",
                        "Mesai kaydı silindi."
                );

            } else {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Silinecek mesai kaydı bulunamadı."
                );
            }

            return;
        }

        cevapGonder(
                exchange,
                405,
                "text/plain; charset=UTF-8",
                "Bu istek türü desteklenmiyor."
        );
    }


    // =========================================
    // RESOURCE DOSYASINI TARAYICIYA GÖNDER
    // =========================================

    private static void dosyaGonder(
            HttpExchange exchange,
            String dosyaYolu,
            String contentType
    ) throws IOException {

        try (
                InputStream input =
                        Main.class.getResourceAsStream(
                                dosyaYolu
                        )
        ) {

            if (input == null) {

                cevapGonder(
                        exchange,
                        404,
                        "text/plain; charset=UTF-8",
                        "Dosya bulunamadı: " + dosyaYolu
                );

                return;
            }

            byte[] bytes =
                    input.readAllBytes();

            exchange
                    .getResponseHeaders()
                    .set(
                            "Content-Type",
                            contentType
                    );

            exchange.sendResponseHeaders(
                    200,
                    bytes.length
            );

            try (
                    OutputStream output =
                            exchange.getResponseBody()
            ) {

                output.write(bytes);
            }
        }
    }


    // =========================================
    // İSTEK BODY'SİNİ OKU
    // =========================================

    private static String istekGovdesiniOku(
            HttpExchange exchange
    ) throws IOException {

        return new String(
                exchange
                        .getRequestBody()
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
    }


    // =========================================
    // URL'DEN SORGU DEĞERİNİ AL
    // =========================================

    private static String sorguDegeriniAl(
            HttpExchange exchange,
            String arananAnahtar
    ) {

        String query =
                exchange
                        .getRequestURI()
                        .getRawQuery();

        if (
                query == null ||
                        query.isBlank()
        ) {

            return null;
        }

        String[] parcalar =
                query.split("&");

        for (String parca : parcalar) {

            String[] anahtarVeDeger =
                    parca.split("=", 2);

            if (
                    anahtarVeDeger.length == 2 &&
                            anahtarVeDeger[0].equals(
                                    arananAnahtar
                            )
            ) {

                return URLDecoder.decode(
                        anahtarVeDeger[1],
                        StandardCharsets.UTF_8
                );
            }
        }

        return null;
    }


    // =========================================
    // CEVABI TARAYICIYA GÖNDER
    // =========================================

    private static void cevapGonder(
            HttpExchange exchange,
            int durumKodu,
            String contentType,
            String cevap
    ) throws IOException {

        byte[] bytes =
                cevap.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange
                .getResponseHeaders()
                .set(
                        "Content-Type",
                        contentType
                );

        exchange.sendResponseHeaders(
                durumKodu,
                bytes.length
        );

        try (
                OutputStream output =
                        exchange.getResponseBody()
        ) {

            output.write(bytes);
        }
    }
}