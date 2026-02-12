package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL =
            "jdbc:sqlserver://DESKTOP-043ECCP:1433;"
            + "databaseName=btJavaTuan9;"
            + "encrypt=true;"
            + "trustServerCertificate=true";
    private static final String USER = "javauser";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
