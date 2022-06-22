package api;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

//@WebServlet(name = "ClassAPI", value = "/ClassAPI")
public class ClassAPI extends HttpServlet {
    @Override
    // Teacher GET
    // /classes/

    // Student GET
    // /classes/attended
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }

    // Teacher POST
    // /classes/create/:classId

    // Student POST
    // /classes/attend/:classId
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }

    // Teacher PUT
    // /classes/:classId
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    // Teacher POST
    // /classes/:classId

    // Student POST
    // /classes/leave/:classId
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
