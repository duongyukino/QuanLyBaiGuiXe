package OOP.Parking.util;

import java.util.regex.Pattern;

public class ValidationUtils {
    // Regex cho biển số xe (Ví dụ: 29A-123.45 hoặc 29A1-123.45)
    private static final String BIEN_SO_REGEX = "^[0-9]{2}[A-Z]{1,2}-[0-9]{3}\\.[0-9]{2}$|^[0-9]{2}[A-Z]{1,2}[0-9]-[0-9]{3}\\.[0-9]{2}$";
    
    // Regex cho số điện thoại (10 số, bắt đầu bằng 0)
    private static final String PHONE_REGEX = "^0[0-9]{9}$";
    
    // Regex cho Email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean isValidBienSo(String bienSo) {
        return bienSo != null && Pattern.matches(BIEN_SO_REGEX, bienSo);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && Pattern.matches(PHONE_REGEX, phone);
    }

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}