package OOP.Parking.model;

import java.time.LocalDateTime;

public class LichSu {
    private String loaiThaoTac; // XE_VAO, XE_RA, BAN_VE_THANG...
    private String bienSo;
    private String loaiXe;      // Thêm trường này: Xe Máy, Ô tô
    private LocalDateTime thoiGian;
    private double phi;

    public LichSu(String loaiThaoTac, String bienSo, String loaiXe, LocalDateTime thoiGian, double phi) {
        this.loaiThaoTac = loaiThaoTac;
        this.bienSo = bienSo;
        this.loaiXe = loaiXe;
        this.thoiGian = thoiGian;
        this.phi = phi;
    }

    public String getLoaiThaoTac() { return loaiThaoTac; }
    public void setLoaiThaoTac(String loaiThaoTac) { this.loaiThaoTac = loaiThaoTac; }

    public String getBienSo() { return bienSo; }
    public void setBienSo(String bienSo) { this.bienSo = bienSo; }

    public String getLoaiXe() { return loaiXe; }
    public void setLoaiXe(String loaiXe) { this.loaiXe = loaiXe; }

    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }

    public double getPhi() { return phi; }
    public void setPhi(double phi) { this.phi = phi; }
}