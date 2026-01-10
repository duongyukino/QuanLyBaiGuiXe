package OOP.Parking.model;

import java.time.LocalDateTime;

public abstract class PhuongTien {
    protected String bienSo;
    protected String loaiXe;
    protected LocalDateTime thoiGianVao;
    protected String maVe;
    protected String chuXe;
    protected PricingStrategy pricingStrategy; // Strategy Pattern

    public PhuongTien(String bienSo, String loaiXe, PricingStrategy pricingStrategy) {
        this.bienSo = bienSo;
        this.loaiXe = loaiXe;
        this.thoiGianVao = LocalDateTime.now();
        this.pricingStrategy = pricingStrategy;
    }

    // Getters and setters
    public String getBienSo() { return bienSo; }
    public String getLoaiXe() { return loaiXe; }
    public LocalDateTime getThoiGianVao() { return thoiGianVao; }
    public void setThoiGianVao(LocalDateTime thoiGianVao) { this.thoiGianVao = thoiGianVao; }
    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }
    public String getChuXe() { return chuXe; }
    public void setChuXe(String chuXe) { this.chuXe = chuXe; }
    
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double tinhPhi(LocalDateTime thoiGianRa) {
        if (pricingStrategy == null) return 0;
        return pricingStrategy.calculateFee(thoiGianVao, thoiGianRa);
    }
}