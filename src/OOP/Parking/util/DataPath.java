package OOP.Parking.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DataPath {
    // Tên thư mục chứa dữ liệu bên ngoài
    private static final String DATA_DIR = "data";

    public static String getFilePath(String fileName) {
        // 1. Xác định thư mục data bên ngoài
        String workingDir = System.getProperty("user.dir");
        String dataFolderPath = workingDir + File.separator + DATA_DIR;
        File dataFolder = new File(dataFolderPath);

        // Tạo thư mục nếu chưa có
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        // 2. Xác định file đích
        File targetFile = new File(dataFolder, fileName);

        // 3. Nếu file chưa tồn tại, copy từ resources ra
        if (!targetFile.exists()) {
            copyFromResources(fileName, targetFile);
        }

        return targetFile.getAbsolutePath();
    }

    private static void copyFromResources(String fileName, File targetFile) {
        // Đường dẫn trong resources (phải bắt đầu bằng /)
        String resourcePath = "/data/" + fileName;
        
        try (InputStream is = DataPath.class.getResourceAsStream(resourcePath)) {
            if (is != null) {
                Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Đã khởi tạo file dữ liệu mẫu: " + targetFile.getAbsolutePath());
            } else {
                // Nếu không có trong resources, tạo file rỗng
                targetFile.createNewFile();
                System.out.println("Đã tạo file dữ liệu mới (rỗng): " + targetFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi copy file dữ liệu: " + e.getMessage());
        }
    }
}