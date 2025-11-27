package attendance;

import javax.swing.*;
import java.awt.*;

/**
 * Configuration Dialog para sa RFID Reader
 */
public class RFIDConfigDialog extends JDialog {
    private JComboBox<String> portComboBox;
    private JComboBox<String> baudRateComboBox;
    private String selectedPort;
    private int selectedBaudRate;
    private boolean configured = false;
    
    public RFIDConfigDialog(JFrame parent) {
        super(parent, "RFID Reader Configuration", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Configure RFID Reader", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // COM Port Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("COM Port:"), gbc);
        
        gbc.gridx = 1;
        portComboBox = new JComboBox<>(getAvailablePorts());
        portComboBox.setEditable(true);
        portComboBox.setPreferredSize(new Dimension(150, 30));
        formPanel.add(portComboBox, gbc);
        
        // Baud Rate Selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Baud Rate:"), gbc);
        
        gbc.gridx = 1;
        String[] baudRates = {"9600", "19200", "38400", "57600", "115200"};
        baudRateComboBox = new JComboBox<>(baudRates);
        baudRateComboBox.setSelectedItem("9600"); // Default
        baudRateComboBox.setPreferredSize(new Dimension(150, 30));
        formPanel.add(baudRateComboBox, gbc);
        
        // Info Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel infoLabel = new JLabel("<html><center>I-identify ang COM port ng RFID reader<br>" +
                                     "sa Device Manager → Ports (COM & LPT)</center></html>");
        infoLabel.setForeground(Color.GRAY);
        formPanel.add(infoLabel, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton testButton = new JButton("Test Connection");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        testButton.addActionListener(e -> testConnection());
        saveButton.addActionListener(e -> saveConfiguration());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(testButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private String[] getAvailablePorts() {
        // Common COM ports para sa Windows
        return new String[]{"COM1", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10"};
    }
    
    private void testConnection() {
        String port = (String) portComboBox.getSelectedItem();
        String baudRateStr = (String) baudRateComboBox.getSelectedItem();
        
        if (port == null || port.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a COM port!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "Testing connection to:\n" +
            "Port: " + port + "\n" +
            "Baud Rate: " + baudRateStr + "\n\n" +
            "Note: Kailangan ng jSerialComm library para sa actual connection.\n" +
            "I-check ang console para sa connection status.",
            "Test Connection", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void saveConfiguration() {
        String port = (String) portComboBox.getSelectedItem();
        String baudRateStr = (String) baudRateComboBox.getSelectedItem();
        
        if (port == null || port.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a COM port!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            selectedPort = port.trim();
            selectedBaudRate = Integer.parseInt(baudRateStr);
            configured = true;
            
            JOptionPane.showMessageDialog(this, 
                "Configuration saved!\n\n" +
                "Port: " + selectedPort + "\n" +
                "Baud Rate: " + selectedBaudRate + "\n\n" +
                "I-restart ang application para ma-apply ang changes.",
                "Configuration Saved", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid baud rate!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfigured() {
        return configured;
    }
    
    public String getSelectedPort() {
        return selectedPort;
    }
    
    public int getSelectedBaudRate() {
        return selectedBaudRate;
    }
}

