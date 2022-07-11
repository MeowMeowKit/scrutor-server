package daos;

import dtos.Question;
import dtos.Option;
import dtos.Tag;
import utils.DBUtils;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class QuestionDAO {
    public static ArrayList<Question> getQuestions(String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        ArrayList<Question> list = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT q.questionId, q.content, q.type, q.difficulty\n" +
                        "FROM Question q\n" +
                        "WHERE teacherId = ? AND q._status = 0\n" +
                        "ORDER BY q._createdAt;";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, teacherId);
                rs = preStm.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        String questionId = rs.getString("q.questionId");
                        String questionContent = rs.getString("q.content");
                        String type = rs.getString("q.type");
                        int difficulty = rs.getInt("q.difficulty");

                        Question q = new Question(questionId, teacherId, questionContent, type, difficulty);

                        // Fetch tags
                        sql = "SELECT t.tagId, t.name, qt.questionId\n" +
                                "FROM Tag t\n" +
                                "INNER JOIN Question_Tag qt\n" +
                                "ON t.tagId = qt.tagId\n" +
                                "WHERE qt.questionId = ? AND qt._status = 0\n" +
                                "ORDER BY qt._createdAt;";

                        preStm = null;
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, questionId);

                        ResultSet rs1 = preStm.executeQuery();
                        if (rs1 != null) {
                            while (rs1.next()) {
                                String tagId = rs1.getString("t.tagId");
                                String name = rs1.getString("t.name");

                                q.addTag(new Tag(tagId, name));
                            }
                        }

                        // Fetch options
                        sql = "SELECT o.optionId, o.content, o.isCorrect\n" +
                                "FROM `Option` o\n" +
                                "WHERE o.questionId = ? AND o._status = 0\n" +
                                "ORDER BY o._createdAt;";

                        preStm = null;
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, questionId);

                        rs1 = preStm.executeQuery();
                        if (rs1 != null) {
                            while (rs1.next()) {
                                String optionId = rs1.getString("o.optionId");
                                String optionContent = rs1.getString("o.content");
                                boolean isCorrect = rs1.getBoolean("o.isCorrect");

                                q.addOption(new Option(optionId, questionId, optionContent, isCorrect));
                            }
                        }

                        list.add(q);
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

    public static Question getQuestionByQuestionId(String questionId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        Question q = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT q.teacherId, q.content, q.type, q.difficulty\n" +
                        "FROM Question q\n" +
                        "WHERE q.questionId = ? AND q._status = 0\n" +
                        "ORDER BY q._createdAt;";

                preStm = conn.prepareStatement(sql);
                preStm.setString(1, questionId);
                rs = preStm.executeQuery();

                if (rs != null && rs.next()) {
                    String teacherId = rs.getString("q.teacherId");
                    String questionContent = rs.getString("q.content");
                    String type = rs.getString("q.type");
                    int difficulty = rs.getInt("q.difficulty");

                    q = new Question(questionId, teacherId, questionContent, type, difficulty);

                    // Fetch tags
                    sql = "SELECT t.tagId, t.name, qt.questionId\n" +
                            "FROM Tag t\n" +
                            "INNER JOIN Question_Tag qt\n" +
                            "ON t.tagId = qt.tagId\n" +
                            "WHERE qt.questionId = ? AND qt._status = 0\n" +
                            "ORDER BY qt._createdAt;";

                    preStm = null;
                    preStm = conn.prepareStatement(sql);
                    preStm.setString(1, questionId);

                    ResultSet rs1 = preStm.executeQuery();
                    if (rs1 != null) {
                        while (rs1.next()) {
                            String tagId = rs1.getString("t.tagId");
                            String name = rs1.getString("t.name");

                            q.addTag(new Tag(tagId, name));
                        }
                    }

                    // Fetch options
                    sql = "SELECT o.optionId, o.content, o.isCorrect\n" +
                            "FROM `Option` o\n" +
                            "WHERE o.questionId = ?; AND o._status = 0\n" +
                            "ORDER BY o._createdAt;";

                    preStm = null;
                    preStm = conn.prepareStatement(sql);
                    preStm.setString(1, questionId);

                    rs1 = preStm.executeQuery();
                    if (rs1 != null) {
                        while (rs1.next()) {
                            String optionId = rs1.getString("o.optionId");
                            String optionContent = rs1.getString("o.content");
                            boolean isCorrect = rs1.getBoolean("o.isCorrect");

                            q.addOption(new Option(optionId, questionId, optionContent, isCorrect));
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
        return q;
    }

    public static Question createQuestion(Question q, String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                q.setTeacherId(teacherId);
                q.setQuestionId(UUID.randomUUID().toString());

                // Insert question
                String sql = "INSERT INTO Question (questionId, teacherId, content, type, difficulty) VALUES(?, ?, ?, ?, ?);";
                preStm = conn.prepareStatement(sql);

                preStm.setString(1, q.getQuestionId());
                preStm.setString(2, q.getTeacherId());
                preStm.setString(3, q.getContent());
                preStm.setString(4, q.getType());
                preStm.setInt(5, q.getDifficulty());
                preStm.executeUpdate();

                // Insert options
                for (Option o : q.getOptions()) {
                    try {
                        o.setOptionId(UUID.randomUUID().toString());
                        o.setQuestionId(q.getQuestionId());

                        preStm = null;
                        sql = "INSERT INTO `Option` (optionId, questionId, content, isCorrect) VALUES (?, ?, ?, ?);";
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, o.getOptionId());
                        preStm.setString(2, o.getQuestionId());
                        preStm.setString(3, o.getContent());
                        preStm.setBoolean(4, o.getIsCorrect());
                        preStm.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Insert tags
                for (Tag t : q.getTags()) {
                    try {
                        sql = "SELECT tagId FROM Tag WHERE name = ? LIMIT 1";
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, t.getName());
                        rs = preStm.executeQuery();


                        // Check whether tag is exist in Tag
                        if (rs != null && rs.next()) {
                            String tagId = rs.getString("tagId");
                            t.setTagId(tagId);
                        } else {
                            try {
                                t.setTagId(UUID.randomUUID().toString());

                                preStm = null;
                                sql = "INSERT INTO Tag (tagId, name) VALUES (?, ?);";
                                preStm = conn.prepareStatement(sql);
                                preStm.setString(1, t.getTagId());
                                preStm.setString(2, t.getName());
                                preStm.executeUpdate();
                            } catch (Exception tagException) {
                                tagException.printStackTrace();
                            }
                        }

                        try {
                            preStm = null;
                            sql = "INSERT INTO Question_Tag (questionId, tagId) VALUES (?, ?);";
                            preStm = conn.prepareStatement(sql);
                            preStm.setString(1, q.getQuestionId());
                            preStm.setString(2, t.getTagId());
                            preStm.executeUpdate();
                        } catch (Exception tagException) {
                            tagException.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
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

        return q;
    }

    public static int updateQuestion(String updateId, Question q, String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        int result = -1;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Update question info
                String sql = "UPDATE Question SET content = ?, type = ?, difficulty = ?, _updatedAt = CURRENT_TIMESTAMP(6) WHERE questionId = ? AND teacherId = ?;";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, q.getContent());
                preStm.setString(2, q.getType());
                preStm.setInt(3, q.getDifficulty());
                preStm.setString(4, updateId);
                preStm.setString(5, teacherId);
                result = preStm.executeUpdate();

                // Delete old options of the question
                preStm = null;
                sql = "UPDATE `Option` SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE questionId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, updateId);
                preStm.executeUpdate();

                // Delete old tags of the question
                preStm = null;
                sql = "UPDATE Question_Tag SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE questionId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, updateId);
                preStm.executeUpdate();

                // Insert new options
                for (Option o : q.getOptions()) {
                    try {
                        o.setOptionId(UUID.randomUUID().toString());
                        o.setQuestionId(updateId);

                        preStm = null;
                        sql = "INSERT INTO `Option` (optionId, questionId, content, isCorrect) VALUES (?, ?, ?, ?);";
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, o.getOptionId());
                        preStm.setString(2, o.getQuestionId());
                        preStm.setString(3, o.getContent());
                        preStm.setBoolean(4, o.getIsCorrect());
                        preStm.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Insert new tags
                for (Tag t : q.getTags()) {
                    try {
                        sql = "SELECT tagId FROM Tag WHERE name = ? LIMIT 1";
                        preStm = conn.prepareStatement(sql);
                        preStm.setString(1, t.getName());
                        rs = preStm.executeQuery();


                        // Check whether tag is exist in Tag
                        if (rs != null && rs.next()) {
                            String tagId = rs.getString("tagId");
                            t.setTagId(tagId);
                        } else {
                            try {
                                t.setTagId(UUID.randomUUID().toString());

                                preStm = null;
                                sql = "INSERT INTO Tag (tagId, name) VALUES (?, ?);";
                                preStm = conn.prepareStatement(sql);
                                preStm.setString(1, t.getTagId());
                                preStm.setString(2, t.getName());
                                preStm.executeUpdate();
                            } catch (Exception tagException) {
                                tagException.printStackTrace();
                            }
                        }

                        try {
                            preStm = null;
                            sql = "INSERT INTO Question_Tag (questionId, tagId) VALUES (?, ?) ON DUPLICATE KEY UPDATE _status = 0;";
                            preStm = conn.prepareStatement(sql);
                            preStm.setString(1, updateId);
                            preStm.setString(2, t.getTagId());
                            preStm.executeUpdate();
                        } catch (Exception tagException) {
                            tagException.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return result;
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
                return result;
            }
            e.printStackTrace();
            return result;
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

    public static int deleteQuestion(String questionId, String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        int result = -1;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Update question info
                String sql = "";

                // Delete options
                preStm = null;
                sql = "UPDATE `Option` SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE questionId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, questionId);
                preStm.executeUpdate();

                // Delete tags
                preStm = null;
                sql = "UPDATE Question_Tag SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE questionId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, questionId);
                preStm.executeUpdate();

                // Delete question
                preStm = null;
                sql = "UPDATE Question SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE questionId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, questionId);
                result = preStm.executeUpdate();
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
