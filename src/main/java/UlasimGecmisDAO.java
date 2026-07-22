import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UlasimGecmisDAO {

    // =========================================
    // ULAŞIM GEÇMİŞİNİ KAYDET
    // =========================================

    public static boolean gecmisiKaydet(
            UlasimGecmis ulasimGecmis
    ) {

        String sql = """
                INSERT INTO ulasim_gecmis(
                    islem_tarihi,
                    islem_turu,
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
                VALUES(
                    ?,?,?,?,?,?,?,?,?,?,?,?,
                    ?,?,?,?,?,?,?,?,?,?,?,?
                )
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    ulasimGecmis.getIslemTarihi()
            );

            statement.setString(
                    2,
                    ulasimGecmis.getIslemTuru()
            );

            statement.setString(
                    3,
                    ulasimGecmis.getPlaka()
            );

            statement.setString(
                    4,
                    ulasimGecmis.getSurucu()
            );

            statement.setString(
                    5,
                    ulasimGecmis.getBaslangic()
            );

            statement.setString(
                    6,
                    ulasimGecmis.getVaris()
            );

            statement.setString(
                    7,
                    ulasimGecmis.getRota()
            );

            statement.setString(
                    8,
                    ulasimGecmis.getGuncelKonum()
            );

            statement.setString(
                    9,
                    ulasimGecmis.getBaslangicZamani()
            );

            statement.setInt(
                    10,
                    ulasimGecmis.getTahminiSure()
            );

            statement.setDouble(
                    11,
                    ulasimGecmis.getToplamMesafe()
            );

            statement.setDouble(
                    12,
                    ulasimGecmis.getYakit()
            );

            statement.setString(
                    13,
                    ulasimGecmis.getRotadanCikti()
            );

            statement.setString(
                    14,
                    ulasimGecmis.getTeslimAlindi()
            );

            statement.setString(
                    15,
                    ulasimGecmis.getKoliNo()
            );

            statement.setString(
                    16,
                    ulasimGecmis.getTeslimAlmaZamani()
            );

            statement.setString(
                    17,
                    ulasimGecmis.getTeslimAlan()
            );

            statement.setString(
                    18,
                    ulasimGecmis.getTeslimAlinanFirma()
            );

            statement.setString(
                    19,
                    ulasimGecmis.getTeslimEdildi()
            );

            statement.setString(
                    20,
                    ulasimGecmis.getTeslimZamani()
            );

            statement.setString(
                    21,
                    ulasimGecmis.getMusteri()
            );

            statement.setString(
                    22,
                    ulasimGecmis.getOnayKodu()
            );

            statement.setString(
                    23,
                    ulasimGecmis.getRotaDurumu()
            );

            statement.setString(
                    24,
                    ulasimGecmis.getAciklama()
            );

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================
    // PLAKAYA GÖRE ULAŞIM GEÇMİŞİNİ GETİR
    // =========================================

    public static List<UlasimGecmis>
    ulasimGecmisiniGetir(String plaka) {

        String sql = """
                SELECT *
                FROM ulasim_gecmis
                WHERE plaka = ?
                ORDER BY id DESC
                """;

        List<UlasimGecmis> gecmisListesi =
                new ArrayList<>();

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, plaka);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    UlasimGecmis ulasimGecmis =
                            resultSetToUlasimGecmis(
                                    resultSet
                            );

                    gecmisListesi.add(
                            ulasimGecmis
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return gecmisListesi;
    }

    // =========================================
    // RESULTSET SATIRINI NESNEYE ÇEVİR
    // =========================================

    private static UlasimGecmis
    resultSetToUlasimGecmis(
            ResultSet resultSet
    ) throws SQLException {

        return new UlasimGecmis(
                resultSet.getInt("id"),
                resultSet.getString("islem_tarihi"),
                resultSet.getString("islem_turu"),
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