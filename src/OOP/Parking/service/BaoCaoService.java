package OOP.Parking.service;

import java.time.LocalDateTime;
import java.util.Map;

public interface BaoCaoService {
    /**
     * Báo cáo doanh thu theo loại xe trong khoảng thời gian
     */
    Map<String, Double> baoCaoDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay);

    /**
     * Báo cáo lưu lượng xe vào/ra trong khoảng thời gian
     */
    Map<String, Integer> baoCaoLuuLuong(LocalDateTime tuNgay, LocalDateTime denNgay);

    /**
     * Báo cáo số lượng xe hiện có trong bãi theo loại
     */
    Map<String, Integer> baoCaoHienTrang();
}