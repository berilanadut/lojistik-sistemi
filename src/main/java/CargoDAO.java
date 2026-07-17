import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    // =========================================
    // 1. KAYDET
    // INSERT
    // =========================================

    public static boolean save(Cargo cargo) {

        String sql = """
                INSERT INTO cargo(
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
                    takip_notu
                )
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, cargo.getKargoNo());
            statement.setString(2, cargo.getGonderici());
            statement.setString(3, cargo.getAlici());
            statement.setString(4, cargo.getGondericiSube());
            statement.setString(5, cargo.getTeslimatSube());
            statement.setDouble(6, cargo.getDesi());
            statement.setDouble(7, cargo.getAgirlik());
            statement.setString(8, cargo.getDurum());
            statement.setString(9, cargo.getVerilisTarihi());
            statement.setString(10, cargo.getTahminiTeslim());
            statement.setString(11, cargo.getTeslimTarihi());
            statement.setString(12, cargo.getPlaka());
            statement.setString(13, cargo.getSurucu());
            statement.setString(14, cargo.getTakipNotu());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================================
    // 2. BÜTÜN KARGOLARI GETİR
    // SELECT *
    // =========================================

    public static List<Cargo> findAll() {

        String sql = "SELECT * FROM cargo";

        List<Cargo> kargolar = new ArrayList<>();

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Cargo cargo = resultSetToCargo(resultSet);

                kargolar.add(cargo);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return kargolar;
    }


    // =========================================
    // 3. KARGO NUMARASINA GÖRE ARA
    // SELECT ... WHERE
    // =========================================

    public static Cargo findByKargoNo(String kargoNo) {

        String sql = """
                SELECT *
                FROM cargo
                WHERE kargo_no = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // SQL içindeki birinci ? işaretini doldurur
            statement.setString(1, kargoNo);

            try (ResultSet resultSet = statement.executeQuery()) {

                // Kayıt bulunduysa
                if (resultSet.next()) {

                    return resultSetToCargo(resultSet);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        // Kargo bulunamadıysa null döner
        return null;
    }


    // =========================================
    // 4. KARGO GÜNCELLE
    // UPDATE
    // =========================================

    public static boolean update(Cargo cargo) {
        Cargo eskiCargo = findByKargoNo(cargo.getKargoNo());

        if (eskiCargo != null) {
            CargoGecmisDAO cargoGecmisDAO = new CargoGecmisDAO();
            cargoGecmisDAO.gecmiseKaydet(eskiCargo);
        }

        String sql = """
                UPDATE cargo
                SET
                    gonderici = ?,
                    alici = ?,
                    gonderici_sube = ?,
                    teslimat_sube = ?,
                    desi = ?,
                    agirlik = ?,
                    durum = ?,
                    verilis_tarihi = ?,
                    tahmini_teslim = ?,
                    teslim_tarihi = ?,
                    plaka = ?,
                    surucu = ?,
                    takip_notu = ?
                WHERE kargo_no = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, cargo.getGonderici());
            statement.setString(2, cargo.getAlici());
            statement.setString(3, cargo.getGondericiSube());
            statement.setString(4, cargo.getTeslimatSube());
            statement.setDouble(5, cargo.getDesi());
            statement.setDouble(6, cargo.getAgirlik());
            statement.setString(7, cargo.getDurum());
            statement.setString(8, cargo.getVerilisTarihi());
            statement.setString(9, cargo.getTahminiTeslim());
            statement.setString(10, cargo.getTeslimTarihi());
            statement.setString(11, cargo.getPlaka());
            statement.setString(12, cargo.getSurucu());
            statement.setString(13, cargo.getTakipNotu());

            // Hangi kargonun güncelleneceğini belirler
            statement.setString(14, cargo.getKargoNo());

            int degisenSatirSayisi = statement.executeUpdate();

            return degisenSatirSayisi > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================================
    // 5. KARGO SİL
    // DELETE
    // =========================================

    public static boolean delete(String kargoNo) {

        String sql = """
                DELETE FROM cargo
                WHERE kargo_no = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, kargoNo);

            int silinenSatirSayisi = statement.executeUpdate();

            return silinenSatirSayisi > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================================
    // RESULTSET SATIRINI CARGO NESNESİNE ÇEVİR
    // =========================================

    private static Cargo resultSetToCargo(
            ResultSet resultSet
    ) throws SQLException {

        return new Cargo(
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
                resultSet.getString("takip_notu")
        );
    }
}