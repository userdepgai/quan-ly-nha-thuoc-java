package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            // Chỉ tạo kết nối mới nếu chưa có hoặc kết nối cũ đã bị đóng
            if (connection == null || connection.isClosed()) {

                Properties props = new Properties();
                // Đọc file db.properties từ thư mục src
                InputStream input = DBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties");

                if (input == null) {
                    System.out.println("Lỗi: Không tìm thấy file db.properties!");
                    return null;
                }

                // Load các thông số vào đối tượng Properties
                props.load(input);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                // Thực hiện kết nối
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