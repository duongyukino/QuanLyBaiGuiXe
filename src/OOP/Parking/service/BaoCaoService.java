package OOP.Parking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface BaoCaoService {
    // Báo cáo cũ (giữ lại nếu cần hoặc xóa đi)
    Map<String, Double> baoCaoDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay);
    Map<String, Integer> baoCaoLuuLuong(LocalDateTime tuNgay, LocalDateTime denNgay);
    Map<String, Integer> baoCaoHienTrang();

    // --- Báo cáo mới chi tiết ---
    
    /**
     * Báo cáo doanh thu vé ngày trong một ngày cụ thể
     * Key: "Xe Máy", "Ô tô"
     */
    Map<String, Double> baoCaoDoanhThuNgay(LocalDate date);

    /**
     * Báo cáo doanh thu tổng hợp trong một tháng
     * Key: 
     * - "VE_NGAY_XEMAY", "VE_NGAY_OTO"
     * - "VE_THANG_XEMAY", "VE_THANG_OTO" (Bao gồm đăng ký mới + gia hạn)
     * - "TONG_XEMAY", "TONG_OTO"
     * - "TONG_CONG"
     */
    Map<String, Double> baoCaoDoanhThuThang(int month, int year);
}