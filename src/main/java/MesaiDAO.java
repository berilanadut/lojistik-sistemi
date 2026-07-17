import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MesaiDAO {

    // =========================================
    // 1. MESAİ KAYDET
    // INSERT
    // =========================================

    public static boolean save(Mesai mesai) {

        mesai.ucretleriHesapla();

        String sql = """
                INSERT INTO mesai(
                    mesai_no,
                    sicil_no,
                    ad_soyad,
                    departman,
                    pozisyon,
                    mesai_tarihi,
                    baslangic_saati,
                    bitis_saati,
                    mola_suresi,
                    toplam_mesai_saati,
                    mesai_nedeni,
                    yapilan_is,
                    proje_operasyon,
                    sube_depo,
                    aciklama,
                    aylik_ucret,
                    aylik_calisma_saati,
                    mesai_katsayisi,
                    normal_saatlik_ucret,
                    mesai_saat_ucreti,
                    toplam_mesai_ucreti,
                    rota_plani_var_mi,
                    sirali_teslimat,
                    teslimat_sorunu,
                    yerinde_kapatma,
                    irsaliye_atf_no,
                    yonetici,
                    onay_durumu,
                    onay_tarihi,
                    odeme_durumu,
                    odeme_tarihi
                )
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, mesai.getMesaiNo());
            statement.setString(2, mesai.getSicilNo());
            statement.setString(3, mesai.getAdSoyad());
            statement.setString(4, mesai.getDepartman());
            statement.setString(5, mesai.getPozisyon());
            statement.setString(6, mesai.getMesaiTarihi());
            statement.setString(7, mesai.getBaslangicSaati());
            statement.setString(8, mesai.getBitisSaati());
            statement.setInt(9, mesai.getMolaSuresi());
            statement.setDouble(10, mesai.getToplamMesaiSaati());
            statement.setString(11, mesai.getMesaiNedeni());
            statement.setString(12, mesai.getYapilanIs());
            statement.setString(13, mesai.getProjeOperasyon());
            statement.setString(14, mesai.getSubeDepo());
            statement.setString(15, mesai.getAciklama());
            statement.setDouble(16, mesai.getAylikUcret());
            statement.setInt(17, mesai.getAylikCalismaSaati());
            statement.setDouble(18, mesai.getMesaiKatsayisi());
            statement.setDouble(19, mesai.getNormalSaatlikUcret());
            statement.setDouble(20, mesai.getMesaiSaatUcreti());
            statement.setDouble(21, mesai.getToplamMesaiUcreti());
            statement.setString(22, mesai.getRotaPlaniVarMi());
            statement.setString(23, mesai.getSiraliTeslimat());
            statement.setString(24, mesai.getTeslimatSorunu());
            statement.setString(25, mesai.getYerindeKapatma());
            statement.setString(26, mesai.getIrsaliyeAtfNo());
            statement.setString(27, mesai.getYonetici());
            statement.setString(28, mesai.getOnayDurumu());
            statement.setString(29, mesai.getOnayTarihi());
            statement.setString(30, mesai.getOdemeDurumu());
            statement.setString(31, mesai.getOdemeTarihi());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================
    // 2. BÜTÜN MESAİ KAYITLARINI GETİR
    // SELECT *
    // =========================================

    public static List<Mesai> findAll() {

        String sql = "SELECT * FROM mesai";
        List<Mesai> mesailer = new ArrayList<>();

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                Mesai mesai = resultSetToMesai(resultSet);
                mesailer.add(mesai);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mesailer;
    }

    // =========================================
    // 3. MESAİ NUMARASINA GÖRE ARA
    // SELECT ... WHERE
    // =========================================

    public static Mesai findByMesaiNo(String mesaiNo) {

        String sql = """
                SELECT *
                FROM mesai
                WHERE mesai_no = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, mesaiNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSetToMesai(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================================
    // 4. MESAİ GÜNCELLE
    // UPDATE
    // =========================================

    public static boolean update(Mesai mesai) {

        mesai.ucretleriHesapla();

        String sql = """
                UPDATE mesai
                SET
                    sicil_no = ?,
                    ad_soyad = ?,
                    departman = ?,
                    pozisyon = ?,
                    mesai_tarihi = ?,
                    baslangic_saati = ?,
                    bitis_saati = ?,
                    mola_suresi = ?,
                    toplam_mesai_saati = ?,
                    mesai_nedeni = ?,
                    yapilan_is = ?,
                    proje_operasyon = ?,
                    sube_depo = ?,
                    aciklama = ?,
                    aylik_ucret = ?,
                    aylik_calisma_saati = ?,
                    mesai_katsayisi = ?,
                    normal_saatlik_ucret = ?,
                    mesai_saat_ucreti = ?,
                    toplam_mesai_ucreti = ?,
                    rota_plani_var_mi = ?,
                    sirali_teslimat = ?,
                    teslimat_sorunu = ?,
                    yerinde_kapatma = ?,
                    irsaliye_atf_no = ?,
                    yonetici = ?,
                    onay_durumu = ?,
                    onay_tarihi = ?,
                    odeme_durumu = ?,
                    odeme_tarihi = ?
                WHERE mesai_no = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, mesai.getSicilNo());
            statement.setString(2, mesai.getAdSoyad());
            statement.setString(3, mesai.getDepartman());
            statement.setString(4, mesai.getPozisyon());
            statement.setString(5, mesai.getMesaiTarihi());
            statement.setString(6, mesai.getBaslangicSaati());
            statement.setString(7, mesai.getBitisSaati());
            statement.setInt(8, mesai.getMolaSuresi());
            statement.setDouble(9, mesai.getToplamMesaiSaati());
            statement.setString(10, mesai.getMesaiNedeni());
            statement.setString(11, mesai.getYapilanIs());
            statement.setString(12, mesai.getProjeOperasyon());
            statement.setString(13, mesai.getSubeDepo());
            statement.setString(14, mesai.getAciklama());
            statement.setDouble(15, mesai.getAylikUcret());
            statement.setInt(16, mesai.getAylikCalismaSaati());
            statement.setDouble(17, mesai.getMesaiKatsayisi());
            statement.setDouble(18, mesai.getNormalSaatlikUcret());
            statement.setDouble(19, mesai.getMesaiSaatUcreti());
            statement.setDouble(20, mesai.getToplamMesaiUcreti());
            statement.setString(21, mesai.getRotaPlaniVarMi());
            statement.setString(22, mesai.getSiraliTeslimat());
            statement.setString(23, mesai.getTeslimatSorunu());
            statement.setString(24, mesai.getYerindeKapatma());
            statement.setString(25, mesai.getIrsaliyeAtfNo());
            statement.setString(26, mesai.getYonetici());
            statement.setString(27, mesai.getOnayDurumu());
            statement.setString(28, mesai.getOnayTarihi());
            statement.setString(29, mesai.getOdemeDurumu());
            statement.setString(30, mesai.getOdemeTarihi());
            statement.setString(31, mesai.getMesaiNo());

            int degisenSatirSayisi = statement.executeUpdate();
            return degisenSatirSayisi > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================
    // 5. MESAİ SİL
    // DELETE
    // =========================================

    public static boolean delete(String mesaiNo) {

        String sql = """
                DELETE FROM mesai
                WHERE mesai_no = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, mesaiNo);

            int silinenSatirSayisi = statement.executeUpdate();
            return silinenSatirSayisi > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================
    // RESULTSET SATIRINI MESAİ NESNESİNE ÇEVİR
    // =========================================

    private static Mesai resultSetToMesai(ResultSet resultSet) throws SQLException {

        return new Mesai(
                resultSet.getString("mesai_no"),
                resultSet.getString("sicil_no"),
                resultSet.getString("ad_soyad"),
                resultSet.getString("departman"),
                resultSet.getString("pozisyon"),
                resultSet.getString("mesai_tarihi"),
                resultSet.getString("baslangic_saati"),
                resultSet.getString("bitis_saati"),
                resultSet.getInt("mola_suresi"),
                resultSet.getDouble("toplam_mesai_saati"),
                resultSet.getString("mesai_nedeni"),
                resultSet.getString("yapilan_is"),
                resultSet.getString("proje_operasyon"),
                resultSet.getString("sube_depo"),
                resultSet.getString("aciklama"),
                resultSet.getDouble("aylik_ucret"),
                resultSet.getInt("aylik_calisma_saati"),
                resultSet.getDouble("mesai_katsayisi"),
                resultSet.getDouble("normal_saatlik_ucret"),
                resultSet.getDouble("mesai_saat_ucreti"),
                resultSet.getDouble("toplam_mesai_ucreti"),
                resultSet.getString("rota_plani_var_mi"),
                resultSet.getString("sirali_teslimat"),
                resultSet.getString("teslimat_sorunu"),
                resultSet.getString("yerinde_kapatma"),
                resultSet.getString("irsaliye_atf_no"),
                resultSet.getString("yonetici"),
                resultSet.getString("onay_durumu"),
                resultSet.getString("onay_tarihi"),
                resultSet.getString("odeme_durumu"),
                resultSet.getString("odeme_tarihi")
        );
    }
}