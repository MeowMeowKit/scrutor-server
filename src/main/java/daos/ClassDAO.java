package daos;

import java.util.ArrayList;
import dtos.Class;
import dtos.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import utils.DBUtils;

public class ClassDAO {

    private static Connection conn = null;
    private static PreparedStatement preStm = null;
    private static ResultSet rs = null;

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

    // Get all the classes that are taught by teacher A
    public static ArrayList<Class> getClassesByTeacherId(String teacherId) {
        conn = null;
        preStm = null;
        rs = null;
        ArrayList<Class> list = new ArrayList<>();
        ArrayList<User> listUser = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            String sql = "SELECT u.userId, u.fullName, u.email, u.password, u.role, cl.classId, cl.name, cl.teacherId  "
                    + "FROM (User u right join User_Class ucl on u.userId = ucl.userId) left join Class cl on ucl.classId = cl.classId "
                    + "WHERE cl.teacherId = ? AND u.role = 'student' "
                    + "ORDER BY cl.classId ";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, teacherId);
            rs = preStm.executeQuery();

            if (rs.next()) {
                //Tạo biến chứa giá trị classId của row hiện tại
                String classId = rs.getString("cl.classId");
                while (rs.next()) {
                    //Create a Student list
                    String userId = rs.getString("u.userId");
                    String fullName = rs.getString("u.fullName");
                    String email = rs.getString("u.email");
                    String password = rs.getString(" u.password");
                    String role = rs.getString("u.role");

                    //Phân loại lớp
                    String newClassId = rs.getString("cl.classId");
                    //So sánh row đang xét có trùng classId với row trên không
                    if (newClassId.equals(classId)) {
                        listUser.add(new User(userId, fullName, email, password, role));
                    } else {
                        classId = newClassId;
                        list.add(new Class(rs.getString("cl.classID"), teacherId, rs.getString("cl.name"), listUser));

                        //Add vô Student đầu tiên vô class mới
                        listUser = null;
                        listUser.add(new User(userId, fullName, email, password, role));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnection();
        }
        return list;
    }

    // Get all the classes that student A attends
    public static ArrayList<Class> getClassesByStudentId(String studentId) {
        conn = null;
        preStm = null;
        rs = null;
        ArrayList<Class> list = new ArrayList<>();
        ArrayList<User> listUser = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();
            String sql = "SELECT cl.classId, cl.teacherId, cl.name , u.userId, u.fullName, u.email, u.password, u.role"
                    + "FROM ( Class cl left join User_Class ucl on cl.classId = ucl.classId ) inner join User u on ucl.userId = u.userId  "
                    + "WHERE ucl.userId = ? AND u.role = 'student'  "
                    + "ORDER BY cl.classId";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, studentId);
            rs = preStm.executeQuery();

            if (rs.next()) {
                //Tạo biến chứa giá trị classId của row hiện tại
                String classId = rs.getString("cl.classId");
                while (rs.next()) {
                    //Create a Student list
                    String userId = rs.getString("u.userId");
                    String fullName = rs.getString("u.fullName");
                    String email = rs.getString("u.email");
                    String password = rs.getString(" u.password");
                    String role = rs.getString("u.role");

                    //Phân loại lớp
                    String newClassId = rs.getString("cl.classId");
                    //So sánh row đang xét có trùng classId với row trên không
                    if (newClassId.equals(classId)) {
                        listUser.add(new User(userId, fullName, email, password, role));
                    } else {
                        classId = newClassId;
                        list.add(new Class(rs.getString("cl.classID"), rs.getString("cl.teacheId"), rs.getString("cl.name"), listUser));

                        //Add vô Student đầu tiên vô class mới
                        listUser = null;
                        listUser.add(new User(userId, fullName, email, password, role));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnection();
        }
        return list;
    }

    // Create a new class that taught by teacher A
    public static Class createNewClass(Class c, String teacherId) {
        conn = null;
        preStm = null;
        rs = null;

        try {
            //Create a Class
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);
                c.setTeacherId(teacherId);
                c.setClassId(UUID.randomUUID().toString());

                //Insert Class
                String sql = "INSERT INTO Class(classId, teacherId, name) "
                        + "VALUES( ?, ?, ?)";

                preStm = conn.prepareStatement(sql);
                preStm.setString(1, c.getClassId());
                preStm.setString(2, c.getTeacherId());
                preStm.setString(3, c.getName());
                rs = preStm.executeQuery();

                //Insert The Student into class
                preStm = null;
                for (User user : c.getStudents()) {
                    try {
                        sql = "INSERT INTO USER_CLASS(userId, classId) "
                                + "VALUES(? , ?)";
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, user.getUserId());
                        preStm.setString(2, c.getClassId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return c;
    }

    // A student attends a new class
    public static int attendNewClass(Class c, String studentId) {
        conn = null;
        preStm = null;
        rs = null;
        int result = 0;// Nếu Hàm trả về 0 thì sẽ không có dòng nào ảnh hưởng
        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                String sql = "INSERT INTO User_Class(userId, classId)"
                        + "Values( ?, ?)";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, studentId);
                preStm.setString(2, c.getClassId());

                rs = preStm.executeQuery();
                result = preStm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    // Update class (edit className; edit student list add, remove student from class)
    public static int updateClass(Class c) {
        conn = null;
        preStm = null;
        rs = null;
        String sql = null;
        int result = 0;// Nếu Hàm trả về 0 thì sẽ không có dòng nào ảnh hưởng
        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);
                //Update the Class
                sql = "UPDATE Class "
                        + "SET name = ? "
                        + "WHERE classId = ? ";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, c.getName());
                preStm.setString(2, c.getClassId());
                result = preStm.executeUpdate();

                //Delete old menbers in a Class
                preStm = null;
                rs = null;
                sql = "DELETE FROM User_Class "
                        + "Where classId = ? ";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, c.getClassId());
                preStm.executeUpdate();

                //INSERT NEW data in User_Class
                preStm = null;
                rs = null;
                for (User user : c.getStudents()) {
                    try {
                        sql = "INSERT INTO User_Class(userId, classId) "
                                + "Values( ?, ?) ";
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, user.getUserId());
                        preStm.setString(2, c.getClassId());
                        preStm.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    // A teacher delete a class
    public static int deleteClass(String classId) {
        conn = null;
        preStm = null;
        rs = null;
        int result = 0; // Nếu Hàm trả về 0 thì sẽ không có dòng nào ảnh hưởng
        String sql = null;
        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);
                //Delete All Student in Class
                sql = "DELETE FROM User_Class "
                        + "WHERE classId = ? ";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, classId);
                preStm.executeQuery();

                //Delete Class
                preStm = null;
                sql = "DELETE FROM Class "
                        + "WHERE classId = ? ";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, classId);
                result = preStm.executeUpdate();
                return result;
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
                return result;
            }
            e.printStackTrace();
            return result;
        } finally {
            closeConnection();
        }
        return result;
    }

    // A student leave a class
    public static int leaveClass(String userId, String classId) {
        conn = null;
        preStm = null;
        rs = null;
        int result = 0; // Nếu Hàm trả về 0 thì sẽ không có dòng nào ảnh hưởng
        try {
            conn = DBUtils.makeConnection();
            String sql = "DELETE FROM User_Class "
                    + "WHERE userId = ? AND classId = ?";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, userId);
            preStm.setString(2, classId);

            rs = preStm.executeQuery();
            result = preStm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        } finally {
            closeConnection();
        }
        return result;
    }
}
