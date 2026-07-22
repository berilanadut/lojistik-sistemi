import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UlasimUrunDAO {

    // =========================================
    // ARACA YENİ ÜRÜN EKLE
    // =========================================

    public void urunEkle(UlasimUrun urun) {

        if (
                urunAractaVarMi(
                        urun.getPlaka(),
                        urun.getUrunKodu()
                )
        ) {

            throw new IllegalArgumentException(
                    "Bu ürün bu araçta zaten mevcut. Güncelleme işlemini kullanın."
            );
        }

        String sql = """
                INSERT INTO ulasim_urun (
                    plaka,
                    urun_kodu,
                    urun_adi,
                    miktar,
                    koli_no,
                    teslim_durumu
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    urun.getPlaka()
            );

            preparedStatement.setString(
                    2,
                    urun.getUrunKodu()
            );

            preparedStatement.setString(
                    3,
                    urun.getUrunAdi()
            );

            preparedStatement.setInt(
                    4,
                    urun.getMiktar()
            );

            preparedStatement.setString(
                    5,
                    urun.getKoliNo()
            );

            preparedStatement.setString(
                    6,
                    urun.getTeslimDurumu()
            );

            preparedStatement.executeUpdate();

            System.out.println(
                    "Ürün ulaşıma eklendi."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Ürün veritabanına eklenemedi."
            );

        }

    }


    // =========================================
    // PLAKAYA GÖRE ÜRÜNLERİ GETİR
    // =========================================

    public List<UlasimUrun> plakayaGoreUrunleriGetir(
            String plaka
    ) {

        List<UlasimUrun> liste =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM ulasim_urun
                WHERE plaka = ?
                ORDER BY id DESC
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    plaka
            );

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                UlasimUrun urun =
                        new UlasimUrun();

                urun.setId(
                        resultSet.getInt("id")
                );

                urun.setPlaka(
                        resultSet.getString("plaka")
                );

                urun.setUrunKodu(
                        resultSet.getString("urun_kodu")
                );

                urun.setUrunAdi(
                        resultSet.getString("urun_adi")
                );

                urun.setMiktar(
                        resultSet.getInt("miktar")
                );

                urun.setKoliNo(
                        resultSet.getString("koli_no")
                );

                urun.setTeslimDurumu(
                        resultSet.getString("teslim_durumu")
                );

                liste.add(urun);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return liste;

    }


    // =========================================
    // ARAÇTAKİ ÜRÜNÜ GÜNCELLE
    // =========================================

    public boolean urunGuncelle(
            UlasimUrun urun
    ) {

        String sql = """
                UPDATE ulasim_urun
                SET urun_kodu = ?,
                    urun_adi = ?,
                    miktar = ?,
                    koli_no = ?,
                    teslim_durumu = ?
                WHERE id = ?
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    urun.getUrunKodu()
            );

            preparedStatement.setString(
                    2,
                    urun.getUrunAdi()
            );

            preparedStatement.setInt(
                    3,
                    urun.getMiktar()
            );

            preparedStatement.setString(
                    4,
                    urun.getKoliNo()
            );

            preparedStatement.setString(
                    5,
                    urun.getTeslimDurumu()
            );

            preparedStatement.setInt(
                    6,
                    urun.getId()
            );

            int etkilenenSatir =
                    preparedStatement.executeUpdate();

            if (etkilenenSatir > 0) {

                System.out.println(
                        "Ürün güncellendi."
                );

                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }


    // =========================================
    // ARAÇTAN ÜRÜN SİL
    // =========================================

    public boolean urunSil(
            int id
    ) {

        String sql = """
                DELETE FROM ulasim_urun
                WHERE id = ?
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setInt(
                    1,
                    id
            );

            int etkilenenSatir =
                    preparedStatement.executeUpdate();

            if (etkilenenSatir > 0) {

                System.out.println(
                        "Ürün ulaşımdan silindi."
                );

                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }


    // =========================================
    // AYNI ÜRÜN BU ARAÇTA VAR MI?
    // =========================================

    public boolean urunAractaVarMi(
            String plaka,
            String urunKodu
    ) {

        String sql = """
                SELECT COUNT(*)
                FROM ulasim_urun
                WHERE plaka = ?
                AND LOWER(urun_kodu) = LOWER(?)
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    plaka
            );

            preparedStatement.setString(
                    2,
                    urunKodu
            );

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            if (resultSet.next()) {

                int kayitSayisi =
                        resultSet.getInt(1);

                return kayitSayisi > 0;
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

}