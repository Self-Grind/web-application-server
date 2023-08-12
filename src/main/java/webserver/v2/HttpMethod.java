package webserver.v2;

public enum HttpMethod {
    GET,
    POST;

    public boolean isPost(){
        return this == POST;
    }
}
