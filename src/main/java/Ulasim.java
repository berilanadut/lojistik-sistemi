public class Ulasim {

    private String plaka;
    private String surucu;
    private String baslangic;
    private String varis;
    private String rota;
    private String guncelKonum;
    private String baslangicZamani;
    private int tahminiSure;
    private double toplamMesafe;
    private double yakit;
    private String rotadanCikti;
    private String rotaDurumu;
    private String aciklama;

    public Ulasim() {

    }

    public Ulasim(String plaka,
                  String surucu,
                  String baslangic,
                  String varis,
                  String rota,
                  String guncelKonum,
                  String baslangicZamani,
                  int tahminiSure,
                  double toplamMesafe,
                  double yakit,
                  String rotadanCikti,
                  String rotaDurumu,
                  String aciklama) {
        this.plaka = plaka;
        this.surucu = surucu;
        this.baslangic = baslangic;
        this.varis = varis;
        this.rota = rota;
        this.guncelKonum = guncelKonum;
        this.baslangicZamani = baslangicZamani;
        this.tahminiSure = tahminiSure;
        this.toplamMesafe = toplamMesafe;
        this.yakit = yakit;
        this.rotadanCikti = rotadanCikti;
        this.rotaDurumu = rotaDurumu;
        this.aciklama = aciklama;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getSurucu() {
        return surucu;
    }

    public void setSurucu(String surucu) {
        this.surucu = surucu;
    }

    public String getBaslangic() {
        return baslangic;
    }

    public void setBaslangic(String baslangic) {
        this.baslangic = baslangic;
    }

    public String getVaris() {
        return varis;
    }

    public void setVaris(String varis) {
        this.varis = varis;
    }

    public String getRota() {
        return rota;
    }

    public void setRota(String rota) {
        this.rota = rota;
    }

    public String getGuncelKonum() {
        return guncelKonum;
    }

    public void setGuncelKonum(String guncelKonum) {
        this.guncelKonum = guncelKonum;
    }

    public String getBaslangicZamani() {
        return baslangicZamani;
    }

    public void setBaslangicZamani(String baslangicZamani) {
        this.baslangicZamani = baslangicZamani;
    }

    public int getTahminiSure() {
        return tahminiSure;
    }

    public void setTahminiSure(int tahminiSure) {
        this.tahminiSure = tahminiSure;
    }

    public double getToplamMesafe() {
        return toplamMesafe;
    }

    public void setToplamMesafe(double toplamMesafe) {
        this.toplamMesafe = toplamMesafe;
    }

    public double getYakit() {
        return yakit;
    }

    public void setYakit(double yakit) {
        this.yakit = yakit;
    }

    public String getRotadanCikti() {
        return rotadanCikti;
    }

    public void setRotadanCikti(String rotadanCikti) {
        this.rotadanCikti = rotadanCikti;
    }

    public String getRotaDurumu() {
        return rotaDurumu;
    }

    public void setRotaDurumu(String rotaDurumu) {
        this.rotaDurumu = rotaDurumu;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}