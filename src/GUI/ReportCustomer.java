import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportCustomer extends JDialog {

    public ReportCustomer(JFrame parent) {
        super(parent, true);

        setSize(800, 550);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 800, 550, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panel.setBounds(0, 0, 800, 550);
        add(panel);

        JLabel title = new JLabel("Transaction report");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(40, 25, 400, 35);
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(40, 70, 720, 1);
        panel.add(line);

        JLabel sub = new JLabel("Account: Heng (ABC001)");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        sub.setForeground(new Color(10, 32, 68));
        sub.setBounds(40, 85, 400, 30);
        panel.add(sub);

        String[] colNames = { "Transaction ID", "Type", "Amount", "Date" };

        Object[][] data = {
                { "TXN-9F061296", "DEPOSIT", "$5,000", "2026-01-07   16:29:13" },
                { "TXN-17616E2D", "DEPOSIT", "$1,000", "2026-01-01   23:23:03" },
                { "TXN-21FE1BF5", "WITHDRAWAL", "$1,200", "2026-01-01   00:41:25" },
                { "TXN-DBB21933", "TRANSFER", "$1,299", "2026-01-01   00:38:20" },
                { "TXN-817DD6B2", "DEPOSIT", "$1,000", "2026-01-01   00:37:34" },
                { "TXN-ABC21E5C", "TRANSFER", "$1,000", "2026-01-01   00:37:22" },
                { "TXN-63A73BFA", "DEPOSIT", "$1,000", "2025-12-31   11:07:13" },
        };

        DefaultTableModel model = new DefaultTableModel(data, colNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(230, 220, 210));
        table.getTableHeader().setForeground(new Color(10, 32, 68));
        table.setForeground(new Color(206, 140, 0));
        table.setGridColor(new Color(200, 200, 200));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 130, 720, 350);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll);

        // BACK BUTTON
        JButton backBtn = new JButton("Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(8, 25, 64));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBounds(350, 490, 100, 40);

        backBtn.addActionListener(e -> dispose());
        panel.add(backBtn);

        setVisible(true);
    }
}
