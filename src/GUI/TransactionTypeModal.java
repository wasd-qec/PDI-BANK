package GUI;

import javax.swing.*;
import java.awt.*;

public class TransactionTypeModal extends JDialog {

    public TransactionTypeModal(JFrame parent) {
        super(parent, true);
        setSize(500, 270);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 500, 300, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 3, getWidth()-3, getHeight()-3, 35, 35);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 35, 35);
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

        depositBtn.addActionListener(e -> {
            dispose(); 
            new DepositTransactionModal(parent); 
        });

        withdrawBtn.addActionListener(e -> {
            dispose();
            new WithdrawTransactionModal(parent);
        });

        transferBtn.addActionListener(e -> {
            dispose();
            new TransferTransactionModal(parent);
        });

        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(transferBtn);

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
        
        showWithAnimation();
    }

    private JButton styledBtn(String text, int x, int y) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(8, 25, 64));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                super.paintComponent(g);
            }
        };
        btn.setBounds(x, y, 110, 50);
        btn.setForeground(new Color(218, 186, 121)); // Gold/tan text
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showWithAnimation() {
        setVisible(true);
        Timer fadeInTimer = new Timer(10, e -> {
            float opacity = getOpacity();
            opacity += 0.05f;
            if (opacity >= 1.0f) {
                opacity = 1.0f;
                ((Timer) e.getSource()).stop();
            }
            setOpacity(opacity);
        });
        fadeInTimer.start();
    }   
}