public class StokHareket {
    private int id;
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

    private String islemTuru;
    private int miktar;

    private int oncekiStok;
    private int yeniStok;

    private String islemTarihi;

    public StokHareket() {

    }

    public StokHareket(int id,
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
                       String girisTarihi,
                       String islemTuru,
                       int miktar,
                       int oncekiStok,
                       int yeniStok,
                       String islemTarihi) {
        this.id = id;
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
        this.islemTuru = islemTuru;
        this.miktar = miktar;
        this.oncekiStok = oncekiStok;
        this.yeniStok = yeniStok;
        this.islemTarihi = islemTarihi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getIslemTuru() {
        return islemTuru;
    }

    public void setIslemTuru(String islemTuru) {
        this.islemTuru = islemTuru;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public int getOncekiStok() {
        return oncekiStok;
    }

    public void setOncekiStok(int oncekiStok) {
        this.oncekiStok = oncekiStok;
    }

    public int getYeniStok() {
        return yeniStok;
    }

    public void setYeniStok(int yeniStok) {
        this.yeniStok = yeniStok;
    }

    public String getIslemTarihi() {
        return islemTarihi;
    }

    public void setIslemTarihi(String islemTarihi) {
        this.islemTarihi = islemTarihi;
    }
}
