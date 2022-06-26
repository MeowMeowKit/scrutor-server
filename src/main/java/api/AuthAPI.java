package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.UserDAO;
import dtos.User;
import utils.DataUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

//@WebServlet(name = "AuthAPI", value = "/AuthAPI")
public class AuthAPI extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    private void setAccessControlHeaders(HttpServletResponse res){
        res.setHeader("Content-Type", "application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setHeader("Access-Control-Max-Age", "86400");
        res.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("Content-Type", "application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setHeader("Access-Control-Max-Age", "86400");

        res.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getPathInfo().substring(1);

        if (action.equals("login")) {
            String json = DataUtil.readInputStream(req.getInputStream());
            User u = GSON.fromJson(json, User.class);

            User user = UserDAO.getUserByEmailAndPassword(u.getEmail(), u.getPassword());

            setAccessControlHeaders(res);
            res.getOutputStream().println(GSON.toJson(user));
            return;
        }

        if (action.equals("register")) {
            String json = DataUtil.readInputStream(req.getInputStream());
            User u = GSON.fromJson(json, User.class);

            User user = UserDAO.createUser(u);

            setAccessControlHeaders(res);
            res.getOutputStream().println(GSON.toJson(user));
            return;
        }

        if (action.equals("auto-login")) {
            String json = DataUtil.readInputStream(req.getInputStream());
            User u = GSON.fromJson(json, User.class);

            User user = UserDAO.getUserByUserId(u.getUserId());

            setAccessControlHeaders(res);
            res.getOutputStream().println(GSON.toJson(user));
            return;
        }

    }
}
