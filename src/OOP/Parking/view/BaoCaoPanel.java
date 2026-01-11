package OOP.Parking.view;

import OOP.Parking.service.BaoCaoService;
import OOP.Parking.service.impl.BaoCaoServiceImpl;
import OOP.Parking.util.DateUtils;
import OOP.Parking.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class BaoCaoPanel extends JPanel {
    private BaoCaoService baoCaoService;
    private JTabbedPane tabbedPane;
    
    // Components for Daily Report
    private JTextField txtNgayBaoCao;
    private DefaultTableModel modelNgay;
    private JLabel lblTongNgay;

    // Components for Monthly Report
    private JComboBox<Integer> cbThang, cbNam;
    private DefaultTableModel modelThang;
    private JLabel lblTongThang;

    public BaoCaoPanel() {
        try {
            baoCaoService = new BaoCaoServiceImpl();
            initUI();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo dịch vụ báo cáo: " + e.getMessage());
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel lblTitle = new JLabel("BÁO CÁO DOANH THU");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.PRIMARY_COLOR);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIUtils.FONT_BOLD);
        tabbedPane.addTab("Báo Cáo Ngày", createDailyReportPanel());
        tabbedPane.addTab("Báo Cáo Tháng", createMonthlyReportPanel());
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(0, 30, 30, 30));
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createDailyReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        
        txtNgayBaoCao = new JTextField(10);
        UIUtils.styleTextField(txtNgayBaoCao);
        txtNgayBaoCao.setText(DateUtils.formatDate(LocalDate.now()));
        
        JButton btnXem = UIUtils.createButton("Xem", UIUtils.PRIMARY_COLOR);
        btnXem.addActionListener(e -> loadDailyData());

        filterPanel.add(createLabel("Chọn ngày (dd/MM/yyyy):"));
        filterPanel.add(txtNgayBaoCao);
        filterPanel.add(btnXem);
        
        panel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Loại Xe", "Doanh Thu (VNĐ)"};
        modelNgay = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(modelNgay);
        UIUtils.styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Footer
        lblTongNgay = new JLabel("Tổng cộng: 0 VNĐ");
        lblTongNgay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongNgay.setForeground(UIUtils.SECONDARY_COLOR);
        lblTongNgay.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblTongNgay, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMonthlyReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);

        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        cbThang.setSelectedItem(LocalDate.now().getMonthValue());

        cbNam = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 5; i <= currentYear + 5; i++) cbNam.addItem(i);
        cbNam.setSelectedItem(currentYear);

        JButton btnXem = UIUtils.createButton("Xem", UIUtils.PRIMARY_COLOR);
        btnXem.addActionListener(e -> loadMonthlyData());

        filterPanel.add(createLabel("Tháng:"));
        filterPanel.add(cbThang);
        filterPanel.add(createLabel("Năm:"));
        filterPanel.add(cbNam);
        filterPanel.add(btnXem);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Hạng Mục", "Xe Máy (VNĐ)", "Ô tô (VNĐ)", "Tổng (VNĐ)"};
        modelThang = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(modelThang);
        UIUtils.styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Footer
        lblTongThang = new JLabel("Tổng doanh thu tháng: 0 VNĐ");
        lblTongThang.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongThang.setForeground(UIUtils.SECONDARY_COLOR);
        lblTongThang.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblTongThang, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIUtils.FONT_BOLD);
        lbl.setForeground(UIUtils.TEXT_COLOR);
        return lbl;
    }

    private void loadDailyData() {
        try {
            LocalDate date = DateUtils.parseDate(txtNgayBaoCao.getText());
            Map<String, Double> data = baoCaoService.baoCaoDoanhThuNgay(date);
            
            modelNgay.setRowCount(0);
            double total = 0;
            
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                modelNgay.addRow(new Object[]{entry.getKey(), String.format("%,.0f", entry.getValue())});
                total += entry.getValue();
            }
            lblTongNgay.setText("Tổng cộng: " + String.format("%,.0f VNĐ", total));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày!");
        }
    }

    private void loadMonthlyData() {
        int month = (int) cbThang.getSelectedItem();
        int year = (int) cbNam.getSelectedItem();
        
        Map<String, Double> data = baoCaoService.baoCaoDoanhThuThang(month, year);
        
        modelThang.setRowCount(0);
        
        // Row 1: Vé Ngày
        modelThang.addRow(new Object[]{
            "Vé Ngày (Lượt)", 
            String.format("%,.0f", data.get("VE_NGAY_XEMAY")), 
            String.format("%,.0f", data.get("VE_NGAY_OTO")),
            String.format("%,.0f", data.get("VE_NGAY_XEMAY") + data.get("VE_NGAY_OTO"))
        });
        
        // Row 2: Vé Tháng
        modelThang.addRow(new Object[]{
            "Vé Tháng (ĐK/Gia hạn)", 
            String.format("%,.0f", data.get("VE_THANG_XEMAY")), 
            String.format("%,.0f", data.get("VE_THANG_OTO")),
            String.format("%,.0f", data.get("VE_THANG_XEMAY") + data.get("VE_THANG_OTO"))
        });
        
        // Row 3: Tổng
        modelThang.addRow(new Object[]{
            "TỔNG CỘNG", 
            String.format("%,.0f", data.get("TONG_XEMAY")), 
            String.format("%,.0f", data.get("TONG_OTO")),
            String.format("%,.0f", data.get("TONG_CONG"))
        });

        lblTongThang.setText("Tổng doanh thu tháng: " + String.format("%,.0f VNĐ", data.get("TONG_CONG")));
    }
}