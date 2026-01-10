package OOP.Parking.view;

import OOP.Parking.model.PhuongTien;
import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.ParkingServiceImpl;
import OOP.Parking.util.DateUtils;
import OOP.Parking.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class QuanLyXeFrame extends JFrame {
    private ParkingService parkingService;
    private JTable table;
    private DefaultTableModel model;

    public QuanLyXeFrame() {
        try {
            parkingService = new ParkingServiceImpl();
            initUI();
            loadData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage());
        }
    }

    private void initUI() {
        setTitle("Tra Cứu Xe Trong Bãi");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIUtils.BACKGROUND_COLOR);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        JLabel lblTitle = new JLabel("DANH SÁCH XE ĐANG GỬI");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.PRIMARY_COLOR);
        
        JButton btnRefresh = UIUtils.createButton("Làm Mới", UIUtils.SECONDARY_COLOR);
        btnRefresh.addActionListener(e -> loadData());

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIUtils.BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(0, 30, 30, 30));

        String[] columns = {"Biển Số", "Loại Xe", "Thời Gian Vào", "Mã Vé"};
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
        
        tablePanel.add(card, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        List<PhuongTien> list = parkingService.getXeTrongBai();
        for (PhuongTien pt : list) {
            model.addRow(new Object[]{
                    pt.getBienSo(),
                    pt.getLoaiXe(),
                    DateUtils.formatDateTime(pt.getThoiGianVao()),
                    pt.getMaVe() == null || pt.getMaVe().isEmpty() ? "Vé lượt" : pt.getMaVe()
            });
        }
    }
}