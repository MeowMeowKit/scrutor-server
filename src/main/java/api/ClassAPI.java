package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.ClassDAO;
import daos.QuizDAO;
import daos.UserDAO;
import dtos.Class;
import dtos.Quiz;
import utils.DataUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;


//@WebServlet(name = "ClassAPI", value = "/ClassAPI")
public class ClassAPI extends HttpServlet {
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
    // Teacher GET
    // /classes/

    // Student GET
    // /classes/attended
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userId = req.getHeader("userId");
        String role = req.getHeader("role");

        String classId = req.getPathInfo().substring("/".length());

        if (classId.length() > 0) {
            Class c = ClassDAO.getClassByClassId(classId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(c));

            return;
        } else {
            if (role.equals("teacher")) {
                ArrayList<Class> classes = ClassDAO.getClassesByTeacherId(userId);

                setAccessControlHeaders(res);
                res.getWriter().println(GSON.toJson(classes));
                return;
            } else {
                ArrayList<Class> classes = ClassDAO.getClassesByStudentId(userId);

                setAccessControlHeaders(res);
                res.getWriter().println(GSON.toJson(classes));
                return;
            }
        }


    }

    // Teacher POST
    // /classes/:classId

    // Student POST
    // /classes/:classId
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userId = req.getHeader("userId");
        String role = req.getHeader("role");

        String json = DataUtil.readInputStream(req.getInputStream());
        Class c = GSON.fromJson(json, Class.class);

        if (role.equals("teacher")) {
            Class newClass = ClassDAO.createClass(c, userId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(newClass));
        } else {
            String classId = req.getPathInfo().substring("/".length());
            int attendance = ClassDAO.enrollClass(classId, userId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(attendance));
        }
    }
//
//    // Teacher PUT
//    // /classes/:classId
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        String teacherId = req.getHeader("userId");
//
//        String json = DataUtil.readInputStream(req.getInputStream());
//        Class c = GSON.fromJson(json, Class.class);
//
//        int result = ClassDAO.updateClass(c);
//
//        setAccessControlHeaders(res);
//        res.getWriter().println(GSON.toJson(result));
//
//    }
//

    // Teacher DELETE
    // /classes/:classId

    // Student DELETE
    // /classes/leave/:classId
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String role = req.getHeader("role");

        if (role.equals("teacher")) {
            String teacherId = req.getHeader("userId");
            String classId = req.getPathInfo().substring("/".length());

            int result = ClassDAO.deleteClass(classId, teacherId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(result));
        } else {
            String studentId = req.getHeader("userId");
            String classId = req.getPathInfo().substring("/".length());

            int result = ClassDAO.leaveClass(classId, studentId);

            setAccessControlHeaders(res);
            res.getWriter().println(GSON.toJson(result));
        }
    }
}
