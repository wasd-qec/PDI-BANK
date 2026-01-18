package GUI;

import javax.swing.*;
import java.awt.*;

public class TransferTransactionModal extends JDialog {

    public TransferTransactionModal(JFrame parent) {
        super(parent, false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(550, 470);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 550, 470, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panel.setBounds(0, 0, 550, 470);
        add(panel);

        JLabel title = new JLabel("Creating Transfer Transaction", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(0, 35, 550, 30);
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(40, 80, 470, 1);
        panel.add(line);

        JLabel senderLabel = new JLabel("Sender Account number");
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        senderLabel.setForeground(new Color(10, 32, 68));
        senderLabel.setBounds(50, 100, 400, 25);
        panel.add(senderLabel);

        JTextField senderField = styledInput("Enter sender account number");
        senderField.setBounds(50, 130, 450, 45);
        panel.add(senderField);

        JLabel recLabel = new JLabel("Recipient Account number");
        recLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recLabel.setForeground(new Color(10, 32, 68));
        recLabel.setBounds(50, 190, 400, 25);
        panel.add(recLabel);

        JTextField recField = styledInput("Enter recipient account number");
        recField.setBounds(50, 220, 450, 45);
        panel.add(recField);

        JLabel amtLabel = new JLabel("Amount");
        amtLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amtLabel.setForeground(new Color(10, 32, 68));
        amtLabel.setBounds(50, 280, 200, 25);
        panel.add(amtLabel);

        JTextField amtField = styledInput("Enter amount");
        amtField.setBounds(50, 310, 450, 45);
        panel.add(amtField);

        JButton cancelBtn = styledDarkBtn("Cancel");
        cancelBtn.setBounds(180, 380, 90, 40);
        cancelBtn.addActionListener(e -> dispose());
        panel.add(cancelBtn);

        JButton createBtn = styledDarkBtn("Create");
        createBtn.setBounds(290, 380, 90, 40);
        panel.add(createBtn);

        createBtn.addActionListener(e -> {
            dispose();
            new TransactionSuccessPopup(parent);
        });

        setVisible(true);
        toFront();
        requestFocus();
    }

    private JTextField styledInput(String placeholder) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        field.setBackground(new Color(218, 186, 121));
        field.setForeground(new Color(80, 80, 80));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(80, 80, 80));
                }
            }
        });
        return field;
    }

    private JButton styledDarkBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(8, 25, 64));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return btn;
    }
}
