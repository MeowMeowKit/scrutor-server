package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.ClassDAO;
import dtos.Class;
import dtos.Question;
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

        ArrayList<Class> classes = ClassDAO.getClassesByTeacherId(userId);

        String json = DataUtil.readInputStream(req.getInputStream());
        Class c = GSON.fromJson(json, Class.class);

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
        String teacherId = req.getHeader("userId");

        String json = DataUtil.readInputStream(req.getInputStream());
        Class c = GSON.fromJson(json, Class.class);

        Class classes = ClassDAO.createNewClass(c, teacherId);

        res.setHeader("Content-Type", "application/json");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.getOutputStream().println(GSON.toJson(classes));
    }

    // Teacher PUT
    // /classes/:classId
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");

        String json = DataUtil.readInputStream(req.getInputStream());
        Class c = GSON.fromJson(json, Class.class);

        boolean result = ClassDAO.updateClass(c);

        resp.setHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.getOutputStream().println(GSON.toJson(result));

    }

    // Teacher POST
    // /classes/:classId

    // Student POST
    // /classes/leave/:classId
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String teacherId = req.getHeader("userId");
        String classId = req.getPathInfo().substring(1);

        boolean result = ClassDAO.deleteClass(classId);

        resp.setHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.getOutputStream().println(GSON.toJson(result));
    }
}
