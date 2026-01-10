package OOP.Parking.model;

public class Oto extends PhuongTien {
    private int soCho;

    public Oto(String bienSo) {
        super(bienSo, "Ô tô", new OtoPricingStrategy());
    }

    public Oto(String bienSo, String chuXe, int soCho) {
        super(bienSo, "Ô tô", new OtoPricingStrategy());
        this.chuXe = chuXe;
        this.soCho = soCho;
    }

    public int getSoCho() {
        return soCho;
    }

    public void setSoCho(int soCho) {
        this.soCho = soCho;
    }
}