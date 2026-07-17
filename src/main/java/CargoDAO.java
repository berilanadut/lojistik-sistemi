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

    // =========================================
    // 4. KARGO GÜNCELLE (TRANSACTION KORUMALI)
    // UPDATE
    // =========================================

    public static boolean update(Cargo cargo) {
        Cargo eskiCargo = findByKargoNo(cargo.getKargoNo());

        if (eskiCargo == null) {
            return false; // Güncellenecek kargo yoksa işlemi durdur
        }

        // Geçmiş tablosuna kayıt SQL'i
        String sqlGecmis = """
                INSERT INTO cargo_gecmis(
                    kargo_no, gonderici, alici, gonderici_sube, teslimat_sube,
                    desi, agirlik, durum, verilis_tarihi, tahmini_teslim,
                    teslim_tarihi, plaka, surucu, takip_notu, islem_turu, degisiklik_tarihi
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'GÜNCELLEME', datetime('now', 'localtime'))
                """;

        // Ana tablo güncelleme SQL'i
        String sqlUpdate = """
                UPDATE cargo
                SET
                    gonderici = ?, alici = ?, gonderici_sube = ?, teslimat_sube = ?,
                    desi = ?, agirlik = ?, durum = ?, verilis_tarihi = ?,
                    tahmini_teslim = ?, teslim_tarihi = ?, plaka = ?, surucu = ?, takip_notu = ?
                WHERE kargo_no = ?
                """;

        Connection connection = null;

        try {
            // İki işlem de AYNI connection üzerinden yürüyecek
            connection = Database.getConnection();

            // 1. ADIM: Otomatik kaydetmeyi (AutoCommit) durdur. Transaction başlasın.
            connection.setAutoCommit(false);

            // 2. ADIM: Önce eski veriyi geçmiş tablosuna yaz
            try (PreparedStatement stmtGecmis = connection.prepareStatement(sqlGecmis)) {
                stmtGecmis.setString(1, eskiCargo.getKargoNo());
                stmtGecmis.setString(2, eskiCargo.getGonderici());
                stmtGecmis.setString(3, eskiCargo.getAlici());
                stmtGecmis.setString(4, eskiCargo.getGondericiSube());
                stmtGecmis.setString(5, eskiCargo.getTeslimatSube());
                stmtGecmis.setDouble(6, eskiCargo.getDesi());
                stmtGecmis.setDouble(7, eskiCargo.getAgirlik());
                stmtGecmis.setString(8, eskiCargo.getDurum());
                stmtGecmis.setString(9, eskiCargo.getVerilisTarihi());
                stmtGecmis.setString(10, eskiCargo.getTahminiTeslim());
                stmtGecmis.setString(11, eskiCargo.getTeslimTarihi());
                stmtGecmis.setString(12, eskiCargo.getPlaka());
                stmtGecmis.setString(13, eskiCargo.getSurucu());
                stmtGecmis.setString(14, eskiCargo.getTakipNotu());

                stmtGecmis.executeUpdate();
            }

            // 3. ADIM: Sonra yeni verilerle ana tabloyu güncelle
            try (PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, cargo.getGonderici());
                stmtUpdate.setString(2, cargo.getAlici());
                stmtUpdate.setString(3, cargo.getGondericiSube());
                stmtUpdate.setString(4, cargo.getTeslimatSube());
                stmtUpdate.setDouble(5, cargo.getDesi());
                stmtUpdate.setDouble(6, cargo.getAgirlik());
                stmtUpdate.setString(7, cargo.getDurum());
                stmtUpdate.setString(8, cargo.getVerilisTarihi());
                stmtUpdate.setString(9, cargo.getTahminiTeslim());
                stmtUpdate.setString(10, cargo.getTeslimTarihi());
                stmtUpdate.setString(11, cargo.getPlaka());
                stmtUpdate.setString(12, cargo.getSurucu());
                stmtUpdate.setString(13, cargo.getTakipNotu());
                stmtUpdate.setString(14, cargo.getKargoNo());

                stmtUpdate.executeUpdate();
            }

            // 4. ADIM: İki sorgu da hatasız çalıştıysa veritabanına kalıcı olarak işle
            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            // 5. ADIM: Herhangi bir aşamada hata olursa, veritabanını işlem öncesi haline geri döndür
            if (connection != null) {
                try {
                    System.out.println("Hata tespit edildi! Veritabanı bütünlüğü için işlemler geri alınıyor (ROLLBACK)...");
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;

        } finally {
            // 6. ADIM: Connection havuza dönmeden önce veya kapanmadan önce standart ayarlarına (true) döndür
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
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