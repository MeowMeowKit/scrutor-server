package daos;

import java.util.ArrayList;
import dtos.Class;
import dtos.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    + "ORDER BY cl.classId";
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
                    if(newClassId != classId){
                        classId = newClassId;
                        list.add(new Class(rs.getString("cl.classID"), teacherId, rs.getString("cl.name"), listUser));
                        
                        //Add vô Student đầu tiên vô class mới
                        listUser = null;
                        listUser.add(new User(userId, fullName, email, password, role));
                    }else{
                        listUser.add(new User(userId, fullName, email, password, role));
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
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
            String sql = "SELECT cl.classId, cl.teacheId, cl.name , u.userId, u.fullName, u.email, u.password, u.role"
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
                    if(newClassId != classId){
                        classId = newClassId;
                        list.add(new Class(rs.getString("cl.classID"), rs.getString("cl.teacheId"), rs.getString("cl.name"), listUser));
                        
                        //Add vô Student đầu tiên vô class mới
                        listUser = null;
                        listUser.add(new User(userId, fullName, email, password, role));
                    }else{
                        listUser.add(new User(userId, fullName, email, password, role));
                    }  
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
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
        Class classResult = null;
        ArrayList<User> listUser = new ArrayList<>();
        try {
            //Create a Class
            conn = DBUtils.makeConnection();
            String sql = "INSERT INTO Class "
                    + "VAVLUES( ?, ?, ?)";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, c.getClassId());
            preStm.setString(2, teacherId);
            preStm.setString(3, c.getName());
            rs = preStm.executeQuery();

            //Insert The Student into class
            preStm = null;
            rs = null;
            for (User user : c.getStudents()) {
                sql = "INSERT INTO USER_CLASS "
                        + "VALUES(? , ?)";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, user.getUserId());
                preStm.setString(2, c.getClassId());
                listUser.add(new User(user.getUserId(), user.getFullName(), user.getEmail(), user.getPassword(), user.getRole()));
            }
            classResult = new Class(c.getClassId(), teacherId, c.getName(), listUser);
            return classResult;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return classResult;
    }

    // A student attends a new class
    public static int attendNewClass(Class c, String studentId) {
        conn = null;
        preStm = null;
        rs = null;
        int result = 0;// Nếu Hàm trả về 0 thì sẽ không có dòng nào ảnh hưởng
        try {
            conn = DBUtils.makeConnection();
            String sql = "INSERT INTO User_Class "
                    + "Values( ?, ?)";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, studentId);
            preStm.setString(2, c.getClassId());

            rs = preStm.executeQuery();
            result = preStm.executeUpdate();
            return result;
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

            //Update the NAME of Class
            sql = "UPDATE Class "
                    + "SET name = ? "
                    + "WHERE classId = ?";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, c.getName());
            preStm.setString(2, c.getClassId());
            rs = preStm.executeQuery();

            //Delete old menbers in a Class
            preStm = null;
            rs = null;
            sql = "DELETE FROM User_Class "
                    + "Where classId = ? ";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, c.getClassId());
            rs = preStm.executeQuery();

            //INSERT NEW data in CLASS
            preStm = null;
            rs = null;
            for (User user : c.getStudents()) {
                sql = "INSERT INTO User_Class "
                        + "Values( ?, ?) ";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, user.getUserId());
                preStm.setString(2, c.getClassId());
                rs = preStm.executeQuery();
                result = preStm.executeUpdate();
            }
            return result;
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
            //Delete All Student in Clas
            sql = "DELETE FROM User_Class "
                    + "WHERE classId = ? ";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, classId);
            rs = preStm.executeQuery();

            //Delete Class
            preStm = null;
            sql = "DELETE FROM Class "
                    + "WHERE classId = ? ";
            preStm.setString(1, classId);
            rs = preStm.executeQuery();
            result = preStm.executeUpdate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
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
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}
