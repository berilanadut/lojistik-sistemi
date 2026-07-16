public class Mesai {

    private String mesaiNo;
    private String sicilNo;
    private String adSoyad;
    private String departman;
    private String pozisyon;

    private String mesaiTarihi;
    private String baslangicSaati;
    private String bitisSaati;
    private int molaSuresi;
    private double toplamMesaiSaati;

    private String mesaiNedeni;
    private String yapilanIs;
    private String projeOperasyon;
    private String subeDepo;
    private String aciklama;

    private double aylikUcret;
    private int aylikCalismaSaati;
    private double mesaiKatsayisi;
    private double normalSaatlikUcret;
    private double mesaiSaatUcreti;
    private double toplamMesaiUcreti;

    private String rotaPlaniVarMi;
    private String siraliTeslimat;
    private String teslimatSorunu;
    private String yerindeKapatma;
    private String irsaliyeAtfNo;

    private String yonetici;
    private String onayDurumu;
    private String onayTarihi;
    private String odemeDurumu;
    private String odemeTarihi;

    public Mesai() {


    }

    public Mesai(String mesaiNo,
                 String sicilNo,
                 String adSoyad,
                 String departman,
                 String pozisyon,
                 String mesaiTarihi,
                 String baslangicSaati,
                 String bitisSaati,
                 int molaSuresi,
                 double toplamMesaiSaati,
                 String mesaiNedeni,
                 String yapilanIs,
                 String projeOperasyon,
                 String subeDepo,
                 String aciklama,
                 double aylikUcret,
                 int aylikCalismaSaati,
                 double mesaiKatsayisi,
                 double normalSaatlikUcret,
                 double mesaiSaatUcreti,
                 double toplamMesaiUcreti,
                 String rotaPlaniVarMi,
                 String siraliTeslimat,
                 String teslimatSorunu,
                 String yerindeKapatma,
                 String irsaliyeAtfNo,
                 String yonetici,
                 String onayDurumu,
                 String onayTarihi,
                 String odemeDurumu,
                 String odemeTarihi) {
        this.mesaiNo = mesaiNo;
        this.sicilNo = sicilNo;
        this.adSoyad = adSoyad;
        this.departman = departman;
        this.pozisyon = pozisyon;
        this.mesaiTarihi = mesaiTarihi;
        this.baslangicSaati = baslangicSaati;
        this.bitisSaati = bitisSaati;
        this.molaSuresi = molaSuresi;
        this.toplamMesaiSaati = toplamMesaiSaati;
        this.mesaiNedeni = mesaiNedeni;
        this.yapilanIs = yapilanIs;
        this.projeOperasyon = projeOperasyon;
        this.subeDepo = subeDepo;
        this.aciklama = aciklama;
        this.aylikUcret = aylikUcret;
        this.aylikCalismaSaati = aylikCalismaSaati;
        this.mesaiKatsayisi = mesaiKatsayisi;
        this.normalSaatlikUcret = normalSaatlikUcret;
        this.mesaiSaatUcreti = mesaiSaatUcreti;
        this.toplamMesaiUcreti = toplamMesaiUcreti;
        this.rotaPlaniVarMi = rotaPlaniVarMi;
        this.siraliTeslimat = siraliTeslimat;
        this.teslimatSorunu = teslimatSorunu;
        this.yerindeKapatma = yerindeKapatma;
        this.irsaliyeAtfNo = irsaliyeAtfNo;
        this.yonetici = yonetici;
        this.onayDurumu = onayDurumu;
        this.onayTarihi = onayTarihi;
        this.odemeDurumu = odemeDurumu;
        this.odemeTarihi = odemeTarihi;
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

    public String getBaslangicSaati() {
        return baslangicSaati;
    }

    public void setBaslangicSaati(String baslangicSaati) {
        this.baslangicSaati = baslangicSaati;
    }

    public String getBitisSaati() {
        return bitisSaati;
    }

    public void setBitisSaati(String bitisSaati) {
        this.bitisSaati = bitisSaati;
    }

    public int getMolaSuresi() {
        return molaSuresi;
    }

    public void setMolaSuresi(int molaSuresi) {
        this.molaSuresi = molaSuresi;
    }

    public double getToplamMesaiSaati() {
        return toplamMesaiSaati;
    }

    public void setToplamMesaiSaati(double toplamMesaiSaati) {
        this.toplamMesaiSaati = toplamMesaiSaati;
    }

    public String getMesaiNedeni() {
        return mesaiNedeni;
    }

    public void setMesaiNedeni(String mesaiNedeni) {
        this.mesaiNedeni = mesaiNedeni;
    }

    public String getYapilanIs() {
        return yapilanIs;
    }

    public void setYapilanIs(String yapilanIs) {
        this.yapilanIs = yapilanIs;
    }

    public String getProjeOperasyon() {
        return projeOperasyon;
    }

    public void setProjeOperasyon(String projeOperasyon) {
        this.projeOperasyon = projeOperasyon;
    }

    public String getSubeDepo() {
        return subeDepo;
    }

    public void setSubeDepo(String subeDepo) {
        this.subeDepo = subeDepo;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public double getAylikUcret() {
        return aylikUcret;
    }

    public void setAylikUcret(double aylikUcret) {
        this.aylikUcret = aylikUcret;
    }

    public int getAylikCalismaSaati() {
        return aylikCalismaSaati;
    }

    public void setAylikCalismaSaati(int aylikCalismaSaati) {
        this.aylikCalismaSaati = aylikCalismaSaati;
    }

    public double getMesaiKatsayisi() {
        return mesaiKatsayisi;
    }

    public void setMesaiKatsayisi(double mesaiKatsayisi) {
        this.mesaiKatsayisi = mesaiKatsayisi;
    }

    public double getNormalSaatlikUcret() {
        return normalSaatlikUcret;
    }

    public void setNormalSaatlikUcret(double normalSaatlikUcret) {
        this.normalSaatlikUcret = normalSaatlikUcret;
    }

    public double getMesaiSaatUcreti() {
        return mesaiSaatUcreti;
    }

    public void setMesaiSaatUcreti(double mesaiSaatUcreti) {
        this.mesaiSaatUcreti = mesaiSaatUcreti;
    }

    public double getToplamMesaiUcreti() {
        return toplamMesaiUcreti;
    }

    public void setToplamMesaiUcreti(double toplamMesaiUcreti) {
        this.toplamMesaiUcreti = toplamMesaiUcreti;
    }

    public String getRotaPlaniVarMi() {
        return rotaPlaniVarMi;
    }

    public void setRotaPlaniVarMi(String rotaPlaniVarMi) {
        this.rotaPlaniVarMi = rotaPlaniVarMi;
    }

    public String getSiraliTeslimat() {
        return siraliTeslimat;
    }

    public void setSiraliTeslimat(String siraliTeslimat) {
        this.siraliTeslimat = siraliTeslimat;
    }

    public String getTeslimatSorunu() {
        return teslimatSorunu;
    }

    public void setTeslimatSorunu(String teslimatSorunu) {
        this.teslimatSorunu = teslimatSorunu;
    }

    public String getYerindeKapatma() {
        return yerindeKapatma;
    }

    public void setYerindeKapatma(String yerindeKapatma) {
        this.yerindeKapatma = yerindeKapatma;
    }

    public String getIrsaliyeAtfNo() {
        return irsaliyeAtfNo;
    }

    public void setIrsaliyeAtfNo(String irsaliyeAtfNo) {
        this.irsaliyeAtfNo = irsaliyeAtfNo;
    }

    public String getYonetici() {
        return yonetici;
    }

    public void setYonetici(String yonetici) {
        this.yonetici = yonetici;
    }

    public String getOnayDurumu() {
        return onayDurumu;
    }

    public void setOnayDurumu(String onayDurumu) {
        this.onayDurumu = onayDurumu;
    }

    public String getOnayTarihi() {
        return onayTarihi;
    }

    public void setOnayTarihi(String onayTarihi) {
        this.onayTarihi = onayTarihi;
    }

    public String getOdemeDurumu() {
        return odemeDurumu;
    }

    public void setOdemeDurumu(String odemeDurumu) {
        this.odemeDurumu = odemeDurumu;
    }

    public String getOdemeTarihi() {
        return odemeTarihi;
    }

    public void setOdemeTarihi(String odemeTarihi) {
        this.odemeTarihi = odemeTarihi;
    }
}