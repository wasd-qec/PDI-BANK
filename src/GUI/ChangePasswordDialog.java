package GUI;
import javax.swing.*;
import java.awt.*;
import Object.Customer;
import Database.CustomerHandling;
import Security.PasswordEncryption;

public class ChangePasswordDialog extends JDialog {
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private Customer customer;
    private CustomerHandling customerHandling;

    public ChangePasswordDialog(JFrame parent, Customer customer) {
        super(parent, "Change Password", true);
        this.customer = customer;
        this.customerHandling = new CustomerHandling();
        setSize(430, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 240, 235));
        setLayout(null);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 200, 25);
        add(titleLabel);

        JLabel oldPasswordLabel = new JLabel("Old Password");
        oldPasswordLabel.setForeground(new Color(30, 50, 85));
        oldPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        oldPasswordLabel.setBounds(20, 70, 150, 15);
        add(oldPasswordLabel);

        oldPasswordField = new JPasswordField();
        oldPasswordField.setBounds(20, 90, 380, 35);
        oldPasswordField.setBackground(new Color(218, 186, 121));
        oldPasswordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        oldPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        oldPasswordField.setForeground(Color.BLACK);
        add(oldPasswordField);

        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setForeground(new Color(30, 50, 85));
        newPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        newPasswordLabel.setBounds(20, 140, 150, 15);
        add(newPasswordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(20, 160, 380, 35);
        newPasswordField.setBackground(new Color(218, 186, 121));
        newPasswordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        newPasswordField.setForeground(Color.BLACK);
        add(newPasswordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm New Password");
        confirmPasswordLabel.setForeground(new Color(30, 50, 85));
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        confirmPasswordLabel.setBounds(20, 210, 150, 15);
        add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(20, 230, 380, 35);
        confirmPasswordField.setBackground(new Color(218, 186, 121));
        confirmPasswordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        confirmPasswordField.setForeground(Color.BLACK);
        add(confirmPasswordField);

        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBackground(new Color(108, 130, 173));
        cancelBtn.setBounds(80, 300, 110, 35);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        RoundedButton changeBtn = new RoundedButton("Change");
        changeBtn.setBackground(new Color(8, 25, 64));
        changeBtn.setBounds(220, 300, 110, 35);
        changeBtn.addActionListener(e -> handleChangePassword());
        add(changeBtn);
    }

    private void handleChangePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showErrorDialog("All fields are required!");
            return;
        }

        if (newPassword.length() < 6) {
            showErrorDialog("New password must be at least 6 characters long!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showErrorDialog("New password and confirm password do not match!");
            return;
        }

        if (oldPassword.equals(newPassword)) {
            showErrorDialog("New password cannot be the same as old password!");
            return;
        }

        String storedPassword = customerHandling.getPasswordByAccNo(customer.getAccNo());
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        
        if (storedPassword == null) {
            showErrorDialog("Unable to verify password!");
            return;
        }
        
        if (!passwordEncryption.verifyPassword(oldPassword, storedPassword)) {
            showErrorDialog("Old password is incorrect!");
            return;
        }

        try {
            String encryptedNewPassword = passwordEncryption.encryptPassword(newPassword);
            customerHandling.updatePassword(customer.getAccNo(), encryptedNewPassword);
            
            dispose();
            showSuccessDialog("Password changed successfully!");
        } catch (Exception ex) {
            showErrorDialog("Failed to update password: " + ex.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        JDialog errorDialog = new JDialog(this, "Error", true);
        errorDialog.setSize(350, 240);
        errorDialog.setLocationRelativeTo(this);
        errorDialog.setResizable(false);
        errorDialog.getContentPane().setBackground(new Color(245, 240, 235));
        errorDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Error");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(205, 0, 20));
        titleLabel.setBounds(20, 20, 150, 25);
        errorDialog.add(titleLabel);

        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setForeground(new Color(30, 50, 85));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setBounds(20, 60, 310, 60);
        errorDialog.add(messageLabel);

        RoundedButton okBtn = new RoundedButton("OK");
        okBtn.setBackground(new Color(108, 130, 173));
        okBtn.setBounds(125, 130, 100, 35);
        okBtn.addActionListener(e -> errorDialog.dispose());
        errorDialog.add(okBtn);

        errorDialog.setVisible(true);
    }

    private void showSuccessDialog(String message) {
        JDialog successDialog = new JDialog(this, "Success", true);
        successDialog.setSize(350, 180);
        successDialog.setLocationRelativeTo(this);
        successDialog.setResizable(false);
        successDialog.getContentPane().setBackground(new Color(245, 240, 235));
        successDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Success");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(34, 139, 34));
        titleLabel.setBounds(20, 20, 150, 25);
        successDialog.add(titleLabel);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(new Color(30, 50, 85));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setBounds(20, 60, 310, 60);
        successDialog.add(messageLabel);

        RoundedButton okBtn = new RoundedButton("OK");
        okBtn.setBackground(new Color(34, 139, 34));
        okBtn.setBounds(125, 130, 100, 35);
        okBtn.addActionListener(e -> successDialog.dispose());
        successDialog.add(okBtn);

        successDialog.setVisible(true);
    }

    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setBackground(new Color(108, 130, 173));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}