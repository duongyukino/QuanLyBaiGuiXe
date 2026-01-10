package OOP.Parking.view;

import OOP.Parking.model.Oto;
import OOP.Parking.model.PhuongTien;
import OOP.Parking.model.XeMay;
import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.ParkingServiceImpl;
import OOP.Parking.util.UIUtils;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParkingUI extends JFrame {
    private ParkingService service;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtBienSo, txtChuXe, txtSoCho;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ParkingUI() {
        try {
            service = new ParkingServiceImpl();
            initUI();
            loadData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage());
        }
    }

    private void initUI() {
        setTitle("Quản Lý Xe Vào/Ra");
        setSize(1200, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- LEFT PANEL (Input) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblHeader = new JLabel("CHECK IN");
        lblHeader.setFont(UIUtils.FONT_TITLE);
        lblHeader.setForeground(UIUtils.PRIMARY_COLOR);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblHeader);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Fields
        addFormField(leftPanel, "Biển Số Xe", txtBienSo = new JTextField());
        addFormField(leftPanel, "Chủ Xe (Tùy chọn)", txtChuXe = new JTextField());
        addFormField(leftPanel, "Số Chỗ (Nếu là Ô tô)", txtSoCho = new JTextField());

        leftPanel.add(Box.createVerticalGlue());

        // Buttons
        JButton btnXeMay = UIUtils.createButton("XE MÁY VÀO", UIUtils.SECONDARY_COLOR);
        JButton btnOto = UIUtils.createButton("Ô TÔ VÀO", UIUtils.WARNING_COLOR);
        JButton btnClear = UIUtils.createButton("LÀM MỚI", Color.LIGHT_GRAY);
        
        btnXeMay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnOto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btnXeMay.addActionListener(e -> guiXeMay());
        btnOto.addActionListener(e -> guiOTo());
        btnClear.addActionListener(e -> clearForm());

        leftPanel.add(btnXeMay);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnOto);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnClear);

        add(leftPanel, BorderLayout.WEST);

        // --- RIGHT PANEL (Table) ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        rightPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header & Action
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.BACKGROUND_COLOR);
        JLabel lblList = new JLabel("Danh Sách Xe Trong Bãi");
        lblList.setFont(UIUtils.FONT_HEADER);
        lblList.setForeground(UIUtils.TEXT_COLOR);
        
        JButton btnThanhToan = UIUtils.createButton("THANH TOÁN & RA", UIUtils.ACCENT_COLOR);
        btnThanhToan.addActionListener(e -> thanhToan());
        
        topBar.add(lblList, BorderLayout.WEST);
        topBar.add(btnThanhToan, BorderLayout.EAST);
        rightPanel.add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Loại Xe", "Biển Số", "Chủ Xe", "Giờ Vào", "Chi Tiết"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Card wrapper for table
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.add(scrollPane);
        
        rightPanel.add(tableCard, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, String label, JTextField txt) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.FONT_BOLD);
        lbl.setForeground(UIUtils.TEXT_COLOR);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        UIUtils.styleTextField(txt);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(txt);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    // Logic giữ nguyên
    private void guiXeMay() {
        if (validateInput()) {
            XeMay xe = new XeMay(txtBienSo.getText(), txtChuXe.getText());
            if (service.xeVao(xe)) { loadData(); clearForm(); JOptionPane.showMessageDialog(this, "Thành công!"); }
            else JOptionPane.showMessageDialog(this, "Lỗi: Xe đã tồn tại hoặc bãi đầy!");
        }
    }

    private void guiOTo() {
        if (validateInput()) {
            try {
                int soCho = txtSoCho.getText().isEmpty() ? 4 : Integer.parseInt(txtSoCho.getText());
                Oto xe = new Oto(txtBienSo.getText(), txtChuXe.getText(), soCho);
                if (service.xeVao(xe)) { loadData(); clearForm(); JOptionPane.showMessageDialog(this, "Thành công!"); }
                else JOptionPane.showMessageDialog(this, "Lỗi: Xe đã tồn tại hoặc bãi đầy!");
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Số chỗ phải là số!"); }
        }
    }

    private void thanhToan() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Chọn xe cần thanh toán!"); return; }
        String bienSo = tableModel.getValueAt(row, 1).toString();
        if (JOptionPane.showConfirmDialog(this, "Xuất xe " + bienSo + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            double phi = service.xeRa(bienSo, LocalDateTime.now());
            loadData();
            JOptionPane.showMessageDialog(this, "Phí: " + String.format("%,.0f VNĐ", phi));
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<PhuongTien> list = service.getXeTrongBai();
        for (PhuongTien xe : list) {
            String chiTiet = (xe instanceof Oto) ? ((Oto) xe).getSoCho() + " chỗ" : "-";
            tableModel.addRow(new Object[]{xe.getLoaiXe(), xe.getBienSo(), xe.getChuXe(), xe.getThoiGianVao().format(dtf), chiTiet});
        }
    }

    private boolean validateInput() { return !txtBienSo.getText().trim().isEmpty(); }
    private void clearForm() { txtBienSo.setText(""); txtChuXe.setText(""); txtSoCho.setText(""); txtBienSo.requestFocus(); }
}