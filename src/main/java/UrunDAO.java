import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UrunDAO {

    // ===============================
    // ÜRÜN KAYDET
    // ===============================

    public static boolean save(Urun urun) {

        String sql = """
                INSERT INTO urun(
                    urun_kodu,
                    urun_adi,
                    kategori,
                    marka,
                    tedarikci,
                    depo,
                    raf_no,
                    birim,
                    stok_miktari,
                    kritik_limit,
                    giris_tarihi
                )
                VALUES(?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, urun.getUrunKodu());
            statement.setString(2, urun.getUrunAdi());
            statement.setString(3, urun.getKategori());
            statement.setString(4, urun.getMarka());
            statement.setString(5, urun.getTedarikci());
            statement.setString(6, urun.getDepo());
            statement.setString(7, urun.getRafNo());
            statement.setString(8, urun.getBirim());
            statement.setInt(9, urun.getStokMiktari());
            statement.setInt(10, urun.getKritikLimit());
            statement.setString(11, urun.getGirisTarihi());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===============================
    // BÜTÜN ÜRÜNLERİ GETİR
    // ===============================

    public static List<Urun> findAll() {

        String sql = "SELECT * FROM urun";

        List<Urun> urunler = new ArrayList<>();

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Urun urun = resultSetToUrun(resultSet);

                urunler.add(urun);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return urunler;
    }


    // ===============================
    // ÜRÜN KODUNA GÖRE ARA
    // ===============================

    public static Urun findByUrunKodu(String urunKodu) {

        String sql = """
                SELECT *
                FROM urun
                WHERE urun_kodu = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, urunKodu);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSetToUrun(resultSet);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // ===============================
    // ÜRÜN GÜNCELLE
    // ===============================

    public static boolean update(Urun urun) {

        String sql = """
                UPDATE urun
                SET
                    urun_adi = ?,
                    kategori = ?,
                    marka = ?,
                    tedarikci = ?,
                    depo = ?,
                    raf_no = ?,
                    birim = ?,
                    stok_miktari = ?,
                    kritik_limit = ?,
                    giris_tarihi = ?
                WHERE urun_kodu = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, urun.getUrunAdi());
            statement.setString(2, urun.getKategori());
            statement.setString(3, urun.getMarka());
            statement.setString(4, urun.getTedarikci());
            statement.setString(5, urun.getDepo());
            statement.setString(6, urun.getRafNo());
            statement.setString(7, urun.getBirim());
            statement.setInt(8, urun.getStokMiktari());
            statement.setInt(9, urun.getKritikLimit());
            statement.setString(10, urun.getGirisTarihi());
            statement.setString(11, urun.getUrunKodu());

            int degisenSatirSayisi =
                    statement.executeUpdate();

            return degisenSatirSayisi > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===============================
    // ÜRÜN SİL
    // ===============================

    public static boolean delete(String urunKodu) {

        String sql = """
                DELETE FROM urun
                WHERE urun_kodu = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, urunKodu);

            int silinenSatirSayisi =
                    statement.executeUpdate();

            return silinenSatirSayisi > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    // ===============================
    // STOK EKLE
    // ===============================

    public static boolean stokEkle(String urunKodu, int miktar) {

        Urun urun = findByUrunKodu(urunKodu);

        if (urun == null) {
            return false;
        }

        if (miktar <= 0) {
            return false;
        }

        int oncekiStok = urun.getStokMiktari();
        int yeniStok = oncekiStok + miktar;

        urun.setStokMiktari(yeniStok);

        if (!update(urun)) {
            return false;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String islemTarihi =
                LocalDateTime.now().format(formatter);

        StokHareket hareket = new StokHareket();

        hareket.setUrunKodu(urun.getUrunKodu());
        hareket.setUrunAdi(urun.getUrunAdi());
        hareket.setKategori(urun.getKategori());
        hareket.setMarka(urun.getMarka());
        hareket.setTedarikci(urun.getTedarikci());
        hareket.setDepo(urun.getDepo());
        hareket.setRafNo(urun.getRafNo());
        hareket.setBirim(urun.getBirim());
        hareket.setStokMiktari(yeniStok);
        hareket.setKritikLimit(urun.getKritikLimit());
        hareket.setGirisTarihi(urun.getGirisTarihi());

        hareket.setIslemTuru("EKLEME");
        hareket.setMiktar(miktar);
        hareket.setOncekiStok(oncekiStok);
        hareket.setYeniStok(yeniStok);
        hareket.setIslemTarihi(islemTarihi);

        StokHareketDAO.hareketiKaydet(hareket);

        return true;
    }

    // ===============================
    // STOK ÇIKAR
    // ===============================

    public static boolean stokCikar(String urunKodu, int miktar) {

        Urun urun = findByUrunKodu(urunKodu);

        if (urun == null) {
            return false;
        }

        if (miktar <= 0) {
            return false;
        }

        int oncekiStok = urun.getStokMiktari();

        if (miktar > oncekiStok) {
            return false;
        }

        int yeniStok = oncekiStok - miktar;

        urun.setStokMiktari(yeniStok);

        if (!update(urun)) {
            return false;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String islemTarihi =
                LocalDateTime.now().format(formatter);

        StokHareket hareket = new StokHareket();

        hareket.setUrunKodu(urun.getUrunKodu());
        hareket.setUrunAdi(urun.getUrunAdi());
        hareket.setKategori(urun.getKategori());
        hareket.setMarka(urun.getMarka());
        hareket.setTedarikci(urun.getTedarikci());
        hareket.setDepo(urun.getDepo());
        hareket.setRafNo(urun.getRafNo());
        hareket.setBirim(urun.getBirim());
        hareket.setStokMiktari(yeniStok);
        hareket.setKritikLimit(urun.getKritikLimit());
        hareket.setGirisTarihi(urun.getGirisTarihi());

        hareket.setIslemTuru("ÇIKARMA");
        hareket.setMiktar(miktar);
        hareket.setOncekiStok(oncekiStok);
        hareket.setYeniStok(yeniStok);
        hareket.setIslemTarihi(islemTarihi);

        StokHareketDAO.hareketiKaydet(hareket);

        return true;
    }


    // ===============================
    // RESULTSET → URUN
    // ===============================

    private static Urun resultSetToUrun(
            ResultSet resultSet
    ) throws SQLException {

        return new Urun(
                resultSet.getString("urun_kodu"),
                resultSet.getString("urun_adi"),
                resultSet.getString("kategori"),
                resultSet.getString("marka"),
                resultSet.getString("tedarikci"),
                resultSet.getString("depo"),
                resultSet.getString("raf_no"),
                resultSet.getString("birim"),
                resultSet.getInt("stok_miktari"),
                resultSet.getInt("kritik_limit"),
                resultSet.getString("giris_tarihi")
        );
    }
}
