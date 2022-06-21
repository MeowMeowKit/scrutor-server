package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    public static Connection makeConnection() {
        Connection connection = null;
        String id = "scrutor.cwcluovkimqe.us-west-1.rds.amazonaws.com";
        String instanceName = "scrutor";
        String port = "3306";
        String db = "Scrutor";
        String username = "admin";
        String password = "123456789";
        
//        String urlDatabase = String.format("jdbc:sqlserver://%s\\%s:%s;databaseName=%s;user=%s;password=%s", id, instanceName, port, db, username, password);
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://scrutor.cwcluovkimqe.us-west-1.rds.amazonaws.com:3306/Scrutor", "admin", "123456789");
            System.out.println("Connection to database successfully");
        } catch (Exception error) {
            error.printStackTrace();
            System.out.println("DB Error");
        }
        return connection;
    }
}
