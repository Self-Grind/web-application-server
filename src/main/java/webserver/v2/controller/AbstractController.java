package webserver.v2.controller;

import webserver.v2.HttpMethod;
import webserver.v2.HttpRequest;
import webserver.v2.HttpResponse;

import java.io.IOException;

public class AbstractController implements Controller{
    /**
     *
     * @param request
     * @param response
     */
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        HttpMethod method = request.getRequestLine().getMethod();

        if(method.equals(HttpMethod.GET)){
            this.doGet(request, response);
        } else if (method.equals(HttpMethod.POST)) {
            this.doPost(request, response);
        }
    }

    protected void doGet(HttpRequest request, HttpResponse response) throws IOException {

    }

    protected void doPost(HttpRequest request, HttpResponse response) throws IOException {

    }
}
