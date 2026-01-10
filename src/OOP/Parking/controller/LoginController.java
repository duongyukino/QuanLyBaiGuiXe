package OOP.Parking.controller;

import OOP.Parking.model.User;
import OOP.Parking.service.AuthenticationService;
import OOP.Parking.view.LoginFrame;
import OOP.Parking.view.MainDashboard;

import javax.swing.*;

public class LoginController {
    private LoginFrame view;
    private AuthenticationService authService;

    public LoginController(LoginFrame view, AuthenticationService authService) {
        this.view = view;
        this.authService = authService;
        initController();
    }

    private void initController() {
        view.getLoginButton().addActionListener(e -> login());
    }

    private void login() {
        String username = view.getUsername();
        String password = view.getPassword();

        try {
            User user = authService.login(username, password);
            if (user != null) {
                view.dispose();
                new MainDashboard(user.getRole()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}