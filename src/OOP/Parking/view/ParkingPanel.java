package OOP.Parking.view;

import OOP.Parking.model.Oto;
import OOP.Parking.model.PhuongTien;
import OOP.Parking.model.VeXe;
import OOP.Parking.model.XeMay;
import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.ParkingServiceImpl;
import OOP.Parking.util.DateUtils;
import OOP.Parking.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParkingPanel extends JPanel {
    private ParkingService service;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtBienSo;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ParkingPanel() {
        try {
            service = new ParkingServiceImpl();
            initUI();
            loadData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage());
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.BACKGROUND_COLOR);

        // --- LEFT PANEL (Input) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblHeader = new JLabel(UIUtils.ICON_ADD + " CHECK IN");
        lblHeader.setFont(UIUtils.FONT_TITLE);
        lblHeader.setForeground(UIUtils.PRIMARY_COLOR);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblHeader);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Fields
        addFormField(leftPanel, "Biển Số Xe", txtBienSo = new JTextField());

        leftPanel.add(Box.createVerticalGlue());

        // Buttons with Icons
        JButton btnKiemTraVe = UIUtils.createButton(UIUtils.ICON_SEARCH + " KIỂM TRA VÉ", UIUtils.PRIMARY_COLOR);
        JButton btnXeMay = UIUtils.createButton(UIUtils.ICON_MOTO + " XE MÁY VÀO", UIUtils.SECONDARY_COLOR);
        JButton btnOto = UIUtils.createButton(UIUtils.ICON_CAR + " Ô TÔ VÀO", UIUtils.WARNING_COLOR);
        JButton btnClear = UIUtils.createButton(UIUtils.ICON_REFRESH + " LÀM MỚI", Color.LIGHT_GRAY);
        
        btnKiemTraVe.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnXeMay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnOto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btnKiemTraVe.addActionListener(e -> kiemTraVe());
        btnXeMay.addActionListener(e -> guiXeMay());
        btnOto.addActionListener(e -> guiOTo());
        btnClear.addActionListener(e -> clearForm());

        leftPanel.add(btnKiemTraVe);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnXeMay);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnOto);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnClear);

        add(leftPanel, BorderLayout.WEST);

        // --- RIGHT PANEL (Table) ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header & Action
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.BACKGROUND_COLOR);
        
        JLabel lblList = new JLabel(UIUtils.ICON_PARKING + " Danh Sách Xe Trong Bãi");
        lblList.setFont(UIUtils.FONT_HEADER);
        lblList.setForeground(UIUtils.TEXT_COLOR);
        
        JButton btnThanhToan = UIUtils.createButton(UIUtils.ICON_MONEY + " THANH TOÁN & RA", UIUtils.ACCENT_COLOR);
        btnThanhToan.addActionListener(e -> thanhToan());
        
        topBar.add(lblList, BorderLayout.WEST);
        topBar.add(btnThanhToan, BorderLayout.EAST);
        rightPanel.add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Loại Xe", "Biển Số", "Giờ Vào", "Mã Vé"};
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

    private void kiemTraVe() {
        if (!validateInput()) return;
        String bienSo = txtBienSo.getText().trim();
        VeXe ve = service.findVeThangByBienSo(bienSo);
        if (ve != null) {
            JOptionPane.showMessageDialog(this, 
                "Xe có vé tháng!\nMã vé: " + ve.getMaVe() + 
                "\nChủ xe: " + ve.getChuXe() + 
                "\nHết hạn: " + DateUtils.formatDate(ve.getNgayHetHan()), 
                "Thông tin vé", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Xe không có vé tháng (hoặc vé đã hết hạn).", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guiXeMay() {
        if (validateInput()) {
            XeMay xe = new XeMay(txtBienSo.getText(), "Khách vãng lai");
            if (service.xeVao(xe)) {
                loadData();
                checkVeThang(xe.getBienSo());
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Xe đã tồn tại hoặc bãi đầy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guiOTo() {
        if (validateInput()) {
            Oto xe = new Oto(txtBienSo.getText(), "Khách vãng lai", 4);
            if (service.xeVao(xe)) {
                loadData();
                checkVeThang(xe.getBienSo());
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Xe đã tồn tại hoặc bãi đầy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void checkVeThang(String bienSo) {
        PhuongTien xeVuaVao = service.timXe(bienSo);
        if (xeVuaVao != null && xeVuaVao.getMaVe() != null && !xeVuaVao.getMaVe().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Check-in thành công!\nXe có vé tháng: " + xeVuaVao.getMaVe());
        } else {
            JOptionPane.showMessageDialog(this, "Check-in thành công (Vé lượt)!");
        }
    }

    private void thanhToan() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn xe cần thanh toán trong bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String bienSo = tableModel.getValueAt(row, 1).toString();
        if (JOptionPane.showConfirmDialog(this, "Xuất xe " + bienSo + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            double phi = service.xeRa(bienSo, LocalDateTime.now());
            loadData();
            if (phi == 0) {
                JOptionPane.showMessageDialog(this, "Xuất bến thành công!\nPhí: 0 VNĐ (Vé tháng hoặc miễn phí)");
            } else {
                JOptionPane.showMessageDialog(this, "Xuất bến thành công!\nPhí: " + String.format("%,.0f VNĐ", phi));
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<PhuongTien> list = service.getXeTrongBai();
        for (PhuongTien xe : list) {
            String maVe = (xe.getMaVe() != null && !xe.getMaVe().isEmpty()) ? xe.getMaVe() : "Vé lượt";
            tableModel.addRow(new Object[]{xe.getLoaiXe(), xe.getBienSo(), xe.getThoiGianVao().format(dtf), maVe});
        }
    }

    private boolean validateInput() {
        if (txtBienSo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Biển số!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtBienSo.setText("");
        txtBienSo.requestFocus();
    }
}