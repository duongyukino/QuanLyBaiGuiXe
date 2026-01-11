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
    
    private static final double GIA_VE_THANG_XEMAY = 100000;
    private static final double GIA_VE_THANG_OTO = 500000;

    public ParkingServiceImpl() throws IOException {
        this.phuongTienDAO = PhuongTienDAO.getInstance();
        this.veXeDAO = VeXeDAO.getInstance();
        this.lichSuDAO = LichSuDAO.getInstance();
    }

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

            VeXe veThang = veXeDAO.findByBienSo(xe.getBienSo());
            if (veThang != null) {
                xe.setMaVe(veThang.getMaVe());
            }

            boolean success = phuongTienDAO.add(xe);
            if (success) {
                LichSu log = new LichSu(
                        "XE_VAO",
                        xe.getBienSo(),
                        xe.getLoaiXe(),
                        LocalDateTime.now(),
                        0
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

            if (xe.getMaVe() == null) {
                phi = xe.tinhPhi(thoiGianRa);
            } else {
                VeXe ve = veXeDAO.getById(xe.getMaVe());
                if (ve == null || !ve.conHan()) {
                    phi = xe.tinhPhi(thoiGianRa);
                }
            }

            phuongTienDAO.delete(bienSo);

            LichSu log = new LichSu(
                    "XE_RA",
                    bienSo,
                    xe.getLoaiXe(),
                    thoiGianRa,
                    phi
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
        // getById không ném IOException
        return phuongTienDAO.getById(bienSo);
    }

    @Override
    public boolean dangKyVeThang(VeXe ve) {
        try {
            if (veXeDAO.getById(ve.getMaVe()) != null) {
                return false;
            }
            boolean success = veXeDAO.add(ve);
            if (success) {
                double giaVe = "Xe Máy".equals(ve.getLoaiXe()) ? GIA_VE_THANG_XEMAY : GIA_VE_THANG_OTO;
                LichSu log = new LichSu(
                        "BAN_VE_THANG",
                        ve.getBienSo(),
                        ve.getLoaiXe(),
                        LocalDateTime.now(),
                        giaVe
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
            boolean success = veXeDAO.update(veMoi);
            if (success) {
                double giaVe = ("Xe Máy".equals(ve.getLoaiXe()) ? GIA_VE_THANG_XEMAY : GIA_VE_THANG_OTO) * soThang;
                LichSu log = new LichSu(
                        "GIA_HAN_VE",
                        ve.getBienSo(),
                        ve.getLoaiXe(),
                        LocalDateTime.now(),
                        giaVe
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
    public VeXe timVeThang(String maVe) {
        // getById không ném IOException
        return veXeDAO.getById(maVe);
    }
    
    @Override
    public VeXe findVeThangByBienSo(String bienSo) {
        // findByBienSo không ném IOException
        return veXeDAO.findByBienSo(bienSo);
    }

    @Override
    public List<PhuongTien> getXeTrongBai() {
        // getAll không ném IOException
        return phuongTienDAO.getAll();
    }

    @Override
    public List<VeXe> getDanhSachVeThang() {
        // getAll không ném IOException
        return veXeDAO.getAll();
    }

    @Override
    public Map<String, Double> baoCaoDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Integer> baoCaoLuuLuong(LocalDateTime tuNgay, LocalDateTime denNgay) {
        return new HashMap<>();
    }

    @Override
    public int kiemTraChoTrong() {
        // getAll không ném IOException
        return TONG_CHO - phuongTienDAO.getAll().size();
    }

    @Override
    public boolean kiemTraVeHopLe(String maVe) {
        // getById không ném IOException
        VeXe ve = veXeDAO.getById(maVe);
        return ve != null && ve.conHan();
    }

    @Override
    public void checkIn(XeMay xe) {
        xeVao(xe);
    }
}