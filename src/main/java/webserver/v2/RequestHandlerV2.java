package webserver.v2;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandlerV1;
import webserver.v2.controller.Controller;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandlerV2 extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandlerV1.class);


    private Socket connection;

    public RequestHandlerV2(Socket connection) {
        this.connection = connection;
    }

    //p.170
    public void run() {
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
        ) {
            HttpRequest req = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            String path = getDefaultPath(req.getPath());

            Controller controller = RequestMapping.getController(path);

            if (controller == null) {
                String defaultPath = getDefaultPath(path);
                response.forward(defaultPath);
            } else {
                controller.service(req, response);
            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void listUser(HttpRequest request, HttpResponse response) {
        if (!isLogin(request.getHeaders("Cookie"))) {
//            responseResource(out, "/user/login.html");
            return;
        }
    }

    private void login(HttpRequest request, HttpResponse response) {
        try {
            User user = DataBase.findUserById(request.getParameter("userId"));
            if (user != null) {
                if (user.login(request.getParameter("password"))) {
                    response.addHeader("Set-Cookie", "logined=true");
                    response.sendRedirect("/index.html");
                }
            } else {
                response.sendRedirect("/user/login_failed.html");
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }

    }

    private void createUser(HttpRequest req, HttpResponse response) {

        try {
            User user = new User(
                    req.getParameter("userId"),
                    req.getParameter("password"),
                    req.getParameter("name"),
                    req.getParameter("email"));
            DataBase.addUser(user);
            response.sendRedirect("/index.html");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void responseCssReource(OutputStream out, String path) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        responseCssHeader(dos, body.length);
        responseBody(dos, body);
    }

    private void responseCssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/l.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isLogin(String cookieVal) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieVal);
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }
}
