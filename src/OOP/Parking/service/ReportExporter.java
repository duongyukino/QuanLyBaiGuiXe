package OOP.Parking.service;

import java.util.List;
import java.util.Map;

public interface ReportExporter {
    void exportDoanhThu(Map<String, Double> data, String filePath);
    void exportLuuLuong(Map<String, Integer> data, String filePath);
}