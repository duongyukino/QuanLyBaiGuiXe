package OOP.Parking.view;

import OOP.Parking.controller.LoginController;
import OOP.Parking.service.AuthenticationService;
import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.AuthenticationServiceImpl;
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

        // --- SIDEBAR (Gradient) ---
        sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, UIUtils.PRIMARY_COLOR, 0, h, UIUtils.GRADIENT_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(280, 0));

        // Logo Area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        logoPanel.setOpaque(false); // Transparent
        JLabel lblLogo = new JLabel(UIUtils.ICON_PARKING + " PARKING PRO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(Color.WHITE); // White text on gradient
        logoPanel.add(lblLogo);
        sidebar.add(logoPanel);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Menu Items
        addMenuItem(UIUtils.ICON_HOME + " Trang Chủ", e -> showHome());
        addMenuItem(UIUtils.ICON_CAR + " Quản Lý Xe", e -> moManHinhXeVao());
        addMenuItem(UIUtils.ICON_SEARCH + " Tra Cứu Xe", e -> moManHinhTraCuu());
        
        if ("QUẢN LÝ".equals(vaiTro)) {
            addMenuItem(UIUtils.ICON_HISTORY + " Lịch Sử", e -> moManHinhLichSu());
            addMenuItem(UIUtils.ICON_TICKET + " Vé Tháng", e -> moManHinhDangKyVe());
            addMenuItem(UIUtils.ICON_REPORT + " Báo Cáo", e -> moManHinhBaoCaoDoanhThu());
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // User Profile
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setOpaque(false);
        userPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblUser = new JLabel((vaiTro.equals("QUẢN LÝ") ? UIUtils.ICON_ADMIN : UIUtils.ICON_USER) + " " + vaiTro);
        lblUser.setFont(UIUtils.FONT_BOLD);
        lblUser.setForeground(Color.WHITE);
        
        JButton btnLogout = new JButton(UIUtils.ICON_LOGOUT);
        btnLogout.setToolTipText("Đăng xuất");
        btnLogout.setFont(UIUtils.FONT_NORMAL);
        btnLogout.setForeground(Color.WHITE);
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
        btn.setForeground(Color.WHITE); // White text
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(12, 30, 12, 30));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(action);
        
        // Hover effect (Semi-transparent white)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setOpaque(true);
                btn.setBackground(new Color(255, 255, 255, 50)); // 20% White
                btn.setFont(UIUtils.FONT_BOLD);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setOpaque(false);
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
        JLabel lblTitle = new JLabel("Tổng Quan Hôm Nay " + UIUtils.ICON_TIME);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.TEXT_COLOR);
        homePanel.add(lblTitle, BorderLayout.NORTH);

        // Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        cardsPanel.setOpaque(false);
        
        int choTrong = parkingService.kiemTraChoTrong();
        cardsPanel.add(createStatCard("Chỗ Trống", String.valueOf(choTrong), UIUtils.ICON_CHECK, UIUtils.SECONDARY_COLOR));
        cardsPanel.add(createStatCard("Đang Gửi", String.valueOf(100 - choTrong), UIUtils.ICON_CAR, UIUtils.PRIMARY_COLOR));
        
        if ("QUẢN LÝ".equals(vaiTro)) {
            cardsPanel.add(createStatCard("Cảnh Báo", "0", UIUtils.ICON_WARNING, UIUtils.ACCENT_COLOR));
        } else {
            cardsPanel.add(createStatCard("Ca Trực", "Sáng", UIUtils.ICON_USER, Color.GRAY));
        }

        homePanel.add(cardsPanel, BorderLayout.CENTER);
        
        contentPanel.add(homePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtils.FONT_NORMAL);
        lblTitle.setForeground(UIUtils.TEXT_MUTED);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(color);
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setForeground(new Color(200, 200, 200));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        card.add(lblIcon, BorderLayout.EAST);
        
        return card;
    }

    // Navigation methods
    private void moManHinhXeVao() { 
        contentPanel.removeAll();
        contentPanel.add(new ParkingPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void moManHinhTraCuu() { 
        contentPanel.removeAll();
        contentPanel.add(new QuanLyXePanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void moManHinhLichSu() { 
        contentPanel.removeAll();
        contentPanel.add(new LichSuPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void moManHinhDangKyVe() { 
        contentPanel.removeAll();
        contentPanel.add(new QuanLyVePanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void moManHinhBaoCaoDoanhThu() { 
        contentPanel.removeAll();
        contentPanel.add(new BaoCaoPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void logout() {
        dispose();
        try {
            LoginFrame loginFrame = new LoginFrame();
            AuthenticationService authService = new AuthenticationServiceImpl();
            new LoginController(loginFrame, authService);
            loginFrame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}