package webserver.v2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.v2.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class RequestLine {
    private HttpMethod method;

    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);

    private String path;
    private Map<String, String> params = new HashMap<>();

    public RequestLine(String requestline) {
        log.debug("request line : {}", requestline);
        String[] tokens = requestline.split(" ");
        method = HttpMethod.valueOf(tokens[0]);

        if(method == HttpMethod.POST){
            path = tokens[1];
            return;
        }

        int index = tokens[1].indexOf("?");
        if (index == -1) {
            path = tokens[1];
        } else {
            path = tokens[1].substring(0, index);
            params = HttpRequestUtils.parseQueryString(
                    tokens[1].substring(index + 1));
        }

    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return this.path;
    }


    public Map<String, String> getParams() {
        return this.params;
    }
}
