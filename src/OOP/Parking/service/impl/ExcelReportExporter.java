package OOP.Parking.service.impl;

import OOP.Parking.service.ReportExporter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ExcelReportExporter implements ReportExporter {

    // Sử dụng CSV format để tương thích với Excel mà không cần thư viện ngoài
    private static final String CSV_SEPARATOR = ",";

    @Override
    public void exportDoanhThu(Map<String, Double> data, String filePath) {
        // Đảm bảo file có đuôi .csv nếu chưa có
        if (!filePath.endsWith(".csv") && !filePath.endsWith(".xlsx")) {
            filePath += ".csv";
        } else if (filePath.endsWith(".xlsx")) {
            // Nếu người dùng chọn .xlsx, ta đổi thành .csv để ghi text đơn giản
            filePath = filePath.replace(".xlsx", ".csv");
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            
            // Thêm BOM để Excel hiển thị đúng tiếng Việt
            writer.write('\ufeff');

            // Header
            writer.write("Loại Xe" + CSV_SEPARATOR + "Doanh Thu (VNĐ)");
            writer.newLine();

            // Data
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                String line = String.format("%s%s%.0f", 
                        escapeCSV(entry.getKey()), 
                        CSV_SEPARATOR, 
                        entry.getValue());
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Lỗi khi xuất file báo cáo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void exportLuuLuong(Map<String, Integer> data, String filePath) {
        if (!filePath.endsWith(".csv") && !filePath.endsWith(".xlsx")) {
            filePath += ".csv";
        } else if (filePath.endsWith(".xlsx")) {
            filePath = filePath.replace(".xlsx", ".csv");
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            // Thêm BOM
            writer.write('\ufeff');

            // Header
            writer.write("Loại Thao Tác" + CSV_SEPARATOR + "Số Lượng");
            writer.newLine();

            // Data
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                String line = String.format("%s%s%d", 
                        escapeCSV(entry.getKey()), 
                        CSV_SEPARATOR, 
                        entry.getValue());
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Lỗi khi xuất file báo cáo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(CSV_SEPARATOR) || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}