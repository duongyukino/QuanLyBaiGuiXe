package OOP.Parking.model;

import java.time.LocalDateTime;

public class LichSu {
    private String id;
    private String loaiThaoTac; // Changed from action
    private String bienSo;
    private String chiTiet; // Changed from moTa
    private double phi;
    private LocalDateTime thoiGian;
    private String nguoiThucHien;

    public LichSu(String id, String loaiThaoTac, String bienSo, String chiTiet, double phi, LocalDateTime thoiGian, String nguoiThucHien) {
        this.id = id;
        this.loaiThaoTac = loaiThaoTac;
        this.bienSo = bienSo;
        this.chiTiet = chiTiet;
        this.phi = phi;
        this.thoiGian = thoiGian;
        this.nguoiThucHien = nguoiThucHien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoaiThaoTac() {
        return loaiThaoTac;
    }

    public void setLoaiThaoTac(String loaiThaoTac) {
        this.loaiThaoTac = loaiThaoTac;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getNguoiThucHien() {
        return nguoiThucHien;
    }

    public void setNguoiThucHien(String nguoiThucHien) {
        this.nguoiThucHien = nguoiThucHien;
    }
}