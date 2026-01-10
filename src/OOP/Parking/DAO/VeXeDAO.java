package OOP.Parking.DAO;

import OOP.Parking.model.VeXe;
import OOP.Parking.util.CSVUtils;
import OOP.Parking.util.DateUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class VeXeDAO implements BaseDAO<VeXe, String> {
    private static final String FILE_PATH = "src/main/resources/data/ve_thang.csv";
    private static VeXeDAO instance;
    private Map<String, VeXe> veXeMap;

    private VeXeDAO() throws IOException {
        veXeMap = new HashMap<>();
        loadData();
    }

    public static synchronized VeXeDAO getInstance() throws IOException {
        if (instance == null) {
            instance = new VeXeDAO();
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
            if (record.length >= 8) {
                try {
                    VeXe ve = new VeXe(
                            record[0],  // maVe
                            record[1],  // bienSo
                            record[2],  // loaiXe
                            record[3],  // loaiVe
                            LocalDate.parse(record[4]),  // ngayDangKy
                            LocalDate.parse(record[5]),  // ngayHetHan
                            record[6],  // chuXe
                            record[7]   // soDienThoai
                    );
                    veXeMap.put(ve.getMaVe(), ve);
                } catch (Exception e) {
                    System.err.println("Lỗi parse dòng vé xe: " + Arrays.toString(record));
                }
            }
        }
        System.out.println("Đã load " + veXeMap.size() + " vé tháng.");
    }

    private void saveToFile() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (VeXe ve : veXeMap.values()) {
            String[] record = {
                    ve.getMaVe(),
                    ve.getBienSo(),
                    ve.getLoaiXe(),
                    ve.getLoaiVe(),
                    ve.getNgayDangKy().toString(),
                    ve.getNgayHetHan().toString(),
                    ve.getChuXe(),
                    ve.getSoDienThoai()
            };
            data.add(record);
        }
        CSVUtils.writeCSV(FILE_PATH, data);
    }

    @Override
    public List<VeXe> getAll() throws IOException {
        return new ArrayList<>(veXeMap.values());
    }

    @Override
    public VeXe getById(String maVe) throws IOException {
        return veXeMap.get(maVe);
    }

    @Override
    public boolean add(VeXe ve) throws IOException {
        if (veXeMap.containsKey(ve.getMaVe())) {
            return false;
        }
        veXeMap.put(ve.getMaVe(), ve);
        saveToFile();
        return true;
    }

    @Override
    public boolean update(VeXe ve) throws IOException {
        if (!veXeMap.containsKey(ve.getMaVe())) {
            return false;
        }
        veXeMap.put(ve.getMaVe(), ve);
        saveToFile();
        return true;
    }

    @Override
    public boolean delete(String maVe) throws IOException {
        if (!veXeMap.containsKey(maVe)) {
            return false;
        }
        veXeMap.remove(maVe);
        saveToFile();
        return true;
    }

    @Override
    public boolean saveAll(List<VeXe> list) throws IOException {
        veXeMap.clear();
        for (VeXe ve : list) {
            veXeMap.put(ve.getMaVe(), ve);
        }
        saveToFile();
        return true;
    }

    // Phương thức đặc biệt
    public VeXe findByBienSo(String bienSo) throws IOException {
        return veXeMap.values().stream()
                .filter(ve -> ve.getBienSo().equals(bienSo))
                .filter(ve -> ve.conHan())
                .findFirst()
                .orElse(null);
    }

    public List<VeXe> getVeSapHetHan(int daysBefore) throws IOException {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(daysBefore);

        return veXeMap.values().stream()
                .filter(ve -> ve.getNgayHetHan().isAfter(today))
                .filter(ve -> ve.getNgayHetHan().isBefore(threshold) ||
                        ve.getNgayHetHan().isEqual(threshold))
                .collect(Collectors.toList());
    }
}