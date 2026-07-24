import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                    rota_durumu,
                    aciklama
                )
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)
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
            statement.setString(12, ulasim.getRotaDurumu());
            statement.setString(13, ulasim.getAciklama());

            int eklenenSatirSayisi =
                    statement.executeUpdate();

            if (eklenenSatirSayisi > 0) {

                ulasimGecmisineKaydet(
                        ulasim,
                        "EKLENDİ"
                );

                return true;
            }

            return false;

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

        Ulasim eskiUlasim =
                findByPlaka(ulasim.getPlaka());

        if (eskiUlasim == null) {

            return false;
        }

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
            statement.setString(11, ulasim.getRotaDurumu());
            statement.setString(12, ulasim.getAciklama());

            // Hangi ulaşım kaydının güncelleneceğini belirler
            statement.setString(13, ulasim.getPlaka());

            int degisenSatirSayisi =
                    statement.executeUpdate();

            if (degisenSatirSayisi > 0) {

                ulasimGecmisineKaydet(
                        eskiUlasim,
                        "GÜNCELLEME ÖNCESİ"
                );

                return true;
            }

            return false;

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

        Ulasim silinecekUlasim =
                findByPlaka(plaka);

        if (silinecekUlasim == null) {

            return false;
        }

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

            if (silinenSatirSayisi > 0) {

                ulasimGecmisineKaydet(
                        silinecekUlasim,
                        "SİLİNDİ"
                );

                return true;
            }

            return false;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================================
    // ULAŞIM İŞLEMİNİ GEÇMİŞE KAYDET
    // =========================================

    private static void ulasimGecmisineKaydet(
            Ulasim ulasim,
            String islemTuru
    ) {

        String islemTarihi =
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                );

        UlasimGecmis ulasimGecmis =
                new UlasimGecmis(
                        0,
                        islemTarihi,
                        islemTuru,
                        ulasim.getPlaka(),
                        ulasim.getSurucu(),
                        ulasim.getBaslangic(),
                        ulasim.getVaris(),
                        ulasim.getRota(),
                        ulasim.getGuncelKonum(),
                        ulasim.getBaslangicZamani(),
                        ulasim.getTahminiSure(),
                        ulasim.getToplamMesafe(),
                        ulasim.getYakit(),
                        ulasim.getRotadanCikti(),
                        ulasim.getRotaDurumu(),
                        ulasim.getAciklama()
                );

        UlasimGecmisDAO.gecmisiKaydet(
                ulasimGecmis
        );
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
                resultSet.getString("rota_durumu"),
                resultSet.getString("aciklama")
        );
    }
}