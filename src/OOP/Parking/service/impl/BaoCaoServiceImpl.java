package OOP.Parking.service.impl;

import OOP.Parking.DAO.LichSuDAO;
import OOP.Parking.DAO.PhuongTienDAO;
import OOP.Parking.model.LichSu;
import OOP.Parking.service.BaoCaoService;

import java.io.IOException;
import java.time.LocalDateTime;
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
    public Map<String, Double> baoCaoDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay) {
        Map<String, Double> doanhThu = new HashMap<>();
        List<LichSu> lichSuList = lichSuDAO.getLichSuByDate(tuNgay, denNgay);

        for (LichSu ls : lichSuList) {
            if ("XE_RA".equals(ls.getLoaiThaoTac())) {
                // Phân loại xe dựa trên chi tiết hoặc cần thêm trường loại xe vào LichSu
                // Tạm thời giả định chi tiết có chứa tên loại xe
                String loaiXe = "Khác";
                if (ls.getChiTiet().contains("Xe Máy")) loaiXe = "Xe Máy";
                else if (ls.getChiTiet().contains("Ô tô")) loaiXe = "Ô tô";
                else if (ls.getChiTiet().contains("Xe Đạp")) loaiXe = "Xe Đạp";

                doanhThu.put(loaiXe, doanhThu.getOrDefault(loaiXe, 0.0) + ls.getPhi());
            }
        }
        return doanhThu;
    }

    @Override
    public Map<String, Integer> baoCaoLuuLuong(LocalDateTime tuNgay, LocalDateTime denNgay) {
        Map<String, Integer> luuLuong = new HashMap<>();
        List<LichSu> lichSuList = lichSuDAO.getLichSuByDate(tuNgay, denNgay);

        int vao = 0;
        int ra = 0;

        for (LichSu ls : lichSuList) {
            if ("XE_VAO".equals(ls.getLoaiThaoTac())) {
                vao++;
            } else if ("XE_RA".equals(ls.getLoaiThaoTac())) {
                ra++;
            }
        }

        luuLuong.put("VAO", vao);
        luuLuong.put("RA", ra);
        return luuLuong;
    }

    @Override
    public Map<String, Integer> baoCaoHienTrang() {
        Map<String, Integer> hienTrang = new HashMap<>();
        try {
            hienTrang.put("Xe Máy", phuongTienDAO.countByType("Xe Máy"));
            hienTrang.put("Ô tô", phuongTienDAO.countByType("Ô tô"));
            hienTrang.put("Xe Đạp", phuongTienDAO.countByType("Xe Đạp"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hienTrang;
    }
}