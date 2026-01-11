package OOP.Parking.DAO;

import OOP.Parking.model.LichSu;
import OOP.Parking.util.CSVUtils;
import OOP.Parking.util.DataPath;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LichSuDAO {
    private static final String FILE_NAME = "lich_su.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static LichSuDAO instance;
    private List<LichSu> lichSuList;

    private LichSuDAO() throws IOException {
        lichSuList = new ArrayList<>();
        loadData();
    }

    public static synchronized LichSuDAO getInstance() throws IOException {
        if (instance == null) instance = new LichSuDAO();
        return instance;
    }

    private void loadData() throws IOException {
        String filePath = DataPath.getFilePath(FILE_NAME);
        File file = new File(filePath);
        if (!file.exists()) file.createNewFile();

        List<String[]> records = CSVUtils.readCSV(filePath);
        for (String[] record : records) {
            if (record.length >= 5) {
                try {
                    LichSu ls = new LichSu(
                            record[0].trim(), record[1].trim(), record[2].trim(),
                            LocalDateTime.parse(record[3].trim(), FORMATTER),
                            Double.parseDouble(record[4].trim())
                    );
                    lichSuList.add(ls);
                } catch (Exception e) {
                    System.err.println("Lỗi parse lịch sử: " + Arrays.toString(record));
                }
            }
        }
    }

    private void saveToFile() throws IOException {
        String filePath = DataPath.getFilePath(FILE_NAME);
        List<String[]> data = new ArrayList<>();
        for (LichSu ls : lichSuList) {
            data.add(new String[]{
                    ls.getLoaiThaoTac(), ls.getBienSo(), ls.getLoaiXe(),
                    ls.getThoiGian().format(FORMATTER), String.valueOf(ls.getPhi())
            });
        }
        CSVUtils.writeCSV(filePath, data);
    }

    public void log(LichSu lichSu) throws IOException {
        lichSuList.add(lichSu);
        String filePath = DataPath.getFilePath(FILE_NAME);
        String[] record = {
                lichSu.getLoaiThaoTac(), lichSu.getBienSo(), lichSu.getLoaiXe(),
                lichSu.getThoiGian().format(FORMATTER), String.valueOf(lichSu.getPhi())
        };
        CSVUtils.appendToCSV(filePath, record);
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
}