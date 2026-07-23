import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MesaiGecmisDAO {

    // =========================================
    // MESAİ GEÇMİŞİNİ KAYDET
    // =========================================

    public static boolean gecmisiKaydet(
            MesaiGecmis mesaiGecmis
    ) {

        String sql = """
                INSERT INTO mesai_gecmis(
                    islem_tarihi,
                    islem_turu,
                    mesai_no,
                    sicil_no,
                    ad_soyad,
                    departman,
                    pozisyon,
                    mesai_tarihi,
                    toplam_mesai_saati,
                    toplam_mesai_ucreti,
                    onay_durumu,
                    odeme_durumu
                )
                VALUES(
                    ?,?,?,?,?,?,
                    ?,?,?,?,?,?
                )
                """;

        try (
                Connection connection = Database.getConnection();

                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, mesaiGecmis.getIslemTarihi()
            );

            statement.setString(2, mesaiGecmis.getIslemTuru()
            );

            statement.setString(3, mesaiGecmis.getMesaiNo()
            );

            statement.setString(4, mesaiGecmis.getSicilNo()
            );

            statement.setString(5, mesaiGecmis.getAdSoyad()
            );

            statement.setString(6, mesaiGecmis.getDepartman()
            );

            statement.setString(7, mesaiGecmis.getPozisyon()
            );

            statement.setString(8, mesaiGecmis.getMesaiTarihi()
            );

            statement.setDouble(9, mesaiGecmis.getToplamMesaiSaati()
            );

            statement.setDouble(10, mesaiGecmis.getToplamMesaiUcreti()
            );

            statement.setString(11, mesaiGecmis.getOnayDurumu()
            );

            statement.setString(12, mesaiGecmis.getOdemeDurumu()
            );

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================
    // MESAİ NUMARASINA GÖRE GEÇMİŞİ GETİR
    // =========================================

    public static List<MesaiGecmis>
    mesaiGecmisiniGetir(String mesaiNo) {

        String sql = """
                SELECT *
                FROM mesai_gecmis
                WHERE mesai_no = ?
                ORDER BY id DESC
                """;

        List<MesaiGecmis> gecmisListesi = new ArrayList<>();


        try (
                Connection connection =Database.getConnection();


                PreparedStatement statement = connection.prepareStatement(sql)

        ) {

            statement.setString(1, mesaiNo);

            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    MesaiGecmis mesaiGecmis = resultSetToMesaiGecmis(resultSet);

                    gecmisListesi.add( mesaiGecmis );
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

    private static MesaiGecmis
    resultSetToMesaiGecmis(
            ResultSet resultSet
    ) throws SQLException {

        return new MesaiGecmis(
                resultSet.getInt("id"),
                resultSet.getString("islem_tarihi"),
                resultSet.getString("islem_turu"),
                resultSet.getString("mesai_no"),
                resultSet.getString("sicil_no"),
                resultSet.getString("ad_soyad"),
                resultSet.getString("departman"),
                resultSet.getString("pozisyon"),
                resultSet.getString("mesai_tarihi"),
                resultSet.getDouble("toplam_mesai_saati"),
                resultSet.getDouble("toplam_mesai_ucreti"),
                resultSet.getString("onay_durumu"),
                resultSet.getString("odeme_durumu")
        );
    }
}