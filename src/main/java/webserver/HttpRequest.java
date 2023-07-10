package webserver;

import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequest {

    private String method;
    private String path;

    private Map<String, String> header;
    private Map<String, String> params;

    public HttpRequest(InputStream in) throws IOException {
        //request line
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        log.debug("request line : {}", line);

        if (line == null) {
            return;
        }

        String[] tokens = line.split(" ");
        this.method = tokens[0];
        if(this.method.equals("GET")) {
            this.path = tokens[1].substring(0, tokens[1].indexOf("?"));
            this.params = HttpRequestUtils.parseQueryString(tokens[1].substring(tokens[1].indexOf("?") + 1));
            Map<String, String> tmp = new HashMap<>();

            while (!line.equals("")) {
                line = br.readLine();
                if(line == null)
                    break;
                String[] val = line.split(": ");
                tmp.put(val[0], val[1]);

                log.debug("header : {}", line);
            }

            this.header = tmp;

        }else if(this.method.equals("POST")){
            this.path = tokens[1];
            Map<String, String> tmp = new HashMap<>();

            while(!line.equals("")){
                line = br.readLine();
                if(line == null){
                    break;
                }
                if(line.contains(":")) {
                    String[] keyVal = line.split(": ");
                    tmp.put(keyVal[0], keyVal[1]);
                }else{
                    this.params = HttpRequestUtils.parseQueryString(line);
                }

                log.debug("header :{}", line);
            }
            this.header = tmp;

        }


    }

    public String getMethod(){
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getHeader(String fieldName) {

        return this.header.get(fieldName);
    }

    public String getParameter(String fieldName) {
        return this.params.get(fieldName);
    }
}
