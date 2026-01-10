package OOP.Parking.DAO;

import OOP.Parking.model.LichSu;
import OOP.Parking.util.CSVUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LichSuDAO {
    private static final String FILE_PATH = "src/main/resources/data/lich_su.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // yyyy-MM-ddTHH:mm:ss
    private static LichSuDAO instance;
    private List<LichSu> lichSuList;

    private LichSuDAO() throws IOException {
        lichSuList = new ArrayList<>();
        loadData();
    }

    public static synchronized LichSuDAO getInstance() throws IOException {
        if (instance == null) {
            instance = new LichSuDAO();
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
            if (record.length >= 6) {
                try {
                    String id = record[0].trim();
                    String loaiThaoTac = record[1].trim();
                    String bienSo = record[2].trim();
                    String chiTiet = record[3].trim();
                    double phi = Double.parseDouble(record[4].trim());
                    String thoiGianStr = record[5].trim();
                    String nguoiThucHien = record.length > 6 ? record[6].trim() : "";

                    LocalDateTime thoiGian = LocalDateTime.parse(thoiGianStr, FORMATTER);

                    LichSu ls = new LichSu(id, loaiThaoTac, bienSo, chiTiet, phi, thoiGian, nguoiThucHien);
                    lichSuList.add(ls);
                } catch (Exception e) {
                    System.err.println("Lỗi parse dòng lịch sử: " + Arrays.toString(record) + " - " + e.getMessage());
                }
            }
        }
        System.out.println("Đã load " + lichSuList.size() + " bản ghi lịch sử.");
    }

    private void saveToFile() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (LichSu ls : lichSuList) {
            String[] record = {
                    ls.getId(),
                    ls.getLoaiThaoTac(),
                    ls.getBienSo(),
                    ls.getChiTiet(),
                    String.valueOf(ls.getPhi()),
                    ls.getThoiGian().format(FORMATTER),
                    ls.getNguoiThucHien()
            };
            data.add(record);
        }
        CSVUtils.writeCSV(FILE_PATH, data);
    }

    public void log(LichSu lichSu) throws IOException {
        lichSuList.add(lichSu);
        String[] record = {
                lichSu.getId(),
                lichSu.getLoaiThaoTac(),
                lichSu.getBienSo(),
                lichSu.getChiTiet(),
                String.valueOf(lichSu.getPhi()),
                lichSu.getThoiGian().format(FORMATTER),
                lichSu.getNguoiThucHien()
        };
        CSVUtils.appendToCSV(FILE_PATH, record);
    }

    public List<LichSu> getLichSuByDate(LocalDateTime start, LocalDateTime end) {
        return lichSuList.stream()
                .filter(ls -> ls.getThoiGian().isAfter(start) && ls.getThoiGian().isBefore(end))
                .collect(Collectors.toList());
    }

    public List<LichSu> getLichSuByBienSo(String bienSo) {
        return lichSuList.stream()
                .filter(ls -> ls.getBienSo().equals(bienSo))
                .collect(Collectors.toList());
    }

    public double getDoanhThu(LocalDateTime start, LocalDateTime end) {
        return lichSuList.stream()
                .filter(ls -> ls.getThoiGian().isAfter(start) && ls.getThoiGian().isBefore(end))
                .filter(ls -> "XE_RA".equals(ls.getLoaiThaoTac()))
                .mapToDouble(LichSu::getPhi)
                .sum();
    }
}