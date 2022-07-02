package daos;

import dtos.Option;
import dtos.Question;
import dtos.Quiz;
import dtos.Tag;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

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

    public static ArrayList<Quiz> getQuizzesByTeacherId(String teacherId) {
        conn = null;
        preStm = null;
        rs = null;
        ArrayList<Quiz> list = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch questions
                String sql = "SELECT q.quizId, q.title, q.description, q.startAt, q.endAt, q.time\n" +
                        "FROM Quiz q\n" +
                        "WHERE q.teacherId = ?\n";
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
                                    "WHERE qq.quizId = ?\n";
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
            closeConnection();
        }
        return list;
    }

    public static Quiz createQuiz(Quiz q, String teacherId) {
        conn = null;
        preStm = null;
        rs = null;

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
            closeConnection();
        }

        return q;
    }

    public static Quiz updateQuiz(String updateId, Quiz q, String teacherId) {
        conn = null;
        preStm = null;
        rs = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Update quiz
                String sql = "UPDATE Quiz SET title = ?, description = ?, startAt = ?, endAt = ?, time = ? WHERE quizId = ? AND teacherId = ?;";
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
                sql = "DELETE FROM Quiz_Question WHERE quizId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, updateId);
                preStm.executeUpdate();

                // Insert new questions
                for (Question que : q.getQuestions()) {
                    sql = "INSERT INTO Quiz_Question (quizId, questionId, point) VALUES(?, ?, ?);";
                    preStm = conn.prepareStatement(sql);

                    preStm.setString(1, updateId);
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
            closeConnection();
        }

        return q;
    }

    public static int deleteQuiz(String deleteId, String teacherId) {
        conn = null;
        preStm = null;
        rs = null;

        int result = 0;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Delete old questions
                preStm = null;
                String sql = "DELETE FROM Quiz_Question WHERE quizId = ?";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, deleteId);
                result = preStm.executeUpdate();

                // Delete quiz
                preStm = null;
                sql = "DELETE FROM Quiz WHERE quizId = ?";
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
            closeConnection();
        }

        return result;
    }
}
