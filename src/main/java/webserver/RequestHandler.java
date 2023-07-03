package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);


    private Socket connection;

    public RequestHandler(Socket connectionSocket) throws IOException {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            // InputStream을 한 줄 단위로 읽기 위해 BufferedReader 생성
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            String url = "";
            int contentLength = 0;


            if (isNull(line)) {
                return;
            }
            String[] token = line.split(" ");

            log.debug("request line :{}", line);



            while (!"".equals(line)) {
                line = br.readLine();
                log.debug("header: {}",line);
                if(line.contains("Content-Length")){
                    contentLength = getContentLength(line);
                }

            }

            url = token[1];

            if("/user/create".equals(url)){
                int index = url.indexOf("?");
                String queryStrings = url.substring(index + 1);
                Map<String, String> params =
                        HttpRequestUtils.parseQueryString(queryStrings);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User :{}", user);

            }else{
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + token[1]).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    public String readBuffer(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        String url = "";
        if (isNull(line)) {
            return "";
        }
        String[] token = line.split(" ");
        if(token[0].equals("GET")){
            url = token[1];
        }

        while (!"".equals(line)) {
            log.debug("header: {}",line);
            line = br.readLine();
        }
        return url;
    }

    private boolean isNull(String line) {
        return line == null;
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

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent) throws IOException{


    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] fileToByte(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    private void setCssAndResponseHeader(DataOutputStream dos, int lengthOfBodyContent) throws IOException{
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void setResponseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
