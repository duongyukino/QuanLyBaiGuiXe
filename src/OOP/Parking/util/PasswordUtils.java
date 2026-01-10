package OOP.Parking.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {

    /**
     * Mã hóa mật khẩu sử dụng SHA-256
     * @param plainPassword Mật khẩu gốc
     * @return Chuỗi đã mã hóa
     */
    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainPassword.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Kiểm tra mật khẩu
     * @param plainPassword Mật khẩu nhập vào
     * @param hashedPassword Mật khẩu đã lưu (đã mã hóa)
     * @return true nếu khớp
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        String newHash = hashPassword(plainPassword);
        return newHash != null && newHash.equals(hashedPassword);
    }
}