package OOP.Parking.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class UIUtils {
    // Vibrant Color Palette
    public static final Color PRIMARY_COLOR = new Color(52, 152, 219);    // Bright Blue
    public static final Color SECONDARY_COLOR = new Color(46, 204, 113);  // Emerald Green
    public static final Color ACCENT_COLOR = new Color(231, 76, 60);      // Alizarin Red
    public static final Color WARNING_COLOR = new Color(241, 196, 15);    // Sun Flower Yellow
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250); // Clouds
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(44, 62, 80);         // Midnight Blue
    public static final Color TEXT_MUTED = new Color(127, 140, 141);      // Asbestos
    
    // Gradient Colors
    public static final Color GRADIENT_START = new Color(41, 128, 185);   // Belize Hole
    public static final Color GRADIENT_END = new Color(109, 213, 250);    // Light Blue

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_ICON = new Font("Segoe UI Emoji", Font.PLAIN, 24);

    // Icons (Unicode)
    public static final String ICON_PARKING = "🅿️";
    public static final String ICON_CAR = "🚗";
    public static final String ICON_MOTO = "🛵";
    public static final String ICON_BIKE = "🚲";
    public static final String ICON_USER = "👤";
    public static final String ICON_ADMIN = "👮";
    public static final String ICON_SEARCH = "🔍";
    public static final String ICON_CHECK = "✅";
    public static final String ICON_MONEY = "💰";
    public static final String ICON_HISTORY = "📜";
    public static final String ICON_REPORT = "📊";
    public static final String ICON_LOGOUT = "🚪";
    public static final String ICON_HOME = "🏠";
    public static final String ICON_TICKET = "🎫";
    public static final String ICON_REFRESH = "🔄";
    public static final String ICON_WARNING = "⚠️";
    public static final String ICON_ADD = "➕";
    public static final String ICON_TIME = "⏰";

    // Rounded Button
    public static JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // Styled Table
    public static void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_NORMAL);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setSelectionForeground(TEXT_COLOR);

        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(PRIMARY_COLOR);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // Rounded TextField
    public static void styleTextField(JTextField txt) {
        txt.setFont(FONT_NORMAL);
        txt.setForeground(TEXT_COLOR);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txt.setBackground(Color.WHITE);
    }
    
    // Card Panel Helper
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setOpaque(false);
        return panel;
    }
    
    // Gradient Panel Helper
    public static JPanel createGradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, w, h, GRADIENT_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }
}