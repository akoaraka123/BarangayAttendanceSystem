package attendance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Load MySQL driver with better error handling
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver Loaded!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
            System.err.println("Make sure MySQL Connector/J is included in the classpath!");
            System.err.println("For JAR deployment, include mysql-connector-j-8.x.x.jar");
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            // Add connection properties for better reliability
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("serverTimezone", "UTC");
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("useUnicode", "true");
            props.setProperty("characterEncoding", "UTF-8");
            props.setProperty("characterSetResults", "UTF-8");
            props.setProperty("connectTimeout", "10000");
            props.setProperty("socketTimeout", "30000");
            
            Connection conn = DriverManager.getConnection(URL, props);
            System.out.println("Connected to Database!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database Connection Failed: " + e.getMessage());
            System.err.println("Make sure MySQL server is running on localhost:3306");
            System.err.println("Make sure database 'attendance_db' exists");
            System.err.println("Check if MySQL user 'root' has proper permissions");
            throw e;
        }
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed!");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    // Test connection with detailed error reporting
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn != null && !conn.isClosed();
            if (isValid) {
                System.out.println("Database connection test PASSED!");
            } else {
                System.out.println("Database connection test FAILED!");
            }
            return isValid;
        } catch (SQLException e) {
            System.err.println("Database connection test FAILED: " + e.getMessage());
            return false;
        }
    }
    
    // Get connection status details
    public static String getConnectionStatus() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                String status = "Connected to MySQL database: " + conn.getMetaData().getDatabaseProductName() + 
                               " " + conn.getMetaData().getDatabaseProductVersion();
                conn.close();
                return status;
            }
        } catch (SQLException e) {
            return "Connection failed: " + e.getMessage();
        }
        return "Unable to establish connection";
    }
}
