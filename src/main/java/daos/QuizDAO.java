package daos;

import dtos.Question;
import dtos.Quiz;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class QuizDAO {

    private static Connection conn;
    private static PreparedStatement preStm;
    private static ResultSet rs;

    public QuizDAO() {
    }

    private static void closeConnection() {
        try {

            if (rs != null) {
                rs.close();
            }
            if (preStm != null) {
                preStm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
