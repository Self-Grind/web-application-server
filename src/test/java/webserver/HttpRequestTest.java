package webserver;

import org.junit.Test;
import webserver.v2.HttpRequest;
import webserver.v2.controller.RequestLine;

import java.io.*;
import java.nio.file.Files;
import static junit.framework.TestCase.assertEquals;

public class HttpRequestTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception{
        InputStream in = Files.newInputStream(new File(testDirectory + "Http_GET.txt").toPath());
        HttpRequest request = new HttpRequest(in);
        

        //v1
        assertEquals("GET", request.getRequestLine().getMethod());
        assertEquals("/user/create", request.getRequestLine().getPath());
        assertEquals("keep-alive", request.getHeaders("Connection"));
        assertEquals("javajigi", request.getRequestLine().getParams().get("userId"));
        
    }

    @Test
    public void request_POST() throws Exception{
        InputStream in = Files.newInputStream(new File(testDirectory + "Http_POST.txt").toPath());
        HttpRequest request = new HttpRequest(in);

        //v1
//        assertEquals("POST", request.getRequestLine().getMethod());
//        assertEquals("/user/create", request.getPath());
//        assertEquals("keep-alive", request.getHeader("Connection"));
//        assertEquals("javajigi", request.getParameter("userId"));
    }


}
