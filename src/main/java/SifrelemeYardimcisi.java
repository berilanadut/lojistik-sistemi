import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Kullanıcı şifrelerinin güvenli bir şekilde hash'lenmesi ve doğrulanması için yardımcı sınıf.
 */
public class SifrelemeYardimcisi {

    /**
     * Şifreyi, kullanıcı adı ve sabit bir tuz (pepper) kullanarak SHA-256 ile özetler (hash'ler).
     *
     * @param sifre Düz metin halindeki şifre
     * @param kullaniciAdi Kullanıcı adı (tuzlama için kullanılır)
     * @return 64 karakterli hex dizesi formatında hash
     */
    public static String sifreyiHashle(String sifre, String kullaniciAdi) {
        if (sifre == null) {
            return null;
        }
        try {
            // Gökkuşağı tablolarına karşı koruma sağlamak için kullanıcı adı ve sabit bir tuz birleştirilir.
            String tuz = kullaniciAdi + "Lojistik";
            MessageDigest ozetOlusturucu = MessageDigest.getInstance("SHA-256");
            
            // Tuz ve şifre birleştirilerek özet çıkartılır
            String birlesikMetin = tuz + sifre;
            byte[] hashDegeri = ozetOlusturucu.digest(birlesikMetin.getBytes(StandardCharsets.UTF_8));
            
            // Byte dizisini okunabilir Hex dizesine dönüştür
            StringBuilder hexDizesi = new StringBuilder();
            for (byte veriByte : hashDegeri) {
                String hexKarakter = Integer.toHexString(0xff & veriByte);
                if (hexKarakter.length() == 1) {
                    hexDizesi.append('0');
                }
                hexDizesi.append(hexKarakter);
            }
            return hexDizesi.toString();
        } catch (NoSuchAlgorithmException hata) {
            throw new RuntimeException("SHA-256 algoritması sistemde bulunamadı!", hata);
        }
    }

    /**
     * Girilen şifrenin, veritabanında kayıtlı olan hash ile uyuşup uyuşmadığını doğrular.
     *
     * @param girilenSifre Kullanıcının giriş yaparken yazdığı şifre
     * @param kullaniciAdi Kullanıcı adı
     * @param kayitliHash Veritabanından çekilen hash'li şifre
     * @return Doğrulama başarılı ise true, aksi halde false
     */
    public static boolean sifreyiDogrula(String girilenSifre, String kullaniciAdi, String kayitliHash) {
        if (girilenSifre == null || kayitliHash == null) {
            return false;
        }
        String yeniHash = sifreyiHashle(girilenSifre, kullaniciAdi);
        return yeniHash.equalsIgnoreCase(kayitliHash);
    }
}
