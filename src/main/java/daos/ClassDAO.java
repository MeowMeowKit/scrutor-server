package daos;

import dtos.Class;
import dtos.Question;
import dtos.Quiz;
import dtos.User;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class ClassDAO {
    public static ArrayList<Class> getClassesByTeacherId(String teacherId) {
        Connection conn = null;
        ArrayList<Class> list = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT c.classId, c.name\n" +
                        "FROM Class c\n" +
                        "WHERE c.teacherId = ? AND q._status = 0\n" +
                        "ORDER BY q._createdAt;";
                PreparedStatement preStm = conn.prepareStatement(sql);
                preStm.setString(1, teacherId);
                ResultSet rs = preStm.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        String classId = rs.getNString("c.classId");
                        String name = rs.getNString("c.name");

                        Class c = new Class(classId, teacherId, name);

                        // Fetch students
                        String sql1 = "SELECT s.userId, s.fullName, s.email, s.role\n" +
                                "FROM `User` s JOIN Student_Class sc JOIN Class c\n" +
                                "ON s.userId = sc.studentId AND sc.classId = c.classId\n" +
                                "WHERE c.teacherId = ? AND sc._status = 0\n" +
                                "ORDER BY sc._createdAt;";
                        PreparedStatement preStm1 = conn.prepareStatement(sql1);
                        preStm.setString(1, teacherId);
                        ResultSet rs1 = preStm1.executeQuery();

                        if (rs1 != null) {
                            while (rs1.next()) {
                                String studentId = rs1.getNString("s.userId");
                                String fullName = rs1.getNString("s.fullName");
                                String email = rs1.getNString("s.email");
                                String role = rs1.getNString("s.role");

                                User student = new User(studentId, fullName, email, role);

                                c.addStudent(student);
                            }
                        }

                        list.add(c);
                    }
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Class createQuiz(Class c, String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                c.setClassId(UUID.randomUUID().toString());
                c.setTeacherId(teacherId);

                // Insert quiz
                String sql = "INSERT INTO Class (classId, teacherId, name) VALUES(?, ?, ?);";
                preStm = conn.prepareStatement(sql);

                preStm.setString(1, c.getClassId());
                preStm.setString(2, c.getTeacherId());
                preStm.setString(3, c.getName());
                preStm.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return c;
    }
}
