package OOP.Parking.DAO;

import OOP.Parking.model.*;
import OOP.Parking.util.CSVUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PhuongTienDAO implements BaseDAO<PhuongTien, String> {
    private static final String FILE_PATH = "src/main/resources/data/phuong_tien.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static PhuongTienDAO instance;
    private Map<String, PhuongTien> phuongTienMap;

    // Factory Pattern cho PhuongTien
    private PhuongTien createPhuongTien(String loaiXe, String bienSo, String thoiGianVaoStr, String maVe) {
        LocalDateTime thoiGianVao = LocalDateTime.parse(thoiGianVaoStr, FORMATTER);

        switch (loaiXe.toUpperCase()) {
            case "XEMAY":
                XeMay xeMay = new XeMay(bienSo);
                xeMay.setThoiGianVao(thoiGianVao);
                xeMay.setMaVe(maVe);
                return xeMay;

            case "OTO":
                Oto oto = new Oto(bienSo);
                oto.setThoiGianVao(thoiGianVao);
                oto.setMaVe(maVe);
                return oto;

            case "XEDAP":
                Xedap xedap = new Xedap(bienSo);
                xedap.setThoiGianVao(thoiGianVao);
                xedap.setMaVe(maVe);
                return xedap;

            default:
                throw new IllegalArgumentException("Loại xe không hợp lệ: " + loaiXe);
        }
    }

    private PhuongTienDAO() throws IOException {
        phuongTienMap = new HashMap<>();
        loadData();
    }

    public static synchronized PhuongTienDAO getInstance() throws IOException {
        if (instance == null) {
            instance = new PhuongTienDAO();
        }
        return instance;
    }

    private void loadData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.err.println("File không tồn tại: " + file.getAbsolutePath());
            return;
        }

        List<String[]> records = CSVUtils.readCSV(FILE_PATH);
        for (String[] record : records) {
            if (record.length >= 3) { // Minimum required fields
                try {
                    String loaiXe = record[0];
                    String bienSo = record[1];
                    String thoiGianVao = record[2];
                    String maVe = record.length > 3 ? record[3] : "";

                    PhuongTien pt = createPhuongTien(loaiXe, bienSo, thoiGianVao, maVe);
                    phuongTienMap.put(bienSo, pt);
                } catch (Exception e) {
                    System.err.println("Lỗi khi đọc dòng phương tiện: " + Arrays.toString(record) + " - " + e.getMessage());
                }
            }
        }
        System.out.println("Đã load " + phuongTienMap.size() + " phương tiện.");
    }

    private void saveToFile() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (PhuongTien pt : phuongTienMap.values()) {
            String[] record = {
                    pt.getLoaiXe().toUpperCase().replace(" ", ""),
                    pt.getBienSo(),
                    pt.getThoiGianVao().format(FORMATTER),
                    pt.getMaVe() != null ? pt.getMaVe() : ""
            };
            data.add(record);
        }
        CSVUtils.writeCSV(FILE_PATH, data);
    }

    @Override
    public List<PhuongTien> getAll() throws IOException {
        return new ArrayList<>(phuongTienMap.values());
    }

    @Override
    public PhuongTien getById(String bienSo) throws IOException {
        return phuongTienMap.get(bienSo);
    }

    @Override
    public boolean add(PhuongTien pt) throws IOException {
        if (phuongTienMap.containsKey(pt.getBienSo())) {
            return false;
        }
        phuongTienMap.put(pt.getBienSo(), pt);
        saveToFile();
        return true;
    }

    @Override
    public boolean update(PhuongTien pt) throws IOException {
        if (!phuongTienMap.containsKey(pt.getBienSo())) {
            return false;
        }
        phuongTienMap.put(pt.getBienSo(), pt);
        saveToFile();
        return true;
    }

    @Override
    public boolean delete(String bienSo) throws IOException {
        if (!phuongTienMap.containsKey(bienSo)) {
            return false;
        }
        phuongTienMap.remove(bienSo);
        saveToFile();
        return true;
    }

    @Override
    public boolean saveAll(List<PhuongTien> list) throws IOException {
        phuongTienMap.clear();
        for (PhuongTien pt : list) {
            phuongTienMap.put(pt.getBienSo(), pt);
        }
        saveToFile();
        return true;
    }

    // Phương thức đặc biệt
    public int countByType(String loaiXe) {
        return (int) phuongTienMap.values().stream()
                .filter(pt -> pt.getLoaiXe().equalsIgnoreCase(loaiXe))
                .count();
    }
}