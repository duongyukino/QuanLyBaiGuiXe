package OOP.Parking.model;

public class XeMay extends PhuongTien {
    public XeMay(String bienSo) {
        super(bienSo, "Xe Máy", new XeMayPricingStrategy());
    }

    public XeMay(String bienSo, String chuXe) {
        super(bienSo, "Xe Máy", new XeMayPricingStrategy());
        this.chuXe = chuXe;
    }
}