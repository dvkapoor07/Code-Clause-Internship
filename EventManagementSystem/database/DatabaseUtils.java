package EventManagementSystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseUtils {
        private static final String URL = "jdbc:mysql://localhost:3306/em";
        private static final String USER = "root";
        private static final String PASSWORD = "Dvkapoor@07";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
