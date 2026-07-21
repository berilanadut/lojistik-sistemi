import java.sql.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {

    private static final String URL = "jdbc:sqlite:cargo.db";
    private static final HikariDataSource dataSource;

    // Statik blok: Sistem ayağa kalktığında bir kez çalışır ve havuzu doldurur
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);

        // HikariCP Performans ve Güvenlik Ayarları
        config.setMaximumPoolSize(10); // Havuzda aynı anda bekleyecek maksimum bağlantı
        config.setMinimumIdle(2);      // Kimse kullanmasa bile en az 2 bağlantı hep sıcak kalsın
        config.setConnectionTimeout(30000); // 30 saniye içinde bağlantı alamazsa hata fırlat
        config.setPoolName("Lojistik-SQLite-Havuzu");

        dataSource = new HikariDataSource(config);
    }

    // Artık her istekte sıfırdan bağlantı kurulmuyor, havuzdan hazır 1 tane veriliyor
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void createCargoTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS cargo (
                    kargo_no TEXT PRIMARY KEY,
                    gonderici TEXT NOT NULL,
                    alici TEXT NOT NULL,
                    gonderici_sube TEXT,
                    teslimat_sube TEXT,
                    desi REAL,
                    agirlik REAL,
                    durum TEXT,
                    verilis_tarihi TEXT,
                    tahmini_teslim TEXT,
                    teslim_tarihi TEXT,
                    plaka TEXT,
                    surucu TEXT,
                    takip_notu TEXT
                )
                """;

        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
            System.out.println("Cargo tablosu hazır.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createUrunTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS urun(
                urun_kodu TEXT PRIMARY KEY,
                urun_adi TEXT NOT NULL,
                kategori TEXT NOT NULL,
                marka TEXT,
                tedarikci TEXT,
                depo TEXT,
                raf_no TEXT,
                birim TEXT,
                stok_miktari INTEGER NOT NULL,
                kritik_limit INTEGER NOT NULL,
                giris_tarihi TEXT
            )
            """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.executeUpdate();
            System.out.println("Ürün tablosu hazır.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createUlasimTable(){
        String sql = """
            CREATE TABLE IF NOT EXISTS ulasim (
                plaka TEXT PRIMARY KEY,
                surucu TEXT NOT NULL,
                baslangic TEXT NOT NULL,
                varis TEXT NOT NULL,
                rota TEXT,
                guncel_konum TEXT,
                baslangic_zamani TEXT,
                tahmini_sure INTEGER,
                toplam_mesafe REAL,
                yakit REAL,
                rotadan_cikti TEXT,
                teslim_alindi TEXT,
                koli_no TEXT,
                teslim_alma_zamani TEXT,
                teslim_alan TEXT,
                teslim_alinan_firma TEXT,
                teslim_edildi TEXT,
                teslim_zamani TEXT,
                musteri TEXT,
                onay_kodu TEXT,
                rota_durumu TEXT,
                aciklama TEXT
            )
            """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.executeUpdate();
            System.out.println("Ulaşım tablosu hazır.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // =========================================
    // MESAİ TABLOSU
    // =========================================
    public static void createMesaiTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS mesai(
            mesai_no TEXT PRIMARY KEY,
            sicil_no TEXT NOT NULL,
            ad_soyad TEXT NOT NULL,
            departman TEXT,
            pozisyon TEXT,
            mesai_tarihi TEXT,
            baslangic_saati TEXT,
            bitis_saati TEXT,
            mola_suresi INTEGER,
            toplam_mesai_saati REAL,
            mesai_nedeni TEXT,
            yapilan_is TEXT,
            proje_operasyon TEXT,
            sube_depo TEXT,
            aciklama TEXT,
            aylik_ucret REAL,
            aylik_calisma_saati INTEGER,
            mesai_katsayisi REAL,
            normal_saatlik_ucret REAL,
            mesai_saat_ucreti REAL,
            toplam_mesai_ucreti REAL,
            rota_plani_var_mi TEXT,
            sirali_teslimat TEXT,
            teslimat_sorunu TEXT,
            yerinde_kapatma TEXT,
            irsaliye_atf_no TEXT,
            yonetici TEXT,
            onay_durumu TEXT,
            onay_tarihi TEXT,
            odeme_durumu TEXT,
            odeme_tarihi TEXT
        )
        """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.executeUpdate();
            System.out.println("Mesai tablosu hazır.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createCargoGecmisTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS cargo_gecmis(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            kargo_no TEXT NOT NULL,
            gonderici TEXT NOT NULL,
            alici TEXT NOT NULL,
            gonderici_sube TEXT,
            teslimat_sube TEXT,
            desi REAL,
            agirlik REAL,
            durum TEXT,
            verilis_tarihi TEXT,
            tahmini_teslim TEXT,
            teslim_tarihi TEXT,
            plaka TEXT,
            surucu TEXT,
            takip_notu TEXT,
            islem_turu TEXT,
            degisiklik_tarihi TEXT
        )
        """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.executeUpdate();
            System.out.println("Cargo Geçmiş tablosu hazır.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createStokHareketTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS stok_hareket (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                urun_kodu TEXT NOT NULL,
                urun_adi TEXT,
                kategori TEXT,
                marka TEXT,
                tedarikci TEXT,
                depo TEXT,
                raf_no TEXT,
                birim TEXT,
                stok_miktari INTEGER,
                kritik_limit INTEGER,
                giris_tarihi TEXT,
                islem_turu TEXT NOT NULL,
                miktar INTEGER NOT NULL,
                onceki_stok INTEGER,
                yeni_stok INTEGER,
                islem_tarihi TEXT NOT NULL
            )
            """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.executeUpdate();
            System.out.println("Stok Hareket tablosu hazır.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}