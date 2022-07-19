package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.AttemptDAO;
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

@WebServlet(name = "AttemptAPI", value = "/attempts/*")
public class AttemptAPI extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    private void setAccessControlHeaders(HttpServletResponse res) {
        res.setHeader("Content-Type", "application/json; charset=UTF-8");
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Max-Age", "86400");
        res.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS, PUT, DELETE");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("Content-Type", "application/json; charset=UTF-8");
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Max-Age", "86400");
        res.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS, PUT, DELETE");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String role = req.getHeader("role");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String role = req.getHeader("role");


        if (role.equals("student")) {
            String studentId = req.getHeader("userId");

            String path = req.getPathInfo().substring("/".length());
            if (path.contains("start")) {
                String json = DataUtil.readInputStream(req.getInputStream());
                Attempt a = GSON.fromJson(json, Attempt.class);

                Attempt result = AttemptDAO.startAttempt(a, studentId);
                setAccessControlHeaders(res);
                res.getWriter().println(GSON.toJson(result));
                return;
            } else if (path.contains("submit")) {
                String json = DataUtil.readInputStream(req.getInputStream());
                Attempt a = GSON.fromJson(json, Attempt.class);

                Attempt result = AttemptDAO.submitAttempt(a, studentId);

                setAccessControlHeaders(res);
                res.getWriter().println(GSON.toJson(result));
                return;
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String role = req.getHeader("role");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String role = req.getHeader("role");
    }
}
