package OOP.Parking.service;

import OOP.Parking.model.User;

public interface AuthenticationService {
    /**
     * Đăng nhập hệ thống
     * @return User nếu thành công, null nếu thất bại
     */
    User login(String username, String password);

    /**
     * Đăng xuất
     */
    void logout();

    /**
     * Lấy user hiện tại đang đăng nhập
     */
    User getCurrentUser();

    /**
     * Đổi mật khẩu
     */
    boolean changePassword(String username, String oldPassword, String newPassword);
}