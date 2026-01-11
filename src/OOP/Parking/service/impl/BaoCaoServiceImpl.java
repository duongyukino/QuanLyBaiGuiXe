package OOP.Parking.service.impl;

import OOP.Parking.DAO.LichSuDAO;
import OOP.Parking.DAO.PhuongTienDAO;
import OOP.Parking.model.LichSu;
import OOP.Parking.service.BaoCaoService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaoCaoServiceImpl implements BaoCaoService {
    private final LichSuDAO lichSuDAO;
    private final PhuongTienDAO phuongTienDAO;

    public BaoCaoServiceImpl() throws IOException {
        this.lichSuDAO = LichSuDAO.getInstance();
        this.phuongTienDAO = PhuongTienDAO.getInstance();
    }

    @Override
    public Map<String, Double> baoCaoDoanhThuNgay(LocalDate date) {
        Map<String, Double> result = new HashMap<>();
        result.put("Xe Máy", 0.0);
        result.put("Ô tô", 0.0);

        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
        List<LichSu> list = lichSuDAO.getLichSuByDate(start, end);

        for (LichSu ls : list) {
            if ("XE_RA".equals(ls.getLoaiThaoTac())) {
                if ("Xe Máy".equals(ls.getLoaiXe())) {
                    result.put("Xe Máy", result.get("Xe Máy") + ls.getPhi());
                } else if ("Ô tô".equals(ls.getLoaiXe())) {
                    result.put("Ô tô", result.get("Ô tô") + ls.getPhi());
                }
            }
        }
        return result;
    }

    @Override
    public Map<String, Double> baoCaoDoanhThuThang(int month, int year) {
        Map<String, Double> result = new HashMap<>();
        result.put("VE_NGAY_XEMAY", 0.0);
        result.put("VE_NGAY_OTO", 0.0);
        result.put("VE_THANG_XEMAY", 0.0);
        result.put("VE_THANG_OTO", 0.0);
        
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        LocalDateTime start = LocalDateTime.of(firstDay, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(lastDay, LocalTime.MAX);

        List<LichSu> list = lichSuDAO.getLichSuByDate(start, end);

        for (LichSu ls : list) {
            boolean isXeMay = "Xe Máy".equals(ls.getLoaiXe());
            boolean isOto = "Ô tô".equals(ls.getLoaiXe());

            if ("XE_RA".equals(ls.getLoaiThaoTac())) {
                if (isXeMay) result.put("VE_NGAY_XEMAY", result.get("VE_NGAY_XEMAY") + ls.getPhi());
                if (isOto) result.put("VE_NGAY_OTO", result.get("VE_NGAY_OTO") + ls.getPhi());
            } 
            else if ("BAN_VE_THANG".equals(ls.getLoaiThaoTac()) || "GIA_HAN_VE".equals(ls.getLoaiThaoTac())) {
                if (isXeMay) result.put("VE_THANG_XEMAY", result.get("VE_THANG_XEMAY") + ls.getPhi());
                if (isOto) result.put("VE_THANG_OTO", result.get("VE_THANG_OTO") + ls.getPhi());
            }
        }

        double tongXeMay = result.get("VE_NGAY_XEMAY") + result.get("VE_THANG_XEMAY");
        double tongOto = result.get("VE_NGAY_OTO") + result.get("VE_THANG_OTO");
        
        result.put("TONG_XEMAY", tongXeMay);
        result.put("TONG_OTO", tongOto);
        result.put("TONG_CONG", tongXeMay + tongOto);

        return result;
    }

    // --- Deprecated methods ---
    @Override
    public Map<String, Double> baoCaoDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Integer> baoCaoLuuLuong(LocalDateTime tuNgay, LocalDateTime denNgay) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Integer> baoCaoHienTrang() {
        Map<String, Integer> hienTrang = new HashMap<>();
        try {
            hienTrang.put("Xe Máy", phuongTienDAO.countByType("Xe Máy"));
            hienTrang.put("Ô tô", phuongTienDAO.countByType("Ô tô"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hienTrang;
    }
}