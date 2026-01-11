package OOP.Parking.view;

import OOP.Parking.model.VeXe;
import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.ParkingServiceImpl;
import OOP.Parking.util.DateUtils;
import OOP.Parking.util.UIUtils;
import OOP.Parking.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class QuanLyVePanel extends JPanel {
    private ParkingService parkingService;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaVe, txtBienSo, txtChuXe, txtSDT;
    private JComboBox<String> cbLoaiXe;

    public QuanLyVePanel() {
        try {
            parkingService = new ParkingServiceImpl();
            initUI();
            loadData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage());
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.BACKGROUND_COLOR);

        // --- LEFT PANEL (Form) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblHeader = new JLabel("THÔNG TIN VÉ");
        lblHeader.setFont(UIUtils.FONT_TITLE);
        lblHeader.setForeground(UIUtils.PRIMARY_COLOR);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblHeader);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Fields
        addFormField(leftPanel, "Mã Vé", txtMaVe = new JTextField());
        addFormField(leftPanel, "Biển Số", txtBienSo = new JTextField());
        addFormField(leftPanel, "Chủ Xe", txtChuXe = new JTextField());
        addFormField(leftPanel, "Số Điện Thoại", txtSDT = new JTextField());
        
        JLabel lblLoaiXe = new JLabel("Loại Xe");
        lblLoaiXe.setFont(UIUtils.FONT_BOLD);
        lblLoaiXe.setForeground(UIUtils.TEXT_COLOR);
        lblLoaiXe.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblLoaiXe);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        cbLoaiXe = new JComboBox<>(new String[]{"Xe Máy", "Ô tô"});
        cbLoaiXe.setFont(UIUtils.FONT_NORMAL);
        cbLoaiXe.setBackground(Color.WHITE);
        cbLoaiXe.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbLoaiXe.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(cbLoaiXe);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons
        JButton btnThem = UIUtils.createButton("ĐĂNG KÝ MỚI", UIUtils.PRIMARY_COLOR);
        JButton btnGiaHan = UIUtils.createButton("GIA HẠN 1 THÁNG", UIUtils.SECONDARY_COLOR);
        JButton btnLamMoi = UIUtils.createButton("LÀM MỚI", Color.LIGHT_GRAY);
        
        btnThem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnGiaHan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLamMoi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btnThem.addActionListener(e -> dangKyVe());
        btnGiaHan.addActionListener(e -> giaHanVe());
        btnLamMoi.addActionListener(e -> clearForm());

        leftPanel.add(btnThem);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnGiaHan);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnLamMoi);
        leftPanel.add(Box.createVerticalGlue());

        add(leftPanel, BorderLayout.WEST);

        // --- RIGHT PANEL (Table) ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        rightPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblList = new JLabel("Danh Sách Vé Tháng");
        lblList.setFont(UIUtils.FONT_HEADER);
        lblList.setForeground(UIUtils.TEXT_COLOR);
        rightPanel.add(lblList, BorderLayout.NORTH);

        String[] columns = {"Mã Vé", "Biển Số", "Chủ Xe", "SĐT", "Loại Xe", "Hết Hạn"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        UIUtils.styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Card wrapper
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout());
        card.add(scrollPane);
        
        rightPanel.add(card, BorderLayout.CENTER);
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
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void loadData() {
        model.setRowCount(0);
        List<VeXe> list = parkingService.getDanhSachVeThang();
        for (VeXe ve : list) {
            model.addRow(new Object[]{
                    ve.getMaVe(),
                    ve.getBienSo(),
                    ve.getChuXe(),
                    ve.getSoDienThoai(),
                    ve.getLoaiXe(),
                    DateUtils.formatDate(ve.getNgayHetHan())
            });
        }
    }

    private void dangKyVe() {
        if (!validateInput()) return;

        VeXe ve = new VeXe(
                txtMaVe.getText(),
                txtBienSo.getText(),
                cbLoaiXe.getSelectedItem().toString(),
                "Tháng",
                LocalDate.now().plusMonths(1),
                txtChuXe.getText(),
                txtSDT.getText()
        );

        if (parkingService.dangKyVeThang(ve)) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Mã vé đã tồn tại hoặc lỗi hệ thống!");
        }
    }

    private void giaHanVe() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn vé cần gia hạn!");
            return;
        }
        String maVe = model.getValueAt(row, 0).toString();
        if (parkingService.giaHanVeThang(maVe, 1)) {
            JOptionPane.showMessageDialog(this, "Gia hạn thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi gia hạn!");
        }
    }

    private boolean validateInput() {
        if (!ValidationUtils.isNotEmpty(txtMaVe.getText()) || !ValidationUtils.isNotEmpty(txtBienSo.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtMaVe.setText("");
        txtBienSo.setText("");
        txtChuXe.setText("");
        txtSDT.setText("");
        cbLoaiXe.setSelectedIndex(0);
    }
}