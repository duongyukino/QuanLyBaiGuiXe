package OOP.Parking.service.impl;

import OOP.Parking.DAO.*;
import OOP.Parking.model.*;
import OOP.Parking.service.ParkingService;
import OOP.Parking.model.LichSu;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ParkingServiceImpl implements ParkingService {
    private final PhuongTienDAO phuongTienDAO;
    private final VeXeDAO veXeDAO;
    private final LichSuDAO lichSuDAO;
    private static final int TONG_CHO = 100;

    public ParkingServiceImpl() throws IOException {
        this.phuongTienDAO = PhuongTienDAO.getInstance();
        this.veXeDAO = VeXeDAO.getInstance();
        this.lichSuDAO = LichSuDAO.getInstance();
    }

    // Dependency Injection cho unit testing
    public ParkingServiceImpl(PhuongTienDAO phuongTienDAO, VeXeDAO veXeDAO, LichSuDAO lichSuDAO) {
        this.phuongTienDAO = phuongTienDAO;
        this.veXeDAO = veXeDAO;
        this.lichSuDAO = lichSuDAO;
    }

    @Override
    public boolean xeVao(PhuongTien xe) {
        try {
            if (phuongTienDAO.getAll().size() >= TONG_CHO) {
                return false;
            }

            // Kiểm tra vé tháng
            VeXe veThang = veXeDAO.findByBienSo(xe.getBienSo());
            if (veThang != null) {
                xe.setMaVe(veThang.getMaVe());
            }

            boolean success = phuongTienDAO.add(xe);
            if (success) {
                // Ghi log
                LichSu log = new LichSu(
                        UUID.randomUUID().toString(),
                        "XE_VAO",
                        xe.getBienSo(),
                        "Xe " + xe.getLoaiXe() + " vào bãi",
                        0,
                        LocalDateTime.now(),
                        "SYSTEM" // Default user
                );
                lichSuDAO.log(log);
            }
            return success;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double xeRa(String bienSo, LocalDateTime thoiGianRa) {
        try {
            PhuongTien xe = phuongTienDAO.getById(bienSo);
            if (xe == null) return 0;

            double phi = 0;

            // Nếu có vé tháng còn hạn thì miễn phí
            if (xe.getMaVe() == null) {
                phi = xe.tinhPhi(thoiGianRa);
            } else {
                VeXe ve = veXeDAO.getById(xe.getMaVe());
                if (ve == null || !ve.conHan()) {
                    phi = xe.tinhPhi(thoiGianRa);
                }
            }

            // Xóa xe khỏi bãi
            phuongTienDAO.delete(bienSo);

            // Ghi log
            LichSu log = new LichSu(
                    UUID.randomUUID().toString(),
                    "XE_RA",
                    bienSo,
                    "Xe " + xe.getLoaiXe() + " ra bãi",
                    phi,
                    thoiGianRa,
                    "SYSTEM" // Default user
            );
            lichSuDAO.log(log);

            return phi;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public PhuongTien timXe(String bienSo) {
        try {
            return phuongTienDAO.getById(bienSo);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean dangKyVeThang(VeXe ve) {
        try {
            if (veXeDAO.getById(ve.getMaVe()) != null) {
                return false;
            }
            return veXeDAO.add(ve);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean giaHanVeThang(String maVe, int soThang) {
        try {
            VeXe ve = veXeDAO.getById(maVe);
            if (ve == null) return false;

            VeXe veMoi = new VeXe(
                    ve.getMaVe(),
                    ve.getBienSo(),
                    ve.getLoaiXe(),
                    ve.getLoaiVe(),
                    ve.getNgayHetHan().plusMonths(soThang),
                    ve.getChuXe(),
                    ve.getSoDienThoai()
            );
            return veXeDAO.update(veMoi);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public VeXe timVeThang(String maVe) {
        try {
            return veXeDAO.getById(maVe);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PhuongTien> getXeTrongBai() {
        try {
            return phuongTienDAO.getAll();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<VeXe> getDanhSachVeThang() {
        try {
            return veXeDAO.getAll();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Double> baoCaoDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay) {
        Map<String, Double> doanhThu = new HashMap<>();
        List<LichSu> lichSuList = lichSuDAO.getLichSuByDate(tuNgay, denNgay);
        
        for (LichSu ls : lichSuList) {
            if ("XE_RA".equals(ls.getLoaiThaoTac())) {
                // Assuming we can derive vehicle type from description or need to fetch it
                // For simplicity, let's just aggregate total revenue or use a generic key
                // Ideally, LichSu should store vehicle type or we fetch it.
                // Given current LichSu structure, we might not have vehicle type directly easily without parsing description or looking up
                // Let's just put it under "DOANH_THU" for now or parse description if it follows "Xe [Type] ra bãi"
                String loaiXe = "Unknown";
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
    public int kiemTraChoTrong() {
        try {
            return TONG_CHO - phuongTienDAO.getAll().size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean kiemTraVeHopLe(String maVe) {
        try {
            VeXe ve = veXeDAO.getById(maVe);
            return ve != null && ve.conHan();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void checkIn(XeMay xe) {
        xeVao(xe);
    }
}