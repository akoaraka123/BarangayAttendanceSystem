package attendance;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    
    // Modern color palette
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Blue
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219);    // Light Blue
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);       // Green
    public static final Color WARNING_COLOR = new Color(241, 196, 15);      // Yellow
    public static final Color DANGER_COLOR = new Color(231, 76, 60);        // Red
    public static final Color LIGHT_COLOR = new Color(236, 240, 241);       // Light Gray
    public static final Color DARK_COLOR = new Color(44, 62, 80);          // Dark Blue
    public static final Color TEXT_COLOR = new Color(52, 73, 94);           // Dark Text
    public static final Color BACKGROUND_COLOR = new Color(255, 255, 255);  // White
    
    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FIELD_FONT = new Font("Segoe UI", Font.BOLD, 18);
    
    /**
     * Style a button with modern design
     */
    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }
    
    /**
     * Style a success button
     */
    public static void styleSuccessButton(JButton button) {
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(46, 204, 113));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR);
            }
        });
    }
    
    /**
     * Style a danger button
     */
    public static void styleDangerButton(JButton button) {
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER_COLOR);
            }
        });
    }
    
    /**
     * Style a text field
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_COLOR, 3),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
    }
    
    /**
     * Style a password field
     */
    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_COLOR, 3),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
    }
    
    /**
     * Style a label
     */
    public static void styleLabel(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    /**
     * Style a header label
     */
    public static void styleHeaderLabel(JLabel label) {
        label.setFont(HEADER_FONT);
        label.setForeground(PRIMARY_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    /**
     * Style a title label
     */
    public static void styleTitleLabel(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(DARK_COLOR);
    }
    
    /**
     * Create a modern panel with gradient background
     */
    public static JPanel createModernPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, BACKGROUND_COLOR, 
                                                         getWidth(), getHeight(), LIGHT_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    /**
     * Create a header panel with solid color
     */
    public static JPanel createHeaderPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    /**
     * Show a modern message dialog
     */
    public static void showModernMessageDialog(Component parent, String message, String title, int messageType) {
        // Customize dialog colors based on message type
        Color headerColor = PRIMARY_COLOR;
        String iconText = "ℹ️";
        
        switch (messageType) {
            case JOptionPane.INFORMATION_MESSAGE:
                headerColor = SUCCESS_COLOR;
                iconText = "✅";
                break;
            case JOptionPane.WARNING_MESSAGE:
                headerColor = WARNING_COLOR;
                iconText = "⚠️";
                break;
            case JOptionPane.ERROR_MESSAGE:
                headerColor = DANGER_COLOR;
                iconText = "❌";
                break;
        }
        
        // Create custom dialog
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 250);
        dialog.setLocationRelativeTo(parent);
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerColor);
        headerPanel.setPreferredSize(new Dimension(500, 50));
        
        JLabel iconLabel = new JLabel(iconText + " " + title);
        iconLabel.setFont(TITLE_FONT);
        iconLabel.setForeground(Color.WHITE);
        headerPanel.add(iconLabel);
        
        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messagePanel.setBackground(BACKGROUND_COLOR);
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; padding: 10px; font-size: 16px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(TEXT_COLOR);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton okButton = new JButton("OK");
        ThemeManager.styleButton(okButton);
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(messagePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Show a large input dialog
     */
    public static String showLargeInputDialog(Component parent, String message, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><div style='font-size: 18px; margin-bottom: 10px;'>" + message + "</div></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(TEXT_COLOR);
        
        JTextField textField = new JTextField();
        ThemeManager.styleTextField(textField);
        textField.setPreferredSize(new Dimension(400, 50));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(
            parent, 
            panel, 
            title, 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        return result == JOptionPane.OK_OPTION ? textField.getText().trim() : null;
    }
}
