import javax.swing.*;
import java.awt.*;

public class LoginSelection {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Login");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel background = new JPanel();
        background.setLayout(new GridBagLayout());
        background.setBackground(new Color(96, 129, 146));

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(350, 380));
        card.setBackground(new Color(58, 92, 110));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        ImageIcon abaIcon = new ImageIcon("aba.png"); // make sure image exists
        Image abaImage = abaIcon.getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(abaImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(30));
        card.add(logoLabel);

        card.add(Box.createVerticalStrut(20));
        JLabel loginAsLabel = new JLabel("Login as");
        loginAsLabel.setForeground(new Color(30, 60, 80));
        loginAsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginAsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginAsLabel);

        card.add(Box.createVerticalStrut(40));

        JButton customerBtn = new JButton("Customer");
        customerBtn.setMaximumSize(new Dimension(200, 40));
        customerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton adminBtn = new JButton("Admin");
        adminBtn.setMaximumSize(new Dimension(200, 40));
        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(customerBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(adminBtn);

        background.add(card);

        frame.add(background);
        frame.setVisible(true);
    }
}
