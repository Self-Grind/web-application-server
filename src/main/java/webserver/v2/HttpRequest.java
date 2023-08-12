package webserver.v2;

import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.v2.controller.RequestLine;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequest {

    private RequestLine requestLine;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();

    public HttpRequest(InputStream in) throws IOException {
        //request line
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        log.debug("request line : {}", line);

        if (line == null) {
            return;
        }

        requestLine = new RequestLine(line);
        line = br.readLine();


        while (!line.equals("")) {
            log.debug("header : {}", line);
            String[] tokens = line.split(":");
            headers.put(tokens[0].trim(), tokens[1].trim());
            line = br.readLine();

        }
        if (requestLine.getMethod().equals(HttpMethod.POST)) {
            String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            params = HttpRequestUtils.parseQueryString(body);
        }else{
            params = requestLine.getParams();
        }

    }

//    public RequestLine getRequestLine(){
//        if(method == HttpMethod.POST)
//        return this.requestLine;
//    }

    public String getHeaders(String key){
        return this.headers.get("key");
    }


    public RequestLine getRequestLine() {
        return requestLine;
    }
}
