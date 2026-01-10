package OOP.Parking.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CSVUtils {
    private static final String CSV_SEPARATOR = ",";
    private static final String CSV_QUOTE = "\"";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    // BOM (Byte Order Mark) cho UTF-8 để Excel nhận diện đúng encoding
    private static final String UTF8_BOM = "\uFEFF";

    /**
     * Đọc file CSV và trả về danh sách các dòng dưới dạng List<String[]>
     */
    public static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                // Xử lý BOM ở dòng đầu tiên nếu có
                if (firstLine) {
                    if (line.startsWith(UTF8_BOM)) {
                        line = line.substring(1);
                    }
                    firstLine = false;
                }

                if (!line.trim().isEmpty()) {
                    String[] values = parseCSVLine(line);
                    records.add(values);
                }
            }
        }
        return records;
    }

    /**
     * Ghi danh sách dữ liệu vào file CSV
     */
    public static void writeCSV(String filePath, List<String[]> data) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            
            // Ghi BOM để Excel hiển thị đúng tiếng Việt
            // bw.write(UTF8_BOM); // Tạm thời comment lại nếu gây lỗi đọc ở các phần mềm khác không hỗ trợ BOM

            for (String[] row : data) {
                String line = Arrays.stream(row)
                        .map(value -> escapeCSV(value))
                        .collect(Collectors.joining(CSV_SEPARATOR));
                bw.write(line);
                bw.write(LINE_SEPARATOR);
            }
        }
    }

    /**
     * Thêm dòng mới vào CSV
     */
    public static void appendToCSV(String filePath, String[] row) throws IOException {
        File file = new File(filePath);
        boolean fileExists = file.exists();

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            
            // Nếu file mới tạo, có thể ghi BOM (tùy chọn)
            // if (!fileExists) bw.write(UTF8_BOM);

            if (fileExists && file.length() > 0) {
                // Thêm newline trước khi append nếu file không rỗng và chưa kết thúc bằng newline
                // Tuy nhiên, BufferedWriter thường không check được ký tự cuối cùng dễ dàng mà không đọc lại.
                // Cách an toàn là luôn đảm bảo writeCSV ghi newline cuối cùng.
                // Ở đây ta giả định file luôn kết thúc hợp lệ hoặc chấp nhận rủi ro nhỏ.
                // Để an toàn hơn, ta có thể check file size > 0
            }
            
            String line = Arrays.stream(row)
                    .map(value -> escapeCSV(value))
                    .collect(Collectors.joining(CSV_SEPARATOR));
            bw.write(line);
            bw.write(LINE_SEPARATOR); // Luôn xuống dòng sau khi ghi
        }
    }

    /**
     * Escape giá trị CSV
     */
    private static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(CSV_SEPARATOR) || value.contains("\"") || value.contains("\n")) {
            return CSV_QUOTE + value.replace("\"", "\"\"") + CSV_QUOTE;
        }
        return value;
    }

    /**
     * Parse một dòng CSV
     */
    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Gặp "" trong quotes -> coi là 1 dấu " thực sự
                    currentValue.append('"');
                    i++;
                } else {
                    // Bắt đầu hoặc kết thúc quote
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Kết thúc 1 trường
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString());

        return values.toArray(new String[0]);
    }
}