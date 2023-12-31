package webserver.v2.controller;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.v2.HttpRequest;
import webserver.v2.HttpResponse;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Map;

public class LoginController extends AbstractController {
    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if(user != null) {
            if (user.login(request.getParameter("password"))) {
                response.addHeader("Set-Cookie", "logned=true");
                response.sendRedirect("/index.html");
            } else {
                response.sendRedirect("/user/login_failed.html");
            }
        }
    }
}
