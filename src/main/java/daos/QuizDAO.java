package daos;

import dtos.*;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class QuizDAO {

    public static ArrayList<Quiz> getQuizzesByTeacherId(String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        ArrayList<Quiz> list = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT q.quizId, q.title, q.description, q.startAt, q.endAt, q.time\n" +
                        "FROM Quiz q\n" +
                        "WHERE q.teacherId = ? AND q._status = 0\n" +
                        "ORDER BY q._createdAt;";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, teacherId);
                rs = preStm.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        String quizId = rs.getString("q.quizId");
                        String title = rs.getString("q.title");
                        String description = rs.getString("q.description");
                        Timestamp startAt = rs.getTimestamp("q.startAt");
                        Timestamp endAt = rs.getTimestamp("q.endAt");
                        Timestamp time = rs.getTimestamp("q.time");

                        Quiz quiz = new Quiz(quizId, teacherId, title, description, startAt, endAt, time);

                        try {
                            sql = "SELECT qq.questionId, qq.point\n" +
                                    "FROM Quiz_Question qq\n" +
                                    "WHERE qq.quizId = ? AND qq._status = 0\n" +
                                    "ORDER BY qq._createdAt";
                            PreparedStatement preStm1 = conn.prepareStatement(sql);
                            preStm1.setString(1, quizId);
                            ResultSet rs1 = preStm1.executeQuery();

                            if (rs1 != null) {
                                while (rs1.next()) {
                                    String questionId = rs1.getString("qq.questionId");
                                    int point = rs1.getInt("qq.point");

                                    Question question = QuestionDAO.getQuestionByQuestionId(questionId);
                                    question.setPoint(point);
                                    quiz.addQuestion(question);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        list.add(quiz);
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

    public static Quiz getQuizzesByQuizId(String quizId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        Quiz result = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT q.teacherId, q.title, q.description, q.startAt, q.endAt, q.time\n" +
                        "FROM Quiz q\n" +
                        "WHERE q.quizId = ? AND q._status = 0\n" +
                        "ORDER BY q._createdAt";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, quizId);
                rs = preStm.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        String teacherId = rs.getString("q.teacherId");
                        String title = rs.getString("q.title");
                        String description = rs.getString("q.description");
                        Timestamp startAt = rs.getTimestamp("q.startAt");
                        Timestamp endAt = rs.getTimestamp("q.endAt");
                        Timestamp time = rs.getTimestamp("q.time");

                        Quiz quiz = new Quiz(quizId, teacherId, title, description, startAt, endAt, time);

                        try {
                            sql = "SELECT qq.questionId, qq.point\n" +
                                    "FROM Quiz_Question qq\n" +
                                    "WHERE qq.quizId = ? AND qq._status = 0\n" +
                                    "ORDER BY qq._createdAt";
                            PreparedStatement preStm1 = conn.prepareStatement(sql);
                            preStm1.setString(1, quizId);
                            ResultSet rs1 = preStm1.executeQuery();

                            if (rs1 != null) {
                                while (rs1.next()) {
                                    String questionId = rs1.getString("qq.questionId");
                                    int point = rs1.getInt("qq.point");

                                    Question question = QuestionDAO.getQuestionByQuestionId(questionId);
                                    question.setPoint(point);
                                    quiz.addQuestion(question);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        result = quiz;
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
        return result;
    }

    public static Quiz createQuiz(Quiz q, String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                q.setTeacherId(teacherId);
                q.setQuizId(UUID.randomUUID().toString());

                // Insert quiz
                String sql = "INSERT INTO Quiz (quizId, teacherId, title, description, startAt, endAt, time) VALUES(?, ?, ?, ?, ?, ?, ?);";
                preStm = conn.prepareStatement(sql);

                preStm.setString(1, q.getQuizId());
                preStm.setString(2, q.getTeacherId());
                preStm.setString(3, q.getTitle());
                preStm.setString(4, q.getDescription());
                preStm.setTimestamp(5, q.getStartAt());
                preStm.setTimestamp(6, q.getEndAt());
                preStm.setTimestamp(7, q.getTime());
                preStm.executeUpdate();

                // Insert questions
                for (Question que : q.getQuestions()) {
                    sql = "INSERT INTO Quiz_Question (quizId, questionId, point) VALUES(?, ?, ?);";
                    preStm = conn.prepareStatement(sql);

                    preStm.setString(1, q.getQuizId());
                    preStm.setString(2, que.getQuestionId());
                    preStm.setInt(3, que.getPoint());

                    preStm.executeUpdate();
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

    public static Quiz updateQuiz(String updateId, Quiz q, String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Update quiz
                String sql = "UPDATE Quiz SET title = ?, description = ?, startAt = ?, endAt = ?, time = ?, _updatedAt = CURRENT_TIMESTAMP(6) WHERE quizId = ? AND teacherId = ?;";
                preStm = conn.prepareStatement(sql);

                preStm.setString(1, q.getTitle());
                preStm.setString(2, q.getDescription());
                preStm.setTimestamp(3, q.getStartAt());
                preStm.setTimestamp(4, q.getEndAt());
                preStm.setTimestamp(5, q.getTime());
                preStm.setString(6, updateId);
                preStm.setString(7, teacherId);
                preStm.executeUpdate();


                // Delete old questions
                preStm = null;
                sql = "UPDATE Quiz_Question SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE quizId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, updateId);
                preStm.executeUpdate();

                // Insert new questions
                for (Question que : q.getQuestions()) {
                    sql = "INSERT INTO Quiz_Question (quizId, questionId, point) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE _status = 0, point = ?;";
                    preStm = conn.prepareStatement(sql);

                    preStm.setString(1, updateId);
                    preStm.setString(2, que.getQuestionId());
                    preStm.setInt(3, que.getPoint());
                    preStm.setInt(4, que.getPoint());

                    preStm.executeUpdate();
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

    public static int deleteQuiz(String deleteId, String teacherId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        int result = 0;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Delete old questions
                preStm = null;
                String sql = "UPDATE Quiz_Question SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE quizId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, deleteId);
                result = preStm.executeUpdate();

                // Delete quiz
                preStm = null;
                sql = "UPDATE Quiz SET _status = 1, _updatedAt = CURRENT_TIMESTAMP(6) WHERE quizId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, deleteId);
                preStm.executeUpdate();
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
