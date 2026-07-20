import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CargoGecmisDAO {

    public void gecmiseKaydet(Cargo cargo) {

        String sql = """
                INSERT INTO cargo_gecmis (
                    kargo_no,
                    gonderici,
                    alici,
                    gonderici_sube,
                    teslimat_sube,
                    desi,
                    agirlik,
                    durum,
                    verilis_tarihi,
                    tahmini_teslim,
                    teslim_tarihi,
                    plaka,
                    surucu,
                    takip_notu,
                    islem_turu,
                    degisiklik_tarihi
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, cargo.getKargoNo());
            preparedStatement.setString(2, cargo.getGonderici());
            preparedStatement.setString(3, cargo.getAlici());
            preparedStatement.setString(4, cargo.getGondericiSube());
            preparedStatement.setString(5, cargo.getTeslimatSube());
            preparedStatement.setDouble(6, cargo.getDesi());
            preparedStatement.setDouble(7, cargo.getAgirlik());
            preparedStatement.setString(8, cargo.getDurum());
            preparedStatement.setString(9, cargo.getVerilisTarihi());
            preparedStatement.setString(10, cargo.getTahminiTeslim());
            preparedStatement.setString(11, cargo.getTeslimTarihi());
            preparedStatement.setString(12, cargo.getPlaka());
            preparedStatement.setString(13, cargo.getSurucu());
            preparedStatement.setString(14, cargo.getTakipNotu());
            preparedStatement.setString(15, "Güncellendi");
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

            preparedStatement.setString(
                    16,
                    LocalDateTime.now().format(formatter)
            );

            preparedStatement.executeUpdate();

            System.out.println("Eski kargo bilgisi geçmişe kaydedildi.");

        } catch (SQLException e) {
            System.out.println(
                    "Kargo geçmişe kaydedilemedi: " + e.getMessage()
            );
        }
    }

    public List<CargoGecmis> kargoGecmisiniGetir(String kargoNo) {

        List<CargoGecmis> gecmisListesi = new ArrayList<>();

        String sql = """
                SELECT *
                FROM cargo_gecmis
                WHERE kargo_no = ?
                ORDER BY id DESC
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, kargoNo);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                CargoGecmis cargoGecmis = new CargoGecmis(
                        resultSet.getInt("id"),
                        resultSet.getString("kargo_no"),
                        resultSet.getString("gonderici"),
                        resultSet.getString("alici"),
                        resultSet.getString("gonderici_sube"),
                        resultSet.getString("teslimat_sube"),
                        resultSet.getDouble("desi"),
                        resultSet.getDouble("agirlik"),
                        resultSet.getString("durum"),
                        resultSet.getString("verilis_tarihi"),
                        resultSet.getString("tahmini_teslim"),
                        resultSet.getString("teslim_tarihi"),
                        resultSet.getString("plaka"),
                        resultSet.getString("surucu"),
                        resultSet.getString("takip_notu"),
                        resultSet.getString("islem_turu"),
                        resultSet.getString("degisiklik_tarihi")
                );

                gecmisListesi.add(cargoGecmis);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Kargo geçmişi getirilemedi: " + e.getMessage()
            );
        }

        return gecmisListesi;
    }
}