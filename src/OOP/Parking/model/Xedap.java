package OOP.Parking.model;

public class Xedap extends PhuongTien {
    public Xedap(String bienSo) {
        super(bienSo, "Xe Đạp", new XeDapPricingStrategy());
    }

    public Xedap(String bienSo, String chuXe) {
        super(bienSo, "Xe Đạp", new XeDapPricingStrategy());
        this.chuXe = chuXe;
    }
}