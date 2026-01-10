package OOP.Parking.service;

import OOP.Parking.model.PhuongTien;
import OOP.Parking.model.VeXe;
import OOP.Parking.model.XeMay;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ParkingService {
    // Quản lý xe vào/ra
    boolean xeVao(PhuongTien xe);
    double xeRa(String bienSo, LocalDateTime thoiGianRa);
    PhuongTien timXe(String bienSo);

    // Quản lý vé tháng
    boolean dangKyVeThang(VeXe ve);
    boolean giaHanVeThang(String maVe, int soThang);
    VeXe timVeThang(String maVe);

    // Tra cứu
    List<PhuongTien> getXeTrongBai();
    List<VeXe> getDanhSachVeThang();

    // Báo cáo
    Map<String, Double> baoCaoDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay);
    Map<String, Integer> baoCaoLuuLuong(LocalDateTime tuNgay, LocalDateTime denNgay);

    // Kiểm tra
    int kiemTraChoTrong();
    boolean kiemTraVeHopLe(String maVe);

    void checkIn(XeMay xe);
}