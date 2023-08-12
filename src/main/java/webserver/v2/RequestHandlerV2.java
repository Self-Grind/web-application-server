package webserver.v2;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandlerV1;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandlerV2 extends Thread{
    private static final Logger log = LoggerFactory.getLogger(RequestHandlerV1.class);


    private Socket connection;

    public RequestHandlerV2(Socket connection) {
        this.connection = connection;
    }

    public void run(){
        try(InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();
        ) {
            HttpRequest req = new HttpRequest(in);
            String path = getDefaultPath(req.getPath());

            if("/user/create".equals(path)){
                User user = new User(
                        req.getParameter("userId"),
                        req.getParameter("password"),
                        req.getParameter("name"),
                        req.getParameter("email"));
                DataBase.addUser(user);
            }else if("/user/login".equals(path)){
                User user = DataBase.findUserById(req.getParameter("userId"));

            }else if("user/list".equals(path)){
                if(!isLogin(req.getHeaders("Cookie"))){
                    responseResource(out, "/user/login.html");
                    return;
                }
            }else if(path.endsWith(".css")){
                responseCssReource(out, path);
            }else{
                responseResource(out, path);
            }
        } catch (IOException e) {
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


    private void responseResource(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Set-Cookie: logined=true \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response300Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 REDIRECT \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
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

    private boolean isLogin(String cookieVal){
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieVal);
        String value = cookies.get("logined");
        if(value == null){
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    private String getDefaultPath(String path) {
        if(path.equals("/")){
            return "/index.html";
        }
        return path;
    }
}
