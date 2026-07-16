public class Urun {

    private String urunKodu;
    private String urunAdi;
    private String kategori;
    private String marka;
    private String tedarikci;
    private String depo;
    private String rafNo;
    private String birim;
    private int stokMiktari;
    private int kritikLimit;
    private String girisTarihi;


    public Urun() {

    }


    public Urun(
            String urunKodu,
            String urunAdi,
            String kategori,
            String marka,
            String tedarikci,
            String depo,
            String rafNo,
            String birim,
            int stokMiktari,
            int kritikLimit,
            String girisTarihi
    ) {

        this.urunKodu = urunKodu;
        this.urunAdi = urunAdi;
        this.kategori = kategori;
        this.marka = marka;
        this.tedarikci = tedarikci;
        this.depo = depo;
        this.rafNo = rafNo;
        this.birim = birim;
        this.stokMiktari = stokMiktari;
        this.kritikLimit = kritikLimit;
        this.girisTarihi = girisTarihi;
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

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getTedarikci() {
        return tedarikci;
    }

    public void setTedarikci(String tedarikci) {
        this.tedarikci = tedarikci;
    }

    public String getDepo() {
        return depo;
    }

    public void setDepo(String depo) {
        this.depo = depo;
    }

    public String getRafNo() {
        return rafNo;
    }

    public void setRafNo(String rafNo) {
        this.rafNo = rafNo;
    }

    public String getBirim() {
        return birim;
    }

    public void setBirim(String birim) {
        this.birim = birim;
    }

    public int getStokMiktari() {
        return stokMiktari;
    }

    public void setStokMiktari(int stokMiktari) {
        this.stokMiktari = stokMiktari;
    }

    public int getKritikLimit() {
        return kritikLimit;
    }

    public void setKritikLimit(int kritikLimit) {
        this.kritikLimit = kritikLimit;
    }

    public String getGirisTarihi() {
        return girisTarihi;
    }

    public void setGirisTarihi(String girisTarihi) {
        this.girisTarihi = girisTarihi;
    }
}