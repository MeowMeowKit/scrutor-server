/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import dtos.Option;
import dtos.Question;
import dtos.Tag;
import dtos.User;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;


public class UserDAO {
    public static User getUserByEmailAndPassword(String email, String password) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        User user = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch user
                String sql = "SELECT u.userId, u.fullName, u.role\n" +
                        "FROM `User` u WHERE u.email = ? AND u.password = ? LIMIT 1;";
                preStm = conn.prepareStatement(sql);
                preStm.setNString(1, email);
                preStm.setNString(2, password);
                rs = preStm.executeQuery();

                if (rs != null && rs.next()) {
                    String userId = rs.getNString("u.userId");
                    String fullName = rs.getNString("u.fullName");
                    String role = rs.getNString("u.role");

                    user = new User(userId, fullName, email, password, role);
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

        return user;
    }

    public static User getUserByUserId(String userId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch user
                String sql = "SELECT u.userId, u.fullName, u.email, u.role\n" +
                        "FROM `User` u WHERE u.userId = ? LIMIT 1;";
                preStm = conn.prepareStatement(sql);
                preStm.setNString(1, userId);
                rs = preStm.executeQuery();

                if (rs != null && rs.next()) {
                    String fullName = rs.getNString("u.fullName");
                    String email = rs.getNString("u.email");
                    String role = rs.getNString("u.role");

                    user = new User();
                    user.setUserId(userId);
                    user.setFullName(fullName);
                    user.setEmail(email);
                    user.setRole(role);
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

        return user;
    }

    public static User createUser(User user) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                String userId = UUID.randomUUID().toString();
                user.setUserId(userId);
                String sql = "INSERT INTO `User`(userId, fullname, email, password, role) VALUES (?, ?, ?, ?, ?);";
                preStm = conn.prepareStatement(sql);
                preStm.setNString(1, userId);
                preStm.setNString(2, user.getFullName());
                preStm.setNString(3, user.getEmail());
                preStm.setNString(4, user.getPassword());
                preStm.setNString(5, user.getRole());
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

        return user;
    }
    public static User checkRole(String userId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch user
                String sql = "SELECT u.role\n" +
                        "FROM User u WHERE u.userId = ? ";
                preStm = conn.prepareStatement(sql);
                preStm.setNString(1, userId);
                rs = preStm.executeQuery();

                if (rs != null && rs.next()) {
                    String role = rs.getNString("u.role");

                    user = new User();
                    user.setUserId(userId);
                    user.setRole(role);
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

        return user;
    }

}
