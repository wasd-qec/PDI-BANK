import javax.swing.*;
import java.awt.*;

public class TransferCustomer extends JDialog {

    public TransferCustomer(JFrame parent) {
        super(parent, true);
        setSize(550, 420);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 550, 420, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        add(panel);

        JLabel title = new JLabel("Transfer", SwingConstants.LEFT);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(40, 30, 400, 40);
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(40, 75, 470, 1);
        panel.add(line);

        JLabel recLabel = new JLabel("Recipient Account number");
        recLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recLabel.setForeground(new Color(10, 32, 68));
        recLabel.setBounds(50, 95, 400, 25);
        panel.add(recLabel);

        JTextField recField = styledInput("Enter recipient account number");
        recField.setBounds(50, 125, 450, 45);
        panel.add(recField);

        JLabel amtLabel = new JLabel("Amount");
        amtLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amtLabel.setForeground(new Color(10, 32, 68));
        amtLabel.setBounds(50, 185, 200, 25);
        panel.add(amtLabel);

        JTextField amtField = styledInput("Enter amount");
        amtField.setBounds(50, 215, 450, 45);
        panel.add(amtField);

        JButton cancelBtn = styledRoundedBtn("Cancel", new Color(74, 85, 110));
        cancelBtn.setBounds(180, 300, 100, 40);
        cancelBtn.addActionListener(e -> dispose());
        panel.add(cancelBtn);

        JButton transferBtn = styledRoundedBtn("Transfer", new Color(8, 25, 64));
        transferBtn.setBounds(290, 300, 100, 40);
        panel.add(transferBtn);

        // Transfer → close + success popup
        transferBtn.addActionListener(e -> {
            dispose();
            new SuccesPopupCustomerTS(parent);
        });

        setVisible(true);
    }

    private JTextField styledInput(String placeholder) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        field.setBackground(new Color(218, 186, 121));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setOpaque(true);

        field.setText(placeholder);
        field.setForeground(new Color(80, 80, 80));

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

    private JButton styledRoundedBtn(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }
}
