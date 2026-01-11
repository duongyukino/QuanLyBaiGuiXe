package OOP.Parking.DAO;

import OOP.Parking.model.*;
import OOP.Parking.util.CSVUtils;
import OOP.Parking.util.DataPath;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PhuongTienDAO implements BaseDAO<PhuongTien, String> {
    private static final String FILE_NAME = "phuong_tien.csv";
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter SPACE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static PhuongTienDAO instance;
    private Map<String, PhuongTien> phuongTienMap;

    private PhuongTien createPhuongTien(String loaiXe, String bienSo, String thoiGianVaoStr, String maVe) {
        LocalDateTime thoiGianVao = parseDateTimeSafe(thoiGianVaoStr);
        
        // Chuẩn hóa chuỗi loại xe: Xóa khoảng trắng, viết hoa, xóa dấu tiếng Việt cơ bản
        String type = loaiXe.trim().toUpperCase()
                .replace(" ", "")
                .replace("Ô", "O")
                .replace("Ê", "E")
                .replace("Á", "A"); 
        
        if (type.contains("XEMAY") || type.contains("MAY")) {
            XeMay xe = new XeMay(bienSo); xe.setThoiGianVao(thoiGianVao); xe.setMaVe(maVe); return xe;
        } else if (type.contains("OTO") || type.contains("CAR")) {
            Oto xe = new Oto(bienSo); xe.setThoiGianVao(thoiGianVao); xe.setMaVe(maVe); return xe;
        }
        
        // Fallback: Nếu không nhận diện được, mặc định là Xe Máy (để không mất dữ liệu)
        System.err.println("Cảnh báo: Không nhận diện được loại xe '" + loaiXe + "'. Mặc định là Xe Máy.");
        XeMay xe = new XeMay(bienSo); 
        xe.setThoiGianVao(thoiGianVao); 
        xe.setMaVe(maVe); 
        return xe;
    }
    
    private LocalDateTime parseDateTimeSafe(String str) {
        try {
            return LocalDateTime.parse(str, ISO_FORMATTER);
        } catch (Exception e1) {
            try {
                return LocalDateTime.parse(str, SPACE_FORMATTER);
            } catch (Exception e2) {
                System.err.println("Không thể parse ngày giờ: " + str + ". Sử dụng thời gian hiện tại.");
                return LocalDateTime.now();
            }
        }
    }

    private PhuongTienDAO() throws IOException {
        phuongTienMap = new HashMap<>();
        loadData();
    }

    public static synchronized PhuongTienDAO getInstance() throws IOException {
        if (instance == null) instance = new PhuongTienDAO();
        return instance;
    }

    private void loadData() throws IOException {
        String filePath = DataPath.getFilePath(FILE_NAME);
        File file = new File(filePath);
        if (!file.exists()) file.createNewFile();

        List<String[]> records = CSVUtils.readCSV(filePath);
        for (String[] record : records) {
            if (record.length >= 3) {
                try {
                    String loaiXe = record[0].trim();
                    // Bỏ qua dòng tiêu đề nếu có (thường tiêu đề là "Loại Xe" hoặc "Type")
                    if (loaiXe.equalsIgnoreCase("Loại Xe") || loaiXe.equalsIgnoreCase("Type")) continue;
                    
                    String bienSo = record[1].trim();
                    String thoiGian = record[2].trim();
                    String maVe = record.length > 3 ? record[3].trim() : "";
                    
                    PhuongTien pt = createPhuongTien(loaiXe, bienSo, thoiGian, maVe);
                    phuongTienMap.put(pt.getBienSo(), pt);
                } catch (Exception e) {
                    System.err.println("Lỗi parse phương tiện: " + Arrays.toString(record) + " - " + e.getMessage());
                }
            }
        }
    }

    private void saveToFile() throws IOException {
        String filePath = DataPath.getFilePath(FILE_NAME);
        List<String[]> data = new ArrayList<>();
        for (PhuongTien pt : phuongTienMap.values()) {
            data.add(new String[]{
                    pt.getLoaiXe(), pt.getBienSo(), pt.getThoiGianVao().format(ISO_FORMATTER),
                    pt.getMaVe() != null ? pt.getMaVe() : ""
            });
        }
        CSVUtils.writeCSV(filePath, data);
    }

    @Override public List<PhuongTien> getAll() { return new ArrayList<>(phuongTienMap.values()); }
    @Override public PhuongTien getById(String id) { return phuongTienMap.get(id); }
    @Override public boolean add(PhuongTien obj) throws IOException {
        if (phuongTienMap.containsKey(obj.getBienSo())) return false;
        phuongTienMap.put(obj.getBienSo(), obj); saveToFile(); return true;
    }
    @Override public boolean update(PhuongTien obj) throws IOException {
        if (!phuongTienMap.containsKey(obj.getBienSo())) return false;
        phuongTienMap.put(obj.getBienSo(), obj); saveToFile(); return true;
    }
    @Override public boolean delete(String id) throws IOException {
        if (!phuongTienMap.containsKey(id)) return false;
        phuongTienMap.remove(id); saveToFile(); return true;
    }
    @Override public boolean saveAll(List<PhuongTien> list) throws IOException {
        phuongTienMap.clear(); for(PhuongTien pt : list) phuongTienMap.put(pt.getBienSo(), pt); saveToFile(); return true;
    }
    public int countByType(String loaiXe) {
        return (int) phuongTienMap.values().stream().filter(pt -> pt.getLoaiXe().equalsIgnoreCase(loaiXe)).count();
    }
}