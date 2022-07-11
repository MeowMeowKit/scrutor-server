package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.QuizDAO;
import dtos.Attempt;
import dtos.Question;
import dtos.Quiz;
import utils.DataUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "QuizzesAPI", value = "/quizzes/*")
public class QuizAPI extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    private void setAccessControlHeaders(HttpServletResponse res) {
        res.setHeader("Content-Type", "application/json");
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Max-Age", "86400");
        res.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS, PUT, DELETE");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("Content-Type", "application/json");
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Max-Age", "86400");
        res.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS, PUT, DELETE");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String role = req.getHeader("role");
        String quizId = req.getPathInfo().substring("/".length());

        if (quizId.length() > 0) {
            Quiz q = QuizDAO.getQuizzesByQuizId(quizId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(q));

            return;
        } else {
            if (role.equals("teacher")) {

                String teacherId = req.getHeader("userId");

                ArrayList<Quiz> quizzes = QuizDAO.getQuizzesByTeacherId(teacherId);

                setAccessControlHeaders(res);
                res.getWriter().println(GSON.toJson(quizzes));

                return;
            } else if (role.equals("student")) {
                String studentId = req.getHeader("userId");

                ArrayList<Attempt> quizzes = QuizDAO.getAttemptByStudentId(studentId);

                setAccessControlHeaders(res);
                res.getWriter().println(GSON.toJson(quizzes));

                return;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String role = req.getHeader("role");

        if (role.equals("teacher")) {

            String teacherId = req.getHeader("userId");

            String json = DataUtil.readInputStream(req.getInputStream());
            Quiz q = GSON.fromJson(json, Quiz.class);

            Quiz quiz = QuizDAO.createQuiz(q, teacherId);


            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(quiz));

            return;
        } else if (role.equals("student")) {
            String studentId = req.getHeader("userId");

            String json = DataUtil.readInputStream(req.getInputStream());
            Attempt a = GSON.fromJson(json, Attempt.class);

            Attempt result = QuizDAO.attemptQuiz(a, studentId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(result));

            return;
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String role = req.getHeader("role");
        String updateId = req.getPathInfo().substring("/".length());

        if (role.equals("teacher")) {

            String teacherId = req.getHeader("userId");


            String json = DataUtil.readInputStream(req.getInputStream());
            Quiz q = GSON.fromJson(json, Quiz.class);

            Quiz quiz = QuizDAO.updateQuiz(updateId, q, teacherId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(quiz));

            return;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String role = req.getHeader("role");
        String deleteId = req.getPathInfo().substring("/".length());

        if (role.equals("teacher")) {

            String teacherId = req.getHeader("userId");

            int result = QuizDAO.deleteQuiz(deleteId, teacherId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(result));

            return;
        }
    }
}
