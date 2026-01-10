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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class BaoCaoFrame extends JFrame {
    private BaoCaoService baoCaoService;
    private JTable tableDoanhThu;
    private DefaultTableModel modelDoanhThu;
    private JLabel lblTongDoanhThu;
    private JTextField txtTuNgay, txtDenNgay;

    public BaoCaoFrame() {
        try {
            baoCaoService = new BaoCaoServiceImpl();
            initUI();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo dịch vụ báo cáo: " + e.getMessage());
        }
    }

    private void initUI() {
        setTitle("Báo Cáo Thống Kê");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIUtils.BACKGROUND_COLOR);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        JLabel lblTitle = new JLabel("BÁO CÁO DOANH THU");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.PRIMARY_COLOR);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // --- CENTER (Filter & Table) ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(0, 30, 30, 30));

        // Filter Card
        JPanel filterCard = UIUtils.createCardPanel();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        filterCard.setBorder(new EmptyBorder(5, 10, 5, 10));

        txtTuNgay = new JTextField(10);
        txtDenNgay = new JTextField(10);
        UIUtils.styleTextField(txtTuNgay);
        UIUtils.styleTextField(txtDenNgay);
        
        txtTuNgay.setText(DateUtils.formatDate(LocalDate.now().minusDays(30)));
        txtDenNgay.setText(DateUtils.formatDate(LocalDate.now()));

        JButton btnXem = UIUtils.createButton("Xem Báo Cáo", UIUtils.PRIMARY_COLOR);
        btnXem.addActionListener(e -> loadData());

        filterCard.add(createLabel("Từ ngày:"));
        filterCard.add(txtTuNgay);
        filterCard.add(createLabel("Đến ngày:"));
        filterCard.add(txtDenNgay);
        filterCard.add(btnXem);

        centerPanel.add(filterCard, BorderLayout.NORTH);

        // Table Card
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout());
        
        String[] columns = {"Loại Xe", "Doanh Thu (VNĐ)"};
        modelDoanhThu = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableDoanhThu = new JTable(modelDoanhThu);
        UIUtils.styleTable(tableDoanhThu);
        
        JScrollPane scrollPane = new JScrollPane(tableDoanhThu);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableCard.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(tableCard, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(0, 30, 30, 30));

        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongDoanhThu.setForeground(UIUtils.SECONDARY_COLOR);
        
        footerPanel.add(lblTongDoanhThu);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIUtils.FONT_BOLD);
        lbl.setForeground(UIUtils.TEXT_COLOR);
        return lbl;
    }

    private void loadData() {
        try {
            LocalDate fromDate = DateUtils.parseDate(txtTuNgay.getText());
            LocalDate toDate = DateUtils.parseDate(txtDenNgay.getText());
            
            LocalDateTime fromDateTime = LocalDateTime.of(fromDate, LocalTime.MIN);
            LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.MAX);

            Map<String, Double> data = baoCaoService.baoCaoDoanhThu(fromDateTime, toDateTime);
            
            modelDoanhThu.setRowCount(0);
            double total = 0;

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                modelDoanhThu.addRow(new Object[]{entry.getKey(), String.format("%,.0f", entry.getValue())});
                total += entry.getValue();
            }

            lblTongDoanhThu.setText("Tổng doanh thu: " + String.format("%,.0f VNĐ", total));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng (dd/MM/yyyy) hoặc dữ liệu!");
        }
    }
}