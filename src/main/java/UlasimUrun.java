public class UlasimUrun {
    private int id;
    private String plaka;
    private String urunKodu;
    private String urunAdi;
    private int miktar;
    private String koliNo;
    private String teslimDurumu;

    public UlasimUrun() {
    }

    public UlasimUrun(
            int id,
            String plaka,
            String urunKodu,
            String urunAdi,
            int miktar,
            String koliNo,
            String teslimDurumu
    ) {

        this.id = id;
        this.plaka = plaka;
        this.urunKodu = urunKodu;
        this.urunAdi = urunAdi;
        this.miktar = miktar;
        this.koliNo = koliNo;
        this.teslimDurumu = teslimDurumu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getUrunKodu() {
        return urunKodu;
    }

    public void setUrunKodu(String urunKodu) {
        this.urunKodu = urunKodu;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public String getKoliNo() {
        return koliNo;
    }

    public void setKoliNo(String koliNo) {
        this.koliNo = koliNo;
    }

    public String getTeslimDurumu() {
        return teslimDurumu;
    }

    public void setTeslimDurumu(String teslimDurumu) {
        this.teslimDurumu = teslimDurumu;
    }

}
