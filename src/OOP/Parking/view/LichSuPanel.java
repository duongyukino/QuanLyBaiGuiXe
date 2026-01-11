package OOP.Parking.view;

import OOP.Parking.DAO.LichSuDAO;
import OOP.Parking.model.LichSu;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LichSuPanel extends JPanel {
    private LichSuDAO lichSuDAO;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTuNgay, txtDenNgay, txtBienSo;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public LichSuPanel() {
        try {
            lichSuDAO = LichSuDAO.getInstance();
            initUI();
            loadData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage());
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.BACKGROUND_COLOR);

        // --- HEADER (Filter) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        headerPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtTuNgay = new JTextField(10);
        txtDenNgay = new JTextField(10);
        txtBienSo = new JTextField(10);
        
        UIUtils.styleTextField(txtTuNgay);
        UIUtils.styleTextField(txtDenNgay);
        UIUtils.styleTextField(txtBienSo);

        txtTuNgay.setText(DateUtils.formatDate(LocalDate.now().minusDays(7)));
        txtDenNgay.setText(DateUtils.formatDate(LocalDate.now()));

        JButton btnXem = UIUtils.createButton("Tìm Kiếm", UIUtils.PRIMARY_COLOR);
        btnXem.addActionListener(e -> loadData());

        headerPanel.add(new JLabel("Từ ngày:"));
        headerPanel.add(txtTuNgay);
        headerPanel.add(new JLabel("Đến ngày:"));
        headerPanel.add(txtDenNgay);
        headerPanel.add(new JLabel("Biển số:"));
        headerPanel.add(txtBienSo);
        headerPanel.add(btnXem);

        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIUtils.BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        String[] columns = {"Thời Gian", "Loại Thao Tác", "Biển Số", "Phí (VNĐ)"};
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
        try {
            String bienSoFilter = txtBienSo.getText().trim().toUpperCase();
            
            LocalDate fromDate = DateUtils.parseDate(txtTuNgay.getText());
            LocalDate toDate = DateUtils.parseDate(txtDenNgay.getText());
            
            LocalDateTime fromDateTime = LocalDateTime.of(fromDate, LocalTime.MIN);
            LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.MAX);

            List<LichSu> list = lichSuDAO.getLichSuByDate(fromDateTime, toDateTime);
            
            model.setRowCount(0);
            for (LichSu ls : list) {
                if (!bienSoFilter.isEmpty() && !ls.getBienSo().toUpperCase().contains(bienSoFilter)) {
                    continue;
                }

                model.addRow(new Object[]{
                        ls.getThoiGian().format(dtf),
                        ls.getLoaiThaoTac(),
                        ls.getBienSo(),
                        String.format("%,.0f", ls.getPhi())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng (dd/MM/yyyy)!");
        }
    }
}