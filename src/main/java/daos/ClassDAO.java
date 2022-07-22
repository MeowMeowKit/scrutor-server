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
                        "WHERE c.teacherId = ? AND c._status = 0\n" +
                        "ORDER BY c._createdAt;";
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
                                "FROM `User` s JOIN Student_Class sc\n" +
                                "ON s.userId = sc.studentId\n" +
                                "WHERE sc.classId = ? AND sc._status = 0\n" +
                                "ORDER BY sc._createdAt;";
                        PreparedStatement preStm1 = conn.prepareStatement(sql1);
                        preStm1.setString(1, classId);
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

    public static Class getClassByClassId(String classId) {
        Connection conn = null;
        Class c = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT c.teacherId, c.name, u.fullName\n" +
                        "FROM Class c JOIN User u\n" +
                        "ON c.teacherId = u.userId\n" +
                        "WHERE c.classId = ? AND c._status = 0\n" +
                        "ORDER BY c._createdAt;";
                PreparedStatement preStm = conn.prepareStatement(sql);
                preStm.setString(1, classId);
                ResultSet rs = preStm.executeQuery();

                if (rs != null && rs.next()) {
                    String teacherId = rs.getNString("c.teacherId");
                    String name = rs.getNString("c.name");
                    String teacherName = rs.getNString("u.fullName");

                    c = new Class(classId, teacherId, name, teacherName);

                    // Fetch students
                    String sql1 = "SELECT s.userId, s.fullName, s.email, s.role\n" +
                            "FROM `User` s JOIN Student_Class sc\n" +
                            "ON s.userId = sc.studentId\n" +
                            "WHERE sc.classId = ? AND sc._status = 0\n" +
                            "ORDER BY sc._createdAt;";
                    PreparedStatement preStm1 = conn.prepareStatement(sql1);
                    preStm1.setString(1, classId);
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
        return c;
    }

    public static ArrayList<Class> getClassesByStudentId(String studentId) {
        Connection conn = null;
        ArrayList<Class> list = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT c.classId, c.teacherId, c.name, u.fullName\n" +
                        "FROM Student_Class sc JOIN Class c JOIN User u\n" +
                        "ON sc.classId = c.classId AND c.teacherId = u.userId\n" +
                        "WHERE sc.studentId = ? AND sc._status = 0\n" +
                        "ORDER BY sc._createdAt;";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, studentId);
                ResultSet rs = ps.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        String classId = rs.getNString("c.classId");
                        String teacherId = rs.getNString("c.teacherId");
                        String className = rs.getNString("c.name");
                        String teacherName = rs.getNString("u.fullName");

                        list.add(new Class(classId, teacherId, className, teacherName));
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

    public static Class createClass(Class c, String teacherId) {
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

    public static int enrollClass(String classId, String studentId) {
        Connection conn = null;
        PreparedStatement preStm = null;

        int result = 0;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Insert quiz
                String sql = "INSERT INTO Student_Class (classId, studentId) VALUES(?, ?) ON DUPLICATE KEY UPDATE _status = 0;";
                preStm = conn.prepareStatement(sql);

                preStm.setString(1, classId);
                preStm.setString(2, studentId);
                result = preStm.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
                return 0;
            }
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static int deleteClass(String deleteId, String teacherId) {
        Connection conn = null;

        ResultSet rs = null;

        int result = 0;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Delete quiz
                String sql = "UPDATE Student_Class SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE classId = ?";
                PreparedStatement preStm = conn.prepareStatement(sql);
                preStm.setString(1, deleteId);
                preStm.executeUpdate();

                sql = "UPDATE Class SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE classId = ? AND teacherId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, deleteId);
                preStm.setString(2, teacherId);
                result = preStm.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
                return 0;
            }
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static int leaveClass(String deleteId, String studentId) {
        Connection conn = null;

        ResultSet rs = null;

        int result = 0;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                String sql = "UPDATE Student_Class SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE classId = ? AND studentId = ?";
                PreparedStatement preStm = conn.prepareStatement(sql);
                preStm.setString(1, deleteId);
                preStm.setString(2, studentId);
                result = preStm.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
                return 0;
            }
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
