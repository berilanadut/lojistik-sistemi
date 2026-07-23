public class MesaiGecmis {
    private int id;
    private String islemTarihi;
    private String islemTuru;
    private String mesaiNo;
    private String sicilNo;
    private String adSoyad;
    private String departman;
    private String pozisyon;
    private String mesaiTarihi;
    private double toplamMesaiSaati;
    private double toplamMesaiUcreti;
    private String onayDurumu;
    private String odemeDurumu;
    // =========================================
    // BOŞ CONSTRUCTOR
    // =========================================

    public MesaiGecmis() {

    }
    // =========================================
    // DOLU CONSTRUCTOR
    // =========================================

    public MesaiGecmis(
            int id,
            String islemTarihi,
            String islemTuru,
            String mesaiNo,
            String sicilNo,
            String adSoyad,
            String departman,
            String pozisyon,
            String mesaiTarihi,
            double toplamMesaiSaati,
            double toplamMesaiUcreti,
            String onayDurumu,
            String odemeDurumu
    ) {

        this.id = id;
        this.islemTarihi = islemTarihi;
        this.islemTuru = islemTuru;
        this.mesaiNo = mesaiNo;
        this.sicilNo = sicilNo;
        this.adSoyad = adSoyad;
        this.departman = departman;
        this.pozisyon = pozisyon;
        this.mesaiTarihi = mesaiTarihi;
        this.toplamMesaiSaati = toplamMesaiSaati;
        this.toplamMesaiUcreti = toplamMesaiUcreti;
        this.onayDurumu = onayDurumu;
        this.odemeDurumu = odemeDurumu;
    }
    // =========================================
    // GETTER VE SETTER METOTLARI
    // =========================================

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getIslemTarihi() {

        return islemTarihi;
    }

    public void setIslemTarihi(String islemTarihi) {

        this.islemTarihi = islemTarihi;
    }

    public String getIslemTuru() {

        return islemTuru;
    }

    public void setIslemTuru(String islemTuru) {

        this.islemTuru = islemTuru;
    }

    public String getMesaiNo() {

        return mesaiNo;
    }

    public void setMesaiNo(String mesaiNo) {

        this.mesaiNo = mesaiNo;
    }

    public String getSicilNo() {

        return sicilNo;
    }

    public void setSicilNo(String sicilNo) {

        this.sicilNo = sicilNo;
    }

    public String getAdSoyad() {

        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {

        this.adSoyad = adSoyad;
    }

    public String getDepartman() {

        return departman;
    }

    public void setDepartman(String departman) {

        this.departman = departman;
    }

    public String getPozisyon() {

        return pozisyon;
    }

    public void setPozisyon(String pozisyon) {

        this.pozisyon = pozisyon;
    }

    public String getMesaiTarihi() {

        return mesaiTarihi;
    }

    public void setMesaiTarihi(String mesaiTarihi) {

        this.mesaiTarihi = mesaiTarihi;
    }

    public double getToplamMesaiSaati() {

        return toplamMesaiSaati;
    }

    public void setToplamMesaiSaati(double toplamMesaiSaati) {

        this.toplamMesaiSaati = toplamMesaiSaati;
    }

    public double getToplamMesaiUcreti() {

        return toplamMesaiUcreti;
    }

    public void setToplamMesaiUcreti(double toplamMesaiUcreti) {

        this.toplamMesaiUcreti = toplamMesaiUcreti;
    }

    public String getOnayDurumu() {

        return onayDurumu;
    }

    public void setOnayDurumu(String onayDurumu) {

        this.onayDurumu = onayDurumu;
    }

    public String getOdemeDurumu() {

        return odemeDurumu;
    }

    public void setOdemeDurumu(String odemeDurumu) {

        this.odemeDurumu = odemeDurumu;
    }

}
