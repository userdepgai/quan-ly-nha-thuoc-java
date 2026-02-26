package DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                Properties props = new Properties();
                InputStream input = DBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties");

                if (input == null) {
                    // Ưu tiên ném Exception để dừng chương trình nếu không có cấu hình DB
                    throw new RuntimeException("Lỗi: Không tìm thấy file db.properties!");
                }
                props.load(input);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Kết nối Database thành công!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi kết nối Database:");
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        getConnection();
    }
}