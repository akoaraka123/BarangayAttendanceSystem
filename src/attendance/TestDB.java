import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC",
                "root", ""
            );
            System.out.println("✅ Connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
