package OOP.Parking.view;

import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.ParkingServiceImpl;
import OOP.Parking.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class MainDashboard extends JFrame {
    private String vaiTro;
    private ParkingService parkingService;
    private JPanel contentPanel;
    private JPanel sidebar;

    public MainDashboard(String vaiTro) {
        this.vaiTro = vaiTro;
        try {
            this.parkingService = new ParkingServiceImpl();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage());
        }

        setTitle("Hệ Thống Quản Lý Bãi Xe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- SIDEBAR (White & Clean) ---
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230))); // Right border

        // Logo Area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        logoPanel.setBackground(Color.WHITE);
        JLabel lblLogo = new JLabel("PARKING PRO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(UIUtils.PRIMARY_COLOR);
        logoPanel.add(lblLogo);
        sidebar.add(logoPanel);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Menu Items
        addMenuItem("Trang Chủ", e -> showHome());
        addMenuItem("Quản Lý Xe", e -> moManHinhXeVao());
        addMenuItem("Tra Cứu", e -> moManHinhTraCuu());
        
        if (vaiTro.equals("QUẢN LÝ")) {
            addMenuItem("Vé Tháng", e -> moManHinhDangKyVe());
            addMenuItem("Báo Cáo", e -> moManHinhBaoCaoDoanhThu());
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // User Profile Area
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(new Color(240, 248, 255));
        userPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblUser = new JLabel(vaiTro);
        lblUser.setFont(UIUtils.FONT_BOLD);
        lblUser.setForeground(UIUtils.PRIMARY_COLOR);
        
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(UIUtils.FONT_SMALL);
        btnLogout.setForeground(UIUtils.ACCENT_COLOR);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> logout());

        userPanel.add(lblUser, BorderLayout.CENTER);
        userPanel.add(btnLogout, BorderLayout.EAST);
        sidebar.add(userPanel);

        add(sidebar, BorderLayout.WEST);

        // --- MAIN CONTENT ---
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        add(contentPanel, BorderLayout.CENTER);

        showHome();
    }

    private void addMenuItem(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(UIUtils.FONT_NORMAL);
        btn.setForeground(UIUtils.TEXT_COLOR);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new EmptyBorder(12, 30, 12, 30));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(action);
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(240, 247, 255));
                btn.setForeground(UIUtils.PRIMARY_COLOR);
                btn.setFont(UIUtils.FONT_BOLD);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(UIUtils.TEXT_COLOR);
                btn.setFont(UIUtils.FONT_NORMAL);
            }
        });

        sidebar.add(btn);
    }

    private void showHome() {
        contentPanel.removeAll();
        
        JPanel homePanel = new JPanel(new BorderLayout(30, 30));
        homePanel.setBackground(UIUtils.BACKGROUND_COLOR);
        homePanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Header
        JLabel lblTitle = new JLabel("Tổng Quan Hôm Nay");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.TEXT_COLOR);
        homePanel.add(lblTitle, BorderLayout.NORTH);

        // Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        cardsPanel.setOpaque(false);
        
        int choTrong = parkingService.kiemTraChoTrong();
        cardsPanel.add(createStatCard("Chỗ Trống", String.valueOf(choTrong), UIUtils.SECONDARY_COLOR));
        cardsPanel.add(createStatCard("Đang Gửi", String.valueOf(100 - choTrong), UIUtils.PRIMARY_COLOR));
        cardsPanel.add(createStatCard("Cảnh Báo", "0", UIUtils.ACCENT_COLOR)); // Placeholder

        homePanel.add(cardsPanel, BorderLayout.CENTER);
        
        contentPanel.add(homePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtils.FONT_NORMAL);
        lblTitle.setForeground(UIUtils.TEXT_MUTED);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(color);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }

    // Navigation methods
    private void moManHinhXeVao() { new ParkingUI().setVisible(true); }
    private void moManHinhTraCuu() { new QuanLyXeFrame().setVisible(true); }
    private void moManHinhDangKyVe() { new QuanLyVeFrame().setVisible(true); }
    private void moManHinhBaoCaoDoanhThu() { new BaoCaoFrame().setVisible(true); }
    private void logout() { dispose(); new LoginFrame().setVisible(true); }
}