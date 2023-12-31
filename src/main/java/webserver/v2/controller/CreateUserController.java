package webserver.v2.controller;

import db.DataBase;
import model.User;
import webserver.v2.HttpRequest;
import webserver.v2.HttpResponse;
import webserver.v2.controller.Controller;

import java.io.IOException;

/**
 * RequestHandlerV2를 받아줄 Controller 구현체
 */
public class CreateUserController extends AbstractController {

    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
