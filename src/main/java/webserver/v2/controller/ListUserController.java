package webserver.v2.controller;

import util.HttpRequestUtils;
import webserver.v2.HttpRequest;
import webserver.v2.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class ListUserController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        if(!isLogin(request.getHeaders("Cookie"))){
            response.sendRedirect("/user/login.html");
        }
        response.forward("/list.html");
    }

    private boolean isLogin(String cookieVal) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieVal);
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}
