package OOP.Parking.model;

import java.time.LocalDate;

public class VeXe {
    private String maVe;
    private String bienSo;
    private String loaiXe;
    private String loaiVe;
    private LocalDate ngayDangKy;
    private LocalDate ngayHetHan;
    private String chuXe;
    private String soDienThoai;

    public VeXe(String maVe, String bienSo, String loaiXe, String loaiVe,
                LocalDate ngayHetHan, String chuXe, String soDienThoai) {
        this.maVe = maVe;
        this.bienSo = bienSo;
        this.loaiXe = loaiXe;
        this.loaiVe = loaiVe;
        this.ngayDangKy = LocalDate.now();
        this.ngayHetHan = ngayHetHan;
        this.chuXe = chuXe;
        this.soDienThoai = soDienThoai;
    }

    public VeXe(String maVe, String bienSo, String loaiXe, String loaiVe,
                LocalDate ngayDangKy, LocalDate ngayHetHan, String chuXe, String soDienThoai) {
        this.maVe = maVe;
        this.bienSo = bienSo;
        this.loaiXe = loaiXe;
        this.loaiVe = loaiVe;
        this.ngayDangKy = ngayDangKy;
        this.ngayHetHan = ngayHetHan;
        this.chuXe = chuXe;
        this.soDienThoai = soDienThoai;
    }

    // Getters and setters
    public String getMaVe() { return maVe; }
    public String getBienSo() { return bienSo; }
    public String getLoaiXe() { return loaiXe; }
    public String getLoaiVe() { return loaiVe; }
    public LocalDate getNgayDangKy() { return ngayDangKy; }
    public LocalDate getNgayHetHan() { return ngayHetHan; }
    public String getChuXe() { return chuXe; }
    public String getSoDienThoai() { return soDienThoai; }

    public boolean conHan() {
        return LocalDate.now().isBefore(ngayHetHan) ||
                LocalDate.now().isEqual(ngayHetHan);
    }
}