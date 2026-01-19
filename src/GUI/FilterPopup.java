import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class FilterPopup extends JDialog {

    public FilterPopup(JFrame parent) {
        super(parent, true);
        setSize(680, 430);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 80)); // translucent overlay

        setLayout(null);

        // Card container
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // subtle shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
            }
        };

        card.setBounds(50, 40, 580, 340);
        card.setOpaque(false);
        add(card);

        // Title
        JLabel title = new JLabel("Filter Transactions");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBounds(25, 15, 400, 30);
        card.add(title);

        // ---- Form Labels ----
        JLabel minLbl = new JLabel("Min Amount:");
        JLabel maxLbl = new JLabel("Max Amount:");
        JLabel typeLbl = new JLabel("Type:");
        JLabel recvLbl = new JLabel("Receiver ID:");
        JLabel sendLbl = new JLabel("Sender ID:");
        JLabel fromLbl = new JLabel("Date From:");
        JLabel toLbl = new JLabel("Date To:");

        JLabel[] labels = { minLbl, maxLbl, typeLbl, recvLbl, sendLbl, fromLbl, toLbl };
        for (JLabel l : labels) {
            l.setFont(new Font("SansSerif", Font.PLAIN, 16));
        }

        int lx = 40, ly = 55, gapY = 45;
        minLbl.setBounds(lx, ly, 140, 30);
        maxLbl.setBounds(310, ly, 140, 30);

        typeLbl.setBounds(lx, ly + gapY, 140, 30);
        recvLbl.setBounds(lx, ly + gapY * 2, 140, 30);
        fromLbl.setBounds(lx, ly + gapY * 3, 140, 30);

        sendLbl.setBounds(310, ly + gapY, 140, 30);
        toLbl.setBounds(310, ly + gapY * 2, 140, 30);

        for (JLabel l : labels)
            card.add(l);

        // ---- Fields ----
        RoundedTextField minField = new RoundedTextField();
        RoundedTextField maxField = new RoundedTextField();
        RoundedComboBox<String> typeBox = new RoundedComboBox<>(
                new String[] { "All", "Deposit", "Withdraw", "Transfer" });
        RoundedTextField recvField = new RoundedTextField();
        RoundedTextField sendField = new RoundedTextField();
        RoundedTextField fromField = new RoundedTextField();
        RoundedTextField toField = new RoundedTextField();

        int fx = 40, fy = 85, w = 220, h = 34;

        minField.setBounds(fx, fy, w, h);
        maxField.setBounds(310, fy, w, h);
        typeBox.setBounds(fx, fy + gapY, w, h);
        recvField.setBounds(fx, fy + gapY * 2, w, h);
        fromField.setBounds(fx, fy + gapY * 3, w, h);

        sendField.setBounds(310, fy + gapY, w, h);
        toField.setBounds(310, fy + gapY * 2, w, h);

        card.add(minField);
        card.add(maxField);
        card.add(typeBox);
        card.add(recvField);
        card.add(sendField);
        card.add(fromField);
        card.add(toField);

        // Date hint
        JLabel hint = new JLabel("Date format: YYYY-MM-DD. Leave empty to skip filter.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hint.setForeground(Color.GRAY);
        hint.setBounds(40, 85 + gapY * 4, 400, 20);
        card.add(hint);

        // Buttons
        JButton cancelBtn = new RoundedButton("Cancel", new Color(235, 239, 244), new Color(70, 70, 70));
        JButton okBtn = new RoundedButton("OK", new Color(15, 35, 85), Color.WHITE);

        cancelBtn.setBounds(260, 260, 120, 38);
        okBtn.setBounds(400, 260, 120, 38);

        cancelBtn.addActionListener(e -> dispose());
        okBtn.addActionListener(e -> dispose());

        card.add(cancelBtn);
        card.add(okBtn);
    }

    // FIELD STYLE
    class RoundedTextField extends JTextField {
        RoundedTextField() {
            setBorder(null);
            setOpaque(false);
            setBackground(new Color(220, 185, 100));
            setFont(new Font("SansSerif", Font.PLAIN, 15));
            setForeground(Color.BLACK);
            setMargin(new Insets(5, 8, 5, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            super.paintComponent(g);
        }
    }

    class RoundedComboBox<E> extends JComboBox<E> {
        RoundedComboBox(E[] items) {
            super(items);
            setOpaque(false);
            setBackground(new Color(220, 185, 100));
        }
    }

    class RoundedButton extends JButton {
        Color bg, fg;

        RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bg = bg;
            this.fg = fg;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            g2.setColor(fg);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;
            g2.drawString(getText(), x, y);
        }
    }
}
