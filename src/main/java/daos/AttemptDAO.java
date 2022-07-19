package daos;

import dtos.*;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class AttemptDAO {
//    public static Attempt startAttempt(String quizId, String studentId) {
//        Connection conn = null;
//        PreparedStatement preStm = null;
//
//        Attempt attempt = new Attempt();
//
//        try {
//            conn = DBUtils.makeConnection();
//
//            if (conn != null) {
//                conn.setAutoCommit(false);
//
//                attempt.setAttemptId(UUID.randomUUID().toString());
//                Quiz quiz = new Quiz();
//                quiz.setQuizId(quizId);
//
//                String sql = "SELECT q.startAt, q.endAT, q.time FROM Quiz q WHERE q.quizId = ? AND q._status = 0";
//                preStm = conn.prepareStatement(sql);
//                preStm.setString(1, quizId);
//                ResultSet rs = preStm.executeQuery();
//
//                if (rs != null && rs.next()) {
//                    Timestamp startAt = rs.getTimestamp("q.startAt");
//                    Timestamp endAt = rs.getTimestamp("q.endAt");
//                    Timestamp time = rs.getTimestamp("q.time");
//                    attempt.setQuiz(quiz);
//                    attempt.setStudentId(studentId);
//
//                    // Insert attempt
//                    sql = "INSERT INTO Attempt (attemptId, quizId, studentId, startAt, endAt) VALUES (?, ?, ?, ?, ?)";
//                    preStm = conn.prepareStatement(sql);
//
//                    preStm.setString(1, attempt.getAttemptId());
//                    preStm.setString(2, attempt.getQuiz().getQuizId());
//                    preStm.setString(3, studentId);
//                    preStm.setTimestamp(4, attempt.getStartAt());
//                    preStm.setTimestamp(5, attempt.getEndAt());
//                    preStm.executeUpdate();
//                }
//
//                conn.commit();
//                conn.setAutoCommit(true);
//            }
//        } catch (Exception e) {
//            try {
//                conn.rollback();
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                return null;
//            }
//            e.printStackTrace();
//            return null;
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return attempt;
//    }

    public static Attempt startAttempt(Attempt attempt, String studentId) {
        Connection conn = null;
        PreparedStatement preStm = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                attempt.setAttemptId(UUID.randomUUID().toString());

                attempt.setStudentId(studentId);

                // Insert attempt
                String sql = "INSERT INTO Attempt (attemptId, quizId, studentId, startAt, endAt) VALUES (?, ?, ?, ?, ?)";
                preStm = conn.prepareStatement(sql);

                preStm.setString(1, attempt.getAttemptId());
                preStm.setString(2, attempt.getQuiz().getQuizId());
                preStm.setString(3, attempt.getStudentId());
                preStm.setTimestamp(4, attempt.getStartAt());
                preStm.setTimestamp(5, attempt.getEndAt());
                preStm.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
            }
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

        return attempt;
    }


    public static Attempt submitAttempt(Attempt attempt, String studentId) {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                attempt.setAttemptId(UUID.randomUUID().toString());
                attempt.setStudentId(studentId);

                // Insert attempt
                String sql = "INSERT INTO Attempt (attemptId, quizId, studentId, grade, maxGrade) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Attempt SET grade = ?, maxGrade = ? WHERE AttemptId = ?";
                preStm = conn.prepareStatement(sql);

                preStm.setString(1, attempt.getAttemptId());
                preStm.setString(2, attempt.getQuiz().getQuizId());
                preStm.setString(3, studentId);
                preStm.setDouble(4, attempt.getGrade());
                preStm.setDouble(5, attempt.getMaxGrade());
                preStm.setDouble(6, attempt.getGrade());
                preStm.setDouble(7, attempt.getMaxGrade());
                preStm.setString(8, attempt.getAttemptId());
                preStm.executeUpdate();

                for (AttemptQuestion aq : attempt.getAttemptQuestions()) {
                    String attemptQuestionId = UUID.randomUUID().toString();
                    aq.setAttemptQuestionId(attemptQuestionId);
                    String sql1 = "INSERT INTO AttemptQuestion (attemptQuestionId, questionId, attemptId, fillAnswer) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps1 = conn.prepareStatement(sql1);

                    ps1.setString(1, aq.getAttemptQuestionId());
                    ps1.setString(2, aq.getQuestion().getQuestionId());
                    ps1.setString(3, attempt.getAttemptId());
                    ps1.setString(4, aq.getFillAnswer());
                    ps1.executeUpdate();

                    for (AttemptOption ao : aq.getAttemptOptions()) {
                        String attemptOptionId = UUID.randomUUID().toString();
                        ao.setAttemptOptionId(attemptOptionId);
                        String sql2 = "INSERT INTO AttemptOption (attemptOptionId, optionId, attemptQuestionId, isChecked) VALUES (?, ?, ?, ?)";
                        PreparedStatement ps2 = conn.prepareStatement(sql2);

                        ps2.setString(1, ao.getAttemptOptionId());
                        ps2.setString(2, ao.getOption().getOptionId());
                        ps2.setString(3, attemptQuestionId);
                        ps2.setBoolean(4, ao.getIsChecked());
                        ps2.executeUpdate();
                    }
                }

                conn.commit();
                conn.setAutoCommit(true);
            }
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

        return attempt;
    }

    public static ArrayList<Attempt> getAttemptByStudentId(String studentId) {
        Connection conn = null;

        ArrayList<Attempt> list = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                conn.setAutoCommit(false);

                // Fetch attempts
                String sql = "SELECT a.attemptId, a.quizId, a.grade, a.maxGrade, a.startAt, a.endAt, q.teacherId, q.title, q.description, q.startAt, q.endAt, q.time\n" +
                        "FROM Attempt a JOIN Quiz q\n" +
                        "ON a.quizId = q.quizId\n" +
                        "WHERE a.studentId = ? AND a._status = 0\n" +
                        "ORDER BY a._createdAt;";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, studentId);
                ResultSet rs = ps.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        String attemptId = rs.getString("a.attemptId");
                        String quizId = rs.getString("a.quizId");
                        double grade = rs.getDouble("a.grade");
                        double maxGrade = rs.getDouble("a.maxGrade");
                        Timestamp attemptStartAt = rs.getTimestamp("a.startAt");
                        Timestamp attemptEndAt = rs.getTimestamp("a.endAt");
                        String teacherQuizId = rs.getString("q.teacherId");
                        String title = rs.getString("q.title");
                        String description = rs.getString("q.description");
                        Timestamp startAt = rs.getTimestamp("q.startAt");
                        Timestamp endAt = rs.getTimestamp("q.endAt");
                        Timestamp time = rs.getTimestamp("q.time");

                        Attempt attempt = new Attempt(attemptId, studentId, new Quiz(quizId, teacherQuizId, title, description, startAt, endAt, time), grade, maxGrade, attemptStartAt, attemptEndAt);

                        // Fetch attempt questions
                        String sql1 = "SELECT aq.attemptQuestionId, aq.questionId, aq.fillAnswer, q.teacherId, q.content, q.type, q.difficulty, qq.point\n" +
                                "FROM AttemptQuestion aq JOIN Question q JOIN Quiz_Question qq\n" +
                                "ON aq.questionId = q.questionId\n AND qq.questionId = aq.questionId AND qq.quizId = ?" +
                                "WHERE aq.attemptId = ?\n" +
                                "ORDER BY aq._createdAt";
                        PreparedStatement ps1 = conn.prepareStatement(sql1);
                        ps1.setString(1, quizId);
                        ps1.setString(2, attemptId);
                        ResultSet rs1 = ps1.executeQuery();

                        if (rs1 != null) {
                            while (rs1.next()) {
                                String attemptQuestionId = rs1.getString("aq.attemptQuestionId");
                                String questionId = rs1.getString("aq.questionId");

                                String fillAnswer = rs1.getString("aq.fillAnswer");
                                String teacherId = rs1.getString("q.teacherId");
                                String questionContent = rs1.getString("q.content");
                                String type = rs1.getString("q.type");
                                int difficulty = rs1.getInt("q.difficulty");
                                int point = rs1.getInt("qq.point");

                                AttemptQuestion attemptQuestion = new AttemptQuestion(attemptQuestionId, new Question(questionId, teacherId, questionContent, type, difficulty, point), fillAnswer);

                                // Fetch attempt options
                                String sql2 = "SELECT ao.attemptOptionId, ao.optionId, ao.isChecked, o.content, o.isCorrect\n" +
                                        "FROM AttemptOption ao JOIN `Option` o\n" +
                                        "ON ao.optionId = o.optionId\n" +
                                        "WHERE ao.attemptQuestionId = ?\n" +
                                        "ORDER BY ao._createdAt";
                                PreparedStatement ps2 = conn.prepareStatement(sql2);
                                ps2.setString(1, attemptQuestionId);
                                ResultSet rs2 = ps2.executeQuery();

                                if (rs2 != null) {
                                    while (rs2.next()) {
                                        String attemptOptionId = rs2.getString("ao.attemptOptionId");
                                        String optionId = rs2.getString("ao.optionId");
                                        boolean isChecked = rs2.getBoolean("ao.isChecked");
                                        String optionContent = rs2.getString("o.content");
                                        boolean isCorrect = rs2.getBoolean("o.isCorrect");

                                        attemptQuestion.addAttemptOption(new AttemptOption(attemptOptionId, new Option(optionId, optionContent, isCorrect), isChecked));
                                    }
                                }

                                attempt.addAttempQuestion(attemptQuestion);
                            }
                        }

                        list.add(attempt);
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
}
