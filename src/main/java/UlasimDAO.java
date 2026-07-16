import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UlasimDAO {

    // =========================================
    // 1. ULAŞIM KAYDET
    // INSERT
    // =========================================

    public static boolean save(Ulasim ulasim) {

        String sql = """
                INSERT INTO ulasim(
                    plaka,
                    surucu,
                    baslangic,
                    varis,
                    rota,
                    guncel_konum,
                    baslangic_zamani,
                    tahmini_sure,
                    toplam_mesafe,
                    yakit,
                    rotadan_cikti,
                    teslim_alindi,
                    koli_no,
                    teslim_alma_zamani,
                    teslim_alan,
                    teslim_alinan_firma,
                    teslim_edildi,
                    teslim_zamani,
                    musteri,
                    onay_kodu,
                    rota_durumu,
                    aciklama
                )
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, ulasim.getPlaka());
            statement.setString(2, ulasim.getSurucu());
            statement.setString(3, ulasim.getBaslangic());
            statement.setString(4, ulasim.getVaris());
            statement.setString(5, ulasim.getRota());
            statement.setString(6, ulasim.getGuncelKonum());
            statement.setString(7, ulasim.getBaslangicZamani());
            statement.setInt(8, ulasim.getTahminiSure());
            statement.setDouble(9, ulasim.getToplamMesafe());
            statement.setDouble(10, ulasim.getYakit());
            statement.setString(11, ulasim.getRotadanCikti());
            statement.setString(12, ulasim.getTeslimAlindi());
            statement.setString(13, ulasim.getKoliNo());
            statement.setString(14, ulasim.getTeslimAlmaZamani());
            statement.setString(15, ulasim.getTeslimAlan());
            statement.setString(16, ulasim.getTeslimAlinanFirma());
            statement.setString(17, ulasim.getTeslimEdildi());
            statement.setString(18, ulasim.getTeslimZamani());
            statement.setString(19, ulasim.getMusteri());
            statement.setString(20, ulasim.getOnayKodu());
            statement.setString(21, ulasim.getRotaDurumu());
            statement.setString(22, ulasim.getAciklama());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================================
    // 2. BÜTÜN ULAŞIM KAYITLARINI GETİR
    // SELECT *
    // =========================================

    public static List<Ulasim> findAll() {

        String sql = "SELECT * FROM ulasim";

        List<Ulasim> ulasimlar = new ArrayList<>();

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Ulasim ulasim =
                        resultSetToUlasim(resultSet);

                ulasimlar.add(ulasim);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return ulasimlar;
    }


    // =========================================
    // 3. PLAKAYA GÖRE ULAŞIM ARA
    // SELECT ... WHERE
    // =========================================

    public static Ulasim findByPlaka(String plaka) {

        String sql = """
                SELECT *
                FROM ulasim
                WHERE plaka = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, plaka);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSetToUlasim(resultSet);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // =========================================
    // 4. ULAŞIM GÜNCELLE
    // UPDATE
    // =========================================

    public static boolean update(Ulasim ulasim) {

        String sql = """
                UPDATE ulasim
                SET
                    surucu = ?,
                    baslangic = ?,
                    varis = ?,
                    rota = ?,
                    guncel_konum = ?,
                    baslangic_zamani = ?,
                    tahmini_sure = ?,
                    toplam_mesafe = ?,
                    yakit = ?,
                    rotadan_cikti = ?,
                    teslim_alindi = ?,
                    koli_no = ?,
                    teslim_alma_zamani = ?,
                    teslim_alan = ?,
                    teslim_alinan_firma = ?,
                    teslim_edildi = ?,
                    teslim_zamani = ?,
                    musteri = ?,
                    onay_kodu = ?,
                    rota_durumu = ?,
                    aciklama = ?
                WHERE plaka = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, ulasim.getSurucu());
            statement.setString(2, ulasim.getBaslangic());
            statement.setString(3, ulasim.getVaris());
            statement.setString(4, ulasim.getRota());
            statement.setString(5, ulasim.getGuncelKonum());
            statement.setString(6, ulasim.getBaslangicZamani());
            statement.setInt(7, ulasim.getTahminiSure());
            statement.setDouble(8, ulasim.getToplamMesafe());
            statement.setDouble(9, ulasim.getYakit());
            statement.setString(10, ulasim.getRotadanCikti());
            statement.setString(11, ulasim.getTeslimAlindi());
            statement.setString(12, ulasim.getKoliNo());
            statement.setString(13, ulasim.getTeslimAlmaZamani());
            statement.setString(14, ulasim.getTeslimAlan());
            statement.setString(15, ulasim.getTeslimAlinanFirma());
            statement.setString(16, ulasim.getTeslimEdildi());
            statement.setString(17, ulasim.getTeslimZamani());
            statement.setString(18, ulasim.getMusteri());
            statement.setString(19, ulasim.getOnayKodu());
            statement.setString(20, ulasim.getRotaDurumu());
            statement.setString(21, ulasim.getAciklama());

            // Hangi ulaşım kaydının güncelleneceğini belirler
            statement.setString(22, ulasim.getPlaka());

            int degisenSatirSayisi =
                    statement.executeUpdate();

            return degisenSatirSayisi > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================================
    // 5. ULAŞIM SİL
    // DELETE
    // =========================================

    public static boolean delete(String plaka) {

        String sql = """
                DELETE FROM ulasim
                WHERE plaka = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, plaka);

            int silinenSatirSayisi =
                    statement.executeUpdate();

            return silinenSatirSayisi > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================================
    // RESULTSET SATIRINI ULAŞIM NESNESİNE ÇEVİR
    // =========================================

    private static Ulasim resultSetToUlasim(
            ResultSet resultSet
    ) throws SQLException {

        return new Ulasim(
                resultSet.getString("plaka"),
                resultSet.getString("surucu"),
                resultSet.getString("baslangic"),
                resultSet.getString("varis"),
                resultSet.getString("rota"),
                resultSet.getString("guncel_konum"),
                resultSet.getString("baslangic_zamani"),
                resultSet.getInt("tahmini_sure"),
                resultSet.getDouble("toplam_mesafe"),
                resultSet.getDouble("yakit"),
                resultSet.getString("rotadan_cikti"),
                resultSet.getString("teslim_alindi"),
                resultSet.getString("koli_no"),
                resultSet.getString("teslim_alma_zamani"),
                resultSet.getString("teslim_alan"),
                resultSet.getString("teslim_alinan_firma"),
                resultSet.getString("teslim_edildi"),
                resultSet.getString("teslim_zamani"),
                resultSet.getString("musteri"),
                resultSet.getString("onay_kodu"),
                resultSet.getString("rota_durumu"),
                resultSet.getString("aciklama")
        );
    }
}