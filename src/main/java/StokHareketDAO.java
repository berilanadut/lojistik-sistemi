import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StokHareketDAO {

    public  static void hareketiKaydet(StokHareket stokHareket) {

        String sql = """
                INSERT INTO stok_hareket (
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
                    giris_tarihi,
                    islem_turu,
                    miktar,
                    onceki_stok,
                    yeni_stok,
                    islem_tarihi
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, stokHareket.getUrunKodu());
            preparedStatement.setString(2, stokHareket.getUrunAdi());
            preparedStatement.setString(3, stokHareket.getKategori());
            preparedStatement.setString(4, stokHareket.getMarka());
            preparedStatement.setString(5, stokHareket.getTedarikci());
            preparedStatement.setString(6, stokHareket.getDepo());
            preparedStatement.setString(7, stokHareket.getRafNo());
            preparedStatement.setString(8, stokHareket.getBirim());
            preparedStatement.setInt(9, stokHareket.getStokMiktari());
            preparedStatement.setInt(10, stokHareket.getKritikLimit());
            preparedStatement.setString(11, stokHareket.getGirisTarihi());
            preparedStatement.setString(12, stokHareket.getIslemTuru());
            preparedStatement.setInt(13, stokHareket.getMiktar());
            preparedStatement.setInt(14, stokHareket.getOncekiStok());
            preparedStatement.setInt(15, stokHareket.getYeniStok());
            preparedStatement.setString(16, stokHareket.getIslemTarihi());

            preparedStatement.executeUpdate();

            System.out.println("Stok hareketi kaydedildi.");

        } catch (SQLException e) {

            System.out.println(
                    "Stok hareketi kaydedilemedi: "
                            + e.getMessage()
            );
        }
    }

    public  static List<StokHareket> stokGecmisiniGetir(
            String urunKodu
    ) {

        List<StokHareket> hareketListesi =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM stok_hareket
                WHERE urun_kodu = ?
                ORDER BY id DESC
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    urunKodu
            );

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                StokHareket stokHareket =
                        new StokHareket(
                                resultSet.getInt("id"),
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
                                resultSet.getString("giris_tarihi"),
                                resultSet.getString("islem_turu"),
                                resultSet.getInt("miktar"),
                                resultSet.getInt("onceki_stok"),
                                resultSet.getInt("yeni_stok"),
                                resultSet.getString("islem_tarihi")
                        );

                hareketListesi.add(stokHareket);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Stok geçmişi getirilemedi: "
                            + e.getMessage()
            );
        }

        return hareketListesi;
    }
}