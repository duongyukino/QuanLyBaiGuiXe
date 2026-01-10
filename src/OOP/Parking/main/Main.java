package OOP.Parking.main;

import OOP.Parking.controller.LoginController;
import OOP.Parking.service.AuthenticationService;
import OOP.Parking.service.impl.AuthenticationServiceImpl;
import OOP.Parking.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Thiết lập giao diện Nimbus (có sẵn trong JDK)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Nếu lỗi thì dùng giao diện mặc định
            System.err.println("Không thể khởi tạo giao diện Nimbus");
        }

        // Sử dụng SwingUtilities để đảm bảo thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Dependency Injection
                AuthenticationService authService = new AuthenticationServiceImpl();

                // Tạo view
                LoginFrame loginFrame = new LoginFrame();

                // Tạo controller và kết nối với view, service
                new LoginController(loginFrame, authService);

                // Hiển thị view
                loginFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Không thể khởi động hệ thống: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}