package OOP.Parking.view;

import OOP.Parking.util.UIUtils;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Đăng Nhập - Hệ Thống Quản Lý Bãi Xe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        // Sử dụng Gradient Panel làm nền
        JPanel mainPanel = UIUtils.createGradientPanel();
        mainPanel.setLayout(new GridBagLayout());

        JPanel cardPanel = UIUtils.createCardPanel();
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Logo
        JLabel lblIcon = new JLabel(UIUtils.ICON_PARKING, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        gbc.gridy = 0;
        cardPanel.add(lblIcon, gbc);

        // Title
        JLabel lblTitle = new JLabel("WELCOME BACK", SwingConstants.CENTER);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.PRIMARY_COLOR);
        gbc.gridy = 1;
        cardPanel.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Bãi Xe", SwingConstants.CENTER);
        lblSubtitle.setFont(UIUtils.FONT_NORMAL);
        lblSubtitle.setForeground(UIUtils.TEXT_MUTED);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(lblSubtitle, gbc);

        // Username
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 5, 0);
        JLabel lblUser = new JLabel(UIUtils.ICON_USER + " Tài khoản");
        lblUser.setFont(UIUtils.FONT_BOLD);
        lblUser.setForeground(UIUtils.TEXT_COLOR);
        cardPanel.add(lblUser, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtUsername = new JTextField(20);
        UIUtils.styleTextField(txtUsername);
        cardPanel.add(txtUsername, gbc);

        // Password
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel lblPass = new JLabel("🔑 Mật khẩu");
        lblPass.setFont(UIUtils.FONT_BOLD);
        lblPass.setForeground(UIUtils.TEXT_COLOR);
        cardPanel.add(lblPass, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 30, 0);
        txtPassword = new JPasswordField(20);
        UIUtils.styleTextField(txtPassword);
        cardPanel.add(txtPassword, gbc);

        // Button
        gbc.gridy = 7;
        btnLogin = UIUtils.createButton("ĐĂNG NHẬP NGAY", UIUtils.PRIMARY_COLOR);
        btnLogin.setPreferredSize(new Dimension(250, 45));
        cardPanel.add(btnLogin, gbc);

        mainPanel.add(cardPanel);
        setContentPane(mainPanel);
        getRootPane().setDefaultButton(btnLogin);
    }

    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public JButton getLoginButton() { return btnLogin; }
    public void showErrorMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}