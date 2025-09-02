package attendance;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";     // default ng XAMPP
    private static final String PASSWORD = "";     // lagyan kung may password ang root mo

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // driver class
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to Database!");
            return conn;
        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
}
