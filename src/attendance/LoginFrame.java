package attendance;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Barangay Attendance System - Login");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create modern panel
        JPanel mainPanel = ThemeManager.createModernPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = ThemeManager.createHeaderPanel();
        headerPanel.setPreferredSize(new Dimension(600, 80));
        
        JLabel headerLabel = new JLabel("Barangay Attendance System");
        ThemeManager.styleHeaderLabel(headerLabel);
        headerPanel.add(headerLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ThemeManager.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblEmail = new JLabel("Email:");
        ThemeManager.styleLabel(lblEmail);
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEmail = new JTextField(30);
        ThemeManager.styleTextField(txtEmail);
        formPanel.add(txtEmail, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblPassword = new JLabel("Password:");
        ThemeManager.styleLabel(lblPassword);
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPassword = new JPasswordField(30);
        ThemeManager.stylePasswordField(txtPassword);
        formPanel.add(txtPassword, gbc);

        // Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.weightx = 0;
        btnLogin = new JButton("Login");
        ThemeManager.styleButton(btnLogin);
        btnLogin.setPreferredSize(new Dimension(200, 50));
        formPanel.add(btnLogin, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        btnLogin.addActionListener(e -> authenticate());
        
        // Test database connection on startup
        testDatabaseConnection();
    }
    
    private void testDatabaseConnection() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return DBConnection.testConnection();
            }
            
            @Override
            protected void done() {
                try {
                    boolean connected = get();
                    if (connected) {
                        System.out.println("✅ Database connection test PASSED!");
                        System.out.println("🔍 " + DBConnection.getConnectionStatus());
                    } else {
                        System.err.println("❌ Database connection test FAILED!");
                        System.err.println("🔍 " + DBConnection.getConnectionStatus());
                        
                        // Show user-friendly error message
                        JOptionPane.showMessageDialog(LoginFrame.this,
                            "⚠️ Database Connection Error\n\n" +
                            "Please make sure:\n" +
                            "• MySQL Server is running\n" +
                            "• Database 'attendance_db' exists\n" +
                            "• MySQL user 'root' has proper permissions\n\n" +
                            "For JAR deployment, ensure MySQL Connector/J is included.\n\n" +
                            "Technical Details:\n" + DBConnection.getConnectionStatus(),
                            "Database Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    System.err.println("❌ Database test error: " + e.getMessage());
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "❌ System Error\n\n" +
                        "Unable to test database connection.\n" +
                        "Please check console for details.\n\n" +
                        "Error: " + e.getMessage(),
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void authenticate() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        System.out.println("GUI DEBUG: Email entered: '" + email + "'");
        System.out.println("GUI DEBUG: Password length: " + password.length());
        System.out.println("GUI DEBUG: Password: '" + password + "'");

        // Input validation
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your email address!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        
        if (!InputValidator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, InputValidator.getEmailErrorMessage(email), "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            txtEmail.selectAll();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your password!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            return;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        // Show loading
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                System.out.println("🔍 GUI DEBUG: Starting authentication...");
                if (DatabaseOperations.authenticateUser(email, password)) {
                    String role = DatabaseOperations.getUserRole(email);
                    System.out.println("🔍 GUI DEBUG: Authentication successful, role: " + role);
                    return role;
                }
                System.out.println("🔍 GUI DEBUG: Authentication failed");
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    String role = get();
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login");
                    
                    System.out.println("🔍 GUI DEBUG: Got role from background: " + role);
                    
                    if (role != null) {
                        if (role.equalsIgnoreCase("admin")) {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Welcome Admin!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            new AdminDashboardFrame().setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Welcome Employee!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            new DashboardFrame().setVisible(true);
                        }
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                    }
                } catch (Exception ex) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login");
                    System.out.println("🔍 GUI DEBUG: Exception in done(): " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
