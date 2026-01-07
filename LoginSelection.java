import javax.swing.*;
import java.awt.*;

public class LoginSelection extends JPanel {

    public LoginSelection(CardLayout cardLayout, JPanel mainPanel) {

        setBackground(new Color(96, 129, 146));
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(400, 350));
        card.setBackground(new Color(58, 92, 110));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Login as");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== LOGO PANEL (TOP-LEFT) =====
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false); // keep card background color

        ImageIcon abaIcon = new ImageIcon("aba.png");
        Image abaImage = abaIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(abaImage));

        logoPanel.add(logoLabel);

        JButton adminBtn = new JButton("Admin");
        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton customerBtn = new JButton("Customer");
        customerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Same size
        Dimension buttonSize = new Dimension(200, 40);
        adminBtn.setPreferredSize(buttonSize);
        customerBtn.setPreferredSize(buttonSize);
        adminBtn.setMaximumSize(buttonSize);
        customerBtn.setMaximumSize(buttonSize);

        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        adminBtn.addActionListener(e -> cardLayout.show(mainPanel, "admin"));

        customerBtn.addActionListener(e -> cardLayout.show(mainPanel, "user"));

        card.add(Box.createVerticalStrut(60));
        card.add(title);
        card.add(Box.createVerticalStrut(30));
        card.add(customerBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(adminBtn);

        add(card);
    }
}
