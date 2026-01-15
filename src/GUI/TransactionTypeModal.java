import javax.swing.*;
import java.awt.*;

public class TransactionTypeModal extends JDialog {

    public TransactionTypeModal(JFrame parent) {
        super(parent, true);
        setSize(500, 300);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 500, 300, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panel.setBounds(0, 0, 500, 300);
        add(panel);

        JLabel title = new JLabel("Transaction Type", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setBounds(0, 30, 500, 30);
        title.setForeground(new Color(10, 32, 68));
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(30, 70, 440, 1);
        panel.add(line);

        JButton depositBtn = styledBtn("Deposit", 80, 110);
        JButton withdrawBtn = styledBtn("Withdraw", 195, 110);
        JButton transferBtn = styledBtn("Transfer", 310, 110);

        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(transferBtn);

        // Cancel label
        JLabel cancelLabel = new JLabel("Cancel", SwingConstants.CENTER);
        cancelLabel.setBounds(0, 200, 500, 25);
        cancelLabel.setForeground(new Color(10, 32, 68));
        cancelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(cancelLabel);

        cancelLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private JButton styledBtn(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 110, 40);
        btn.setBackground(new Color(8, 25, 64));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
