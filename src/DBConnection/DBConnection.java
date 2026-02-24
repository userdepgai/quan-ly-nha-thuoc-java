package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL =
            "jdbc:sqlserver://DESKTOP-043ECCP:1433;"
            + "databaseName=QuanLyNhaThuoc;"
            + "encrypt=true;"
            + "trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "Uyendepgai";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
