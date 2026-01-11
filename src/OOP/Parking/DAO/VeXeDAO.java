package OOP.Parking.DAO;

import OOP.Parking.model.VeXe;
import OOP.Parking.util.CSVUtils;
import OOP.Parking.util.DataPath;
import OOP.Parking.util.DateUtils; // Import DateUtils

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class VeXeDAO implements BaseDAO<VeXe, String> {
    private static final String FILE_NAME = "ve_thang.csv";
    private static VeXeDAO instance;
    private Map<String, VeXe> veXeMap;

    private VeXeDAO() throws IOException {
        veXeMap = new HashMap<>();
        loadData();
    }

    public static synchronized VeXeDAO getInstance() throws IOException {
        if (instance == null) instance = new VeXeDAO();
        return instance;
    }

    private void loadData() throws IOException {
        String filePath = DataPath.getFilePath(FILE_NAME);
        File file = new File(filePath);
        if (!file.exists()) file.createNewFile();

        List<String[]> records = CSVUtils.readCSV(filePath);
        for (String[] record : records) {
            if (record.length >= 8) {
                try {
                    // Sử dụng DateUtils để parse định dạng dd/MM/yyyy nếu cần
                    // Hoặc thử cả 2 định dạng để an toàn
                    LocalDate ngayDangKy = parseDateSafe(record[4]);
                    LocalDate ngayHetHan = parseDateSafe(record[5]);

                    VeXe ve = new VeXe(
                            record[0], record[1], record[2], record[3],
                            ngayDangKy, ngayHetHan,
                            record[6], record[7]
                    );
                    veXeMap.put(ve.getMaVe(), ve);
                } catch (Exception e) {
                    System.err.println("Lỗi parse vé: " + Arrays.toString(record) + " - " + e.getMessage());
                }
            }
        }
    }
    
    private LocalDate parseDateSafe(String dateStr) {
        try {
            // Thử parse theo chuẩn ISO (yyyy-MM-dd) trước (do file mẫu đang dùng cái này)
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            try {
                // Nếu lỗi, thử parse theo dd/MM/yyyy (do DateUtils dùng cái này)
                return DateUtils.parseDate(dateStr);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Ngày tháng không hợp lệ: " + dateStr);
            }
        }
    }

    private void saveToFile() throws IOException {
        String filePath = DataPath.getFilePath(FILE_NAME);
        List<String[]> data = new ArrayList<>();
        for (VeXe ve : veXeMap.values()) {
            data.add(new String[]{
                    ve.getMaVe(), ve.getBienSo(), ve.getLoaiXe(), ve.getLoaiVe(),
                    ve.getNgayDangKy().toString(), // Lưu yyyy-MM-dd để chuẩn hóa
                    ve.getNgayHetHan().toString(),
                    ve.getChuXe(), ve.getSoDienThoai()
            });
        }
        CSVUtils.writeCSV(filePath, data);
    }

    @Override public List<VeXe> getAll() { return new ArrayList<>(veXeMap.values()); }
    @Override public VeXe getById(String id) { return veXeMap.get(id); }
    @Override public boolean add(VeXe obj) throws IOException {
        if (veXeMap.containsKey(obj.getMaVe())) return false;
        veXeMap.put(obj.getMaVe(), obj); saveToFile(); return true;
    }
    @Override public boolean update(VeXe obj) throws IOException {
        if (!veXeMap.containsKey(obj.getMaVe())) return false;
        veXeMap.put(obj.getMaVe(), obj); saveToFile(); return true;
    }
    @Override public boolean delete(String id) throws IOException {
        if (!veXeMap.containsKey(id)) return false;
        veXeMap.remove(id); saveToFile(); return true;
    }
    @Override public boolean saveAll(List<VeXe> list) throws IOException {
        veXeMap.clear(); for(VeXe v : list) veXeMap.put(v.getMaVe(), v); saveToFile(); return true;
    }
    
    public VeXe findByBienSo(String bienSo) {
        return veXeMap.values().stream()
                .filter(ve -> ve.getBienSo().equals(bienSo) && ve.conHan())
                .findFirst().orElse(null);
    }
}