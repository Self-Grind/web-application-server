package webserver;

import org.junit.Test;
import webserver.v2.controller.RequestLine;

import static junit.framework.TestCase.assertEquals;

public class RequestLineTest {

    @Test
    public void create_method(){
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
        assertEquals("GET", line.getMethod().toString());
        assertEquals("/index.html", line.getPath());

        line = new RequestLine("POST /index.html HTTP/1.1");
        assertEquals("/index.html", line.getPath());

    }

    @Test
    public void create_param(){
        RequestLine line = new RequestLine("GET /user/create?userId=javajigi&password=pass HTTP/1.1");
        assertEquals("javajigi", line.getParams().get("userId"));
        assertEquals("pass", line.getParams().get("password"));
    }
}
