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

@WebServlet(name = "QuestionAPI", value = "/questions/*")
public class QuestionsAPI extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    private void setAccessControlHeaders(HttpServletResponse res){
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
        String userId = req.getHeader("userId");

        ArrayList<Question> questions = QuestionDAO.getQuestions(userId);

        setAccessControlHeaders(res);
        res.getOutputStream().println(GSON.toJson(questions));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");

        String json = DataUtil.readInputStream(req.getInputStream());
        Question q = GSON.fromJson(json, Question.class);

        Question question = QuestionDAO.createQuestion(q, teacherId);

        setAccessControlHeaders(res);
        res.getOutputStream().println(GSON.toJson(question));
        return;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");
        String updateId = req.getPathInfo().substring(1);

        String json = DataUtil.readInputStream(req.getInputStream());
        Question newQuestion = GSON.fromJson(json, Question.class);

        int result = QuestionDAO.updateQuestion(updateId, newQuestion, teacherId);

        setAccessControlHeaders(res);
        if (result > 0)
            res.getOutputStream().println(GSON.toJson(newQuestion));
        else
            res.getOutputStream().println(GSON.toJson(null));
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");
        String deleteId = req.getPathInfo().substring(1);

        int result = QuestionDAO.deleteQuestion(deleteId, teacherId);

        setAccessControlHeaders(res);
        res.getOutputStream().println(GSON.toJson(result));
        return;
    }
}
