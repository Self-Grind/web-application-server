package webserver;

import org.junit.Test;
import webserver.v2.HttpRequest;

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
//        assertEquals("GET", request.getMethod());
//        assertEquals("/user/create", request.getPath());
//        assertEquals("keep-alive", request.getHeader("Connection"));
//        assertEquals("javajigi", request.getParameter("userId"));
        
    }

    @Test
    public void request_POST() throws Exception{
        InputStream in = Files.newInputStream(new File(testDirectory + "Http_POST.txt").toPath());
        HttpRequest request = new HttpRequest(in);

        //v1
//        assertEquals("POST", request.getMethod());
//        assertEquals("/user/create", request.getPath());
//        assertEquals("keep-alive", request.getHeader("Connection"));
//        assertEquals("javajigi", request.getParameter("userId"));
    }


}
