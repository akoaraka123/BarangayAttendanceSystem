package attendance;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class RegisterFrame extends JFrame {
    private JTextField txtId, txtName, txtPosition;
    private JButton btnSave;

    public RegisterFrame() {
        setTitle("Register Employee");
        setSize(350, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblId = new JLabel("Employee ID:");
        JLabel lblName = new JLabel("Full Name:");
        JLabel lblPosition = new JLabel("Position:");

        txtId = new JTextField();
        txtName = new JTextField();
        txtPosition = new JTextField();

        btnSave = new JButton("Save");

        add(lblId);
        add(txtId);
        add(lblName);
        add(txtName);
        add(lblPosition);
        add(txtPosition);
        add(new JLabel()); // Empty cell
        add(btnSave);

        // Save button action
        btnSave.addActionListener(e -> saveEmployee());
    }

    private void saveEmployee() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String position = txtPosition.getText().trim();

        if (id.isEmpty() || name.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        try (FileWriter fw = new FileWriter("employees.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(id + "," + name + "," + position);

            JOptionPane.showMessageDialog(this, "Employee registered successfully!");

            txtId.setText("");
            txtName.setText("");
            txtPosition.setText("");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving employee: " + ex.getMessage());
        }
    }
}
