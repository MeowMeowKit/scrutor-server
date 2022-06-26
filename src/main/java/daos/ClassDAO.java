package daos;

import utils.DBUtils;
import dtos.Class;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ClassDAO {

    private static Connection conn;
    private static PreparedStatement preStm;
    private static ResultSet rs;

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
        ArrayList<Class> list = new ArrayList<>();
        try {
            conn = DBUtils.makeConnection();
            String sql = "SELECT classID, name  "
                    + "FROM Class "
                    + "WHERE teacherId = ? ";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, teacherId);
            rs = preStm.executeQuery();
            while (rs.next()) {
                String classID = rs.getString("classID");
                String name = rs.getString("name");
                list.add(new Class(classID, teacherId, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;
    }

    // Get all the classes that student A attends
    public static ArrayList<Class> getClassesByStudentId(String studentId) {
        ArrayList<Class> list = new ArrayList<>();
        try {
            conn = DBUtils.makeConnection();
            String sql = "SELECT cl.classId, cl.teacheId, cl.name  "
                    + "FROM Class cl left join User_Class ucl on cl.classId = ucl.classId "
                    + "WHERE ucl.userId = ? ";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, studentId);
            rs = preStm.executeQuery();
            while (rs.next()) {
                String classID = rs.getString("cl.classID");
                String name = rs.getString("cl.name");
                list.add(new Class(classID, studentId, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;
    }

    // Create a new class that taught by teacher A
    public static Class createNewClass(Class c, String teacherId) {
        Class classResult = null;
        try {
            conn = DBUtils.makeConnection();
            String sql = "INSERT INTO Class "
                    + "Values( ?, ?, ?)";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, c.getClassId());
            preStm.setString(2, teacherId);
            preStm.setString(3, c.getName());

            rs = preStm.executeQuery();
            while (rs.next()) {
                String classID = rs.getString("classID");
                String name = rs.getString("name");
                classResult = new Class(classID, teacherId, name);
            }
            return classResult;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return classResult;
    }

    // A student attends a new class
    public static boolean attendNewClass(Class c, String studentId) {
        boolean result = false;
        try {
            conn = DBUtils.makeConnection();
            String sql = "INSERT INTO User_Class "
                    + "Values( ?, ?)";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, studentId);
            preStm.setString(2, c.getClassId());

            rs = preStm.executeQuery();
            int row = preStm.executeUpdate();
            if (row > 0) {
                result = true;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    // Update class (edit className; edit student list add, remove student from class)
    public static boolean updateClass(Class c) {
        boolean result = false;
        try {
            String sql = "UPDATE Class "
                    + "SET name = ? "
                    + "WHERE classId = ?";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, c.getName());
            preStm.setString(2, c.getClassId());

            rs = preStm.executeQuery();
            int row = preStm.executeUpdate();
            if (row > 0) {
                result = true;
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
    public static boolean deleteClass(String classId) {
        boolean result = false;
        try {
            conn = DBUtils.makeConnection();
            String sql = "DELETE FROM Class "
                    + "WHERE classId = ? ";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, classId);
            rs = preStm.executeQuery();
            int row = preStm.executeUpdate();
            if (row > 0) {
                result = true;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    // A student leave a class
    public static boolean leaveClass(String userId, String classId) {
        boolean result = false;
        try {
            conn = DBUtils.makeConnection();
            String sql = "DELETE FROM User_Class "
                    + "WHERE userId = ? AND classId = ? ";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, userId);
            preStm.setString(2, classId);

            rs = preStm.executeQuery();
            int row = preStm.executeUpdate();
            if (row > 0) {
                result = true;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}