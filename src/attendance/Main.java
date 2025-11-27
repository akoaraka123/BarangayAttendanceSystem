package attendance;

public class Main {

    public static void main(String[] args) {
        // Set Look and Feel to system default for better appearance
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set Look and Feel: " + e.getMessage());
        }

        // Start with Login Frame
        new LoginFrame().setVisible(true);
    }
}
