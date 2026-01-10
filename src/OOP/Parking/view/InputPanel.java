package OOP.Parking.view;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    private JLabel label;
    private JTextField textField;

    public InputPanel(String labelText, int columns) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel(labelText);
        textField = new JTextField(columns);
        add(label);
        add(textField);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }
}