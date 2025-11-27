package attendance;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtOldPassword, txtNewPassword, txtConfirmPassword;
    private JButton btnChange;
    private JLabel lblStatus;

    public ChangePasswordFrame() {
        setTitle("Change Password");
        setSize(550, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create modern panel
        JPanel mainPanel = ThemeManager.createModernPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = ThemeManager.createHeaderPanel();
        headerPanel.setPreferredSize(new Dimension(550, 80));
        
        JLabel headerLabel = new JLabel("🔐 Change Password", SwingConstants.CENTER);
        ThemeManager.styleHeaderLabel(headerLabel);
        headerPanel.add(headerLabel);

        // Form panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeManager.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblEmail = new JLabel("📧 Email:");
        ThemeManager.styleLabel(lblEmail);
        panel.add(lblEmail, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmail = new JTextField(25);
        ThemeManager.styleTextField(txtEmail);
        panel.add(txtEmail, gbc);

        // Old Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblOldPassword = new JLabel("🔑 Old Password:");
        ThemeManager.styleLabel(lblOldPassword);
        panel.add(lblOldPassword, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtOldPassword = new JPasswordField(25);
        ThemeManager.stylePasswordField(txtOldPassword);
        panel.add(txtOldPassword, gbc);

        // New Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblNewPassword = new JLabel("🔒 New Password:");
        ThemeManager.styleLabel(lblNewPassword);
        panel.add(lblNewPassword, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNewPassword = new JPasswordField(25);
        ThemeManager.stylePasswordField(txtNewPassword);
        panel.add(txtNewPassword, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblConfirmPassword = new JLabel("✅ Confirm Password:");
        ThemeManager.styleLabel(lblConfirmPassword);
        panel.add(lblConfirmPassword, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtConfirmPassword = new JPasswordField(25);
        ThemeManager.stylePasswordField(txtConfirmPassword);
        panel.add(txtConfirmPassword, gbc);

        // Status Label
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatus.setForeground(Color.RED);
        panel.add(lblStatus, gbc);

        // Change Button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        btnChange = new JButton("🔐 Change Password");
        ThemeManager.styleButton(btnChange);
        btnChange.setPreferredSize(new Dimension(200, 50));
        panel.add(btnChange, gbc);

        // Password strength checker
        txtNewPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String password = new String(txtNewPassword.getPassword());
                String message = PasswordUtils.getPasswordStrengthMessage(password);
                lblStatus.setText(message);
                lblStatus.setForeground(message.contains("✓") ? Color.GREEN : Color.RED);
            }
        });

        btnChange.addActionListener(e -> changePassword());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void changePassword() {
        String email = txtEmail.getText().trim();
        String oldPassword = new String(txtOldPassword.getPassword()).trim();
        String newPassword = new String(txtNewPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

        // Validation
        if (email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            lblStatus.setText("All fields are required!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            lblStatus.setText("New passwords do not match!");
            return;
        }

        if (!PasswordUtils.isPasswordStrong(newPassword)) {
            lblStatus.setText(PasswordUtils.getPasswordStrengthMessage(newPassword));
            return;
        }

        // Change password
        btnChange.setEnabled(false);
        btnChange.setText("Changing...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return DatabaseOperations.changePassword(email, oldPassword, newPassword);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    btnChange.setEnabled(true);
                    btnChange.setText("Change Password");
                    
                    if (success) {
                        JOptionPane.showMessageDialog(ChangePasswordFrame.this, 
                            "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        lblStatus.setText("Invalid email or old password!");
                    }
                } catch (Exception ex) {
                    btnChange.setEnabled(true);
                    btnChange.setText("Change Password");
                    lblStatus.setText("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
}
