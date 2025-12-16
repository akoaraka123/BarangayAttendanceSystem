package attendance;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DepartmentManagementFrame extends JFrame {
    private JTable departmentTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnViewEmployees, btnRefresh;
    
    public DepartmentManagementFrame() {
        setTitle("Department Management");
        setSize(1000, 700);
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
        
        JLabel titleLabel = new JLabel("🏢 Department Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"ID", "Department Name", "Description", "Employees", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        departmentTable = new JTable(tableModel);
        departmentTable.setRowHeight(30);
        departmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        departmentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        departmentTable.getTableHeader().setBackground(ThemeManager.PRIMARY_COLOR);
        departmentTable.getTableHeader().setForeground(Color.WHITE);
        
        // Set column widths
        TableColumnModel columnModel = departmentTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);   // ID
        columnModel.getColumn(1).setPreferredWidth(250);  // Department Name
        columnModel.getColumn(2).setPreferredWidth(300);   // Description
        columnModel.getColumn(3).setPreferredWidth(100);   // Employees
        columnModel.getColumn(4).setPreferredWidth(200);   // Created At
        
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Departments"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        btnAdd = new JButton("➕ Add Department");
        btnEdit = new JButton("✏️ Edit Department");
        btnDelete = new JButton("🗑️ Delete Department");
        btnViewEmployees = new JButton("👥 View Employees");
        btnRefresh = new JButton("🔄 Refresh");
        
        ThemeManager.styleButton(btnAdd);
        ThemeManager.styleButton(btnEdit);
        ThemeManager.styleButton(btnDelete);
        ThemeManager.styleButton(btnViewEmployees);
        ThemeManager.styleButton(btnRefresh);
        
        btnAdd.setPreferredSize(new Dimension(180, 40));
        btnEdit.setPreferredSize(new Dimension(180, 40));
        btnDelete.setPreferredSize(new Dimension(180, 40));
        btnViewEmployees.setPreferredSize(new Dimension(180, 40));
        btnRefresh.setPreferredSize(new Dimension(180, 40));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewEmployees);
        buttonPanel.add(btnRefresh);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Action listeners
        btnAdd.addActionListener(e -> addDepartment());
        btnEdit.addActionListener(e -> editDepartment());
        btnDelete.addActionListener(e -> deleteDepartment());
        btnViewEmployees.addActionListener(e -> viewEmployeesInDepartment());
        btnRefresh.addActionListener(e -> refreshTable());
        
        // Add selection listener
        departmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // Add double-click listener to view employees
        departmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Double-click detected
                    int selectedRow = departmentTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        viewEmployeesInDepartment();
                    }
                }
            }
        });
        
        add(mainPanel);
        
        // Load initial data
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<DatabaseOperations.Department> departments = DatabaseOperations.getAllDepartments();
        
        for (DatabaseOperations.Department dept : departments) {
            String createdAt = dept.getCreatedAt() != null 
                ? dept.getCreatedAt().toString().substring(0, 19) 
                : "N/A";
            
            tableModel.addRow(new Object[]{
                dept.getId(),
                dept.getName(),
                dept.getDescription() != null ? dept.getDescription() : "",
                dept.getEmployeeCount(),
                createdAt
            });
        }
        
        updateButtonStates();
    }
    
    private void updateButtonStates() {
        int selectedRow = departmentTable.getSelectedRow();
        boolean hasSelection = selectedRow >= 0;
        
        btnEdit.setEnabled(hasSelection);
        btnDelete.setEnabled(hasSelection);
        btnViewEmployees.setEnabled(hasSelection);
    }
    
    private void addDepartment() {
        JDialog dialog = new JDialog(this, "Add New Department", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Department Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtName = new JTextField(20);
        InputFilter.applyFilter(txtName, InputFilter.createDepartmentNameFilter());
        panel.add(txtName, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextArea txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        InputFilter.applyFilter(txtDescription, InputFilter.createDescriptionFilter());
        JScrollPane scrollPane = new JScrollPane(txtDescription);
        panel.add(scrollPane, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        ThemeManager.styleButton(btnSave);
        ThemeManager.styleButton(btnCancel);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, gbc);
        
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            String description = txtDescription.getText().trim();
            
            // Validate department name
            String nameError = InputValidator.getDepartmentNameErrorMessage(name);
            if (!nameError.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, nameError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate description
            String descError = InputValidator.getDescriptionErrorMessage(description);
            if (!descError.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, descError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (DatabaseOperations.createDepartment(name, description)) {
                JOptionPane.showMessageDialog(dialog, "Department created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to create department. Name may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a department to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int deptId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String description = (String) tableModel.getValueAt(selectedRow, 2);
        
        JDialog dialog = new JDialog(this, "Edit Department", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Department Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtName = new JTextField(name, 20);
        InputFilter.applyFilter(txtName, InputFilter.createDepartmentNameFilter());
        panel.add(txtName, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextArea txtDescription = new JTextArea(description != null ? description : "", 3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        InputFilter.applyFilter(txtDescription, InputFilter.createDescriptionFilter());
        JScrollPane scrollPane = new JScrollPane(txtDescription);
        panel.add(scrollPane, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        ThemeManager.styleButton(btnSave);
        ThemeManager.styleButton(btnCancel);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, gbc);
        
        btnSave.addActionListener(e -> {
            String newName = txtName.getText().trim();
            String newDescription = txtDescription.getText().trim();
            
            // Validate department name
            String nameError = InputValidator.getDepartmentNameErrorMessage(newName);
            if (!nameError.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, nameError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate description
            String descError = InputValidator.getDescriptionErrorMessage(newDescription);
            if (!descError.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, descError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (DatabaseOperations.updateDepartment(deptId, newName, newDescription)) {
                JOptionPane.showMessageDialog(dialog, "Department updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update department. Name may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int deptId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        int employeeCount = (Integer) tableModel.getValueAt(selectedRow, 3);
        
        if (employeeCount > 0) {
            JOptionPane.showMessageDialog(this, 
                "Cannot delete department with assigned employees.\n" +
                "Please reassign or remove employees first.",
                "Cannot Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete department: " + name + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseOperations.deleteDepartment(deptId)) {
                JOptionPane.showMessageDialog(this, "Department deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete department.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewEmployeesInDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a department to view employees.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int deptId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String deptName = (String) tableModel.getValueAt(selectedRow, 1);
        
        List<Employee> employees = DatabaseOperations.getEmployeesByDepartment(deptId);
        
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees assigned to this department.", "No Employees", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create dialog to show employees
        JDialog dialog = new JDialog(this, "Employees in " + deptName, true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        
        String[] columnNames = {"Employee ID", "Name", "Contact", "Position"};
        Object[][] data = new Object[employees.size()][4];
        
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            data[i][0] = emp.getEmployeeId();
            data[i][1] = emp.getFullName();
            data[i][2] = emp.getContactNumber();
            data[i][3] = emp.getPosition();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setEnabled(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(ThemeManager.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }
}

