package attendance;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class UserManagementFrame extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRecover, btnRefresh;
    private JCheckBox chkShowDeleted;
    
    public UserManagementFrame() {
        setTitle("User Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("👥 User Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        chkShowDeleted = new JCheckBox("Show Deleted Users");
        chkShowDeleted.setForeground(Color.WHITE);
        chkShowDeleted.setOpaque(false);
        chkShowDeleted.addActionListener(e -> refreshTable());
        headerPanel.add(chkShowDeleted, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"ID", "Email", "Role", "Created At", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(ThemeManager.PRIMARY_COLOR);
        userTable.getTableHeader().setForeground(Color.WHITE);
        
        // Add selection listener to update button states
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // Set column widths
        TableColumnModel columnModel = userTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);   // ID
        columnModel.getColumn(1).setPreferredWidth(300); // Email
        columnModel.getColumn(2).setPreferredWidth(150);  // Role
        columnModel.getColumn(3).setPreferredWidth(200);   // Created At
        columnModel.getColumn(4).setPreferredWidth(150);  // Status
        
        // Custom renderer for status column
        userTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("Deleted".equals(status)) {
                    c.setForeground(Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(Color.GREEN);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Users"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        btnAdd = new JButton("➕ Add User");
        btnEdit = new JButton("✏️ Edit User");
        btnDelete = new JButton("🗑️ Delete User");
        btnRecover = new JButton("♻️ Recover User");
        btnRefresh = new JButton("🔄 Refresh");
        
        ThemeManager.styleButton(btnAdd);
        ThemeManager.styleButton(btnEdit);
        ThemeManager.styleButton(btnDelete);
        ThemeManager.styleButton(btnRecover);
        ThemeManager.styleButton(btnRefresh);
        
        btnAdd.setPreferredSize(new Dimension(150, 40));
        btnEdit.setPreferredSize(new Dimension(150, 40));
        btnDelete.setPreferredSize(new Dimension(150, 40));
        btnRecover.setPreferredSize(new Dimension(150, 40));
        btnRefresh.setPreferredSize(new Dimension(150, 40));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRecover);
        buttonPanel.add(btnRefresh);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Action listeners
        btnAdd.addActionListener(e -> addUser());
        btnEdit.addActionListener(e -> editUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnRecover.addActionListener(e -> recoverUser());
        btnRefresh.addActionListener(e -> refreshTable());
        
        add(mainPanel);
        
        // Load initial data
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        boolean includeDeleted = chkShowDeleted.isSelected();
        List<DatabaseOperations.User> users = DatabaseOperations.getAllUsers(includeDeleted);
        
        for (DatabaseOperations.User user : users) {
            String status = user.isDeleted() ? "Deleted" : "Active";
            String createdAt = user.getCreatedAt() != null 
                ? user.getCreatedAt().toString().substring(0, 19) 
                : "N/A";
            
            tableModel.addRow(new Object[]{
                user.getId(),
                user.getEmail(),
                user.getRole(),
                createdAt,
                status
            });
        }
        
        // Update button states
        updateButtonStates();
    }
    
    private void updateButtonStates() {
        int selectedRow = userTable.getSelectedRow();
        boolean hasSelection = selectedRow >= 0;
        
        if (hasSelection) {
            String email = (String) tableModel.getValueAt(selectedRow, 1);
            String status = (String) tableModel.getValueAt(selectedRow, 4);
            boolean isDeleted = "Deleted".equals(status);
            boolean isProtected = "admin@barangay.com".equalsIgnoreCase(email);
            
            btnEdit.setEnabled(hasSelection && !isDeleted && !isProtected);
            btnDelete.setEnabled(hasSelection && !isDeleted && !isProtected);
            btnRecover.setEnabled(hasSelection && isDeleted);
        } else {
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnRecover.setEnabled(false);
        }
    }
    
    private void addUser() {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtEmail = new JTextField(20);
        // Email filter: allow @ and . but filter other special characters
        if (txtEmail.getDocument() instanceof javax.swing.text.AbstractDocument) {
            ((javax.swing.text.AbstractDocument) txtEmail.getDocument()).setDocumentFilter(
                new javax.swing.text.DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if (string == null) return;
                        String filtered = string.replaceAll("[^A-Za-z0-9@.\\-_]", "");
                        if (!filtered.isEmpty()) {
                            super.insertString(fb, offset, filtered, attr);
                        }
                    }
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        if (text == null) return;
                        String filtered = text.replaceAll("[^A-Za-z0-9@.\\-_]", "");
                        if (!filtered.isEmpty() || length > 0) {
                            super.replace(fb, offset, length, filtered, attrs);
                        }
                    }
                }
            );
        }
        panel.add(txtEmail, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JPasswordField txtPassword = new JPasswordField(20);
        // Password filter: alphanumeric only (no special characters as requested)
        if (txtPassword.getDocument() instanceof javax.swing.text.AbstractDocument) {
            ((javax.swing.text.AbstractDocument) txtPassword.getDocument()).setDocumentFilter(
                InputFilter.createAlphanumericFilter()
            );
        }
        panel.add(txtPassword, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"admin", "employee"});
        panel.add(cmbRole, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        ThemeManager.styleSuccessButton(btnSave);
        ThemeManager.styleButton(btnCancel);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, gbc);
        
        btnSave.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String role = (String) cmbRole.getSelectedItem();
            
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(dialog, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (DatabaseOperations.createUserForManagement(email, password, role)) {
                JOptionPane.showMessageDialog(dialog, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to create user. Email may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String email = (String) tableModel.getValueAt(selectedRow, 1);
        String role = (String) tableModel.getValueAt(selectedRow, 2);
        
        // Protect admin account
        if ("admin@barangay.com".equalsIgnoreCase(email)) {
            JOptionPane.showMessageDialog(this, "Cannot edit protected admin account.", "Protected Account", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Edit User", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtEmail = new JTextField(email, 20);
        // Email filter: allow @ and . but filter other special characters
        if (txtEmail.getDocument() instanceof javax.swing.text.AbstractDocument) {
            ((javax.swing.text.AbstractDocument) txtEmail.getDocument()).setDocumentFilter(
                new javax.swing.text.DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if (string == null) return;
                        String filtered = string.replaceAll("[^A-Za-z0-9@.\\-_]", "");
                        if (!filtered.isEmpty()) {
                            super.insertString(fb, offset, filtered, attr);
                        }
                    }
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        if (text == null) return;
                        String filtered = text.replaceAll("[^A-Za-z0-9@.\\-_]", "");
                        if (!filtered.isEmpty() || length > 0) {
                            super.replace(fb, offset, length, filtered, attrs);
                        }
                    }
                }
            );
        }
        panel.add(txtEmail, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"admin", "employee"});
        cmbRole.setSelectedItem(role);
        panel.add(cmbRole, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        ThemeManager.styleSuccessButton(btnSave);
        ThemeManager.styleButton(btnCancel);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, gbc);
        
        btnSave.addActionListener(e -> {
            String newEmail = txtEmail.getText().trim();
            String newRole = (String) cmbRole.getSelectedItem();
            
            if (newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Email cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidEmail(newEmail)) {
                JOptionPane.showMessageDialog(dialog, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (DatabaseOperations.updateUser(userId, newEmail, newRole)) {
                JOptionPane.showMessageDialog(dialog, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update user. Email may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String email = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Protect admin account
        if ("admin@barangay.com".equalsIgnoreCase(email)) {
            JOptionPane.showMessageDialog(this, "Cannot delete protected admin account.", "Protected Account", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete user: " + email + "?\n\nNote: User will be soft-deleted and can be recovered later.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseOperations.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully! (Soft delete - can be recovered)", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void recoverUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a deleted user to recover.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String email = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to recover user: " + email + "?",
            "Confirm Recover", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseOperations.recoverUser(userId)) {
                JOptionPane.showMessageDialog(this, "User recovered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to recover user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

