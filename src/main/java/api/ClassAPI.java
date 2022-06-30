package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.ClassDAO;
import daos.UserDAO;
import dtos.Class;
import utils.DataUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;


//@WebServlet(name = "ClassAPI", value = "/ClassAPI")
public class ClassAPI extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    // Teacher GET
    // /classes/

    // Student GET
    // /classes/attended
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userId = req.getHeader("userId");
        String role = req.getHeader("role");

        ArrayList<Class> classes = new ArrayList<>();

        if (role.equals("teacher")) {
            classes = ClassDAO.getClassesByTeacherId(userId);
        } else {
            classes = ClassDAO.getClassesByStudentId(userId);

        }
        res.setHeader("Content-Type", "application/json");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.getOutputStream().println(GSON.toJson(classes));
    }

    // Teacher POST
    // /classes/create/:classId

    // Student POST
    // /classes/attend/:classId
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userId = req.getHeader("userId");
        String role = req.getHeader("role");

        String json = DataUtil.readInputStream(req.getInputStream());
        Class c = GSON.fromJson(json, Class.class);

        if (role.equals("teacher")) {
            Class classes = ClassDAO.createNewClass(c, userId);

            res.setHeader("Content-Type", "application/json");
            res.addHeader("Access-Control-Allow-Origin", "*");
            res.getOutputStream().println(GSON.toJson(classes));
        } else {
            int attendance = ClassDAO.attendNewClass(c, userId);

            res.setHeader("Content-Type", "application/json");
            res.addHeader("Access-Control-Allow-Origin", "*");
            res.getOutputStream().println(GSON.toJson(attendance));
        }
    }

    // Teacher PUT
    // /classes/:classId
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");

        String json = DataUtil.readInputStream(req.getInputStream());
        Class c = GSON.fromJson(json, Class.class);

        int result = ClassDAO.updateClass(c);

        resp.setHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.getOutputStream().println(GSON.toJson(result));

    }

    // Teacher DELETE
    // /classes/:classId

    // Student DELETE
    // /classes/leave/:classId
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getHeader("userId");
        String role = req.getHeader("role");
        if (role.equals("teacher")) {
            String classId = req.getPathInfo().substring("/".length());
            int result = ClassDAO.deleteClass(classId);

            resp.setHeader("Content-Type", "application/json");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.getOutputStream().println(GSON.toJson(result));
        } else {
            String classId = req.getPathInfo().substring("/leave".length());
            int result = ClassDAO.leaveClass(userId, classId);

            resp.setHeader("Content-Type", "application/json");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.getOutputStream().println(GSON.toJson(result));
        }
    }
}
