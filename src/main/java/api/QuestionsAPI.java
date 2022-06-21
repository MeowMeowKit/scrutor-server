package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.QuestionDAO;
import dtos.Question;
import dtos.User;
import utils.DataUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

//@WebServlet(name = "QuestionAPI", value = "/QuestionAPI")
public class QuestionsAPI extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userId = req.getHeader("userId");

        ArrayList<Question> questions = QuestionDAO.getQuestions(userId);

        res.setHeader("Content-Type", "application/json");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.getOutputStream().println(GSON.toJson(questions));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");

        String json = DataUtil.readInputStream(req.getInputStream());
        Question q = GSON.fromJson(json, Question.class);

        Question question = QuestionDAO.createQuestion(q, teacherId);

        res.setHeader("Content-Type", "application/json");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.getOutputStream().println(GSON.toJson(question));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");

        String json = DataUtil.readInputStream(req.getInputStream());
        Question q = GSON.fromJson(json, Question.class);

        int result = QuestionDAO.updateQuestion(q, teacherId);

        res.setHeader("Content-Type", "application/json");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.getOutputStream().println(GSON.toJson(result));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");
        String deleteId = req.getPathInfo().substring(1);

        int result = QuestionDAO.deleteQuestion(deleteId, teacherId);

        res.setHeader("Content-Type", "application/json");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.getOutputStream().println(GSON.toJson(result));
    }
}
