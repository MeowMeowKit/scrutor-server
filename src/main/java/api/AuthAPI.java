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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo().substring(1);

        if (action.equals("login")) {
            String json = DataUtil.readInputStream(request.getInputStream());
            User u = GSON.fromJson(json, User.class);

            User user = UserDAO.getUserByEmailAndPassword(u.getEmail(), u.getPassword());

            response.setStatus(200);
            response.setHeader("Content-Type", "application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(GSON.toJson(user));
        } else if (action.equals("register")) {
            String json = DataUtil.readInputStream(request.getInputStream());
            User u = GSON.fromJson(json, User.class);

            User user = UserDAO.createUser(u);

            response.setStatus(200);
            response.setHeader("Content-Type", "application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(GSON.toJson(user));
        }
    }
}
