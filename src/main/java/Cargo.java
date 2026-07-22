public class Cargo {
    public String getKargoNo() {
        return kargoNo;
    }
    public String getGonderici() {
        return gonderici;
    }
    public String getAlici() {
        return alici;
    }
    public String getGondericiSube() {
        return gondericiSube;
    }
    public String getTeslimatSube() {
        return teslimatSube;
    }
    public double getDesi() {
        return desi;
    }
    public double getAgirlik() {
        return agirlik;
    }
    public String getDurum() {
        return durum;
    }
    public String getVerilisTarihi() {
        return verilisTarihi;
    }
    public String getTahminiTeslim() {
        return tahminiTeslim;
    }
    public String getTeslimTarihi() {
        return teslimTarihi;
    }
    public String getPlaka() {
        return plaka;
    }
    public String getSurucu() {
        return surucu;
    }
    public String getTakipNotu() {
        return takipNotu;
    }
    private String kargoNo;
    private String gonderici;
    private String alici;
    private String gondericiSube;
    private String teslimatSube;
    private double desi;
    private double agirlik;
    private String durum;
    private String verilisTarihi;
    private String tahminiTeslim;
    private String teslimTarihi;
    private String plaka;
    private String surucu;
    private String takipNotu;

    public Cargo(String kargoNo,
                 String gonderici,
                 String alici,
                 String gondericiSube,
                 String teslimatSube,
                 double desi,
                 double agirlik,
                 String durum,
                 String verilisTarihi,
                 String tahminiTeslim,
                 String teslimTarihi,
                 String plaka,
                 String surucu,
                 String takipNotu) {

        this.kargoNo = kargoNo;
        this.gonderici = gonderici;
        this.alici = alici;
        this.gondericiSube = gondericiSube;
        this.teslimatSube = teslimatSube;
        this.desi = desi;
        this.agirlik = agirlik;
        this.durum = durum;
        this.verilisTarihi = verilisTarihi;
        this.tahminiTeslim = tahminiTeslim;
        this.teslimTarihi = teslimTarihi;
        this.plaka = plaka;
        this.surucu = surucu;
        this.takipNotu = takipNotu;

    }
}
