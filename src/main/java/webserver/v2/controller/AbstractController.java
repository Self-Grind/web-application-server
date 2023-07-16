package webserver.v2.controller;

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
        String method = request.getMethod();

        if(method.equals("GET")){
            this.doGet(request, response);
        } else if (method.equals("POST")) {
            this.doPost(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.forward(request.getPath());
    }

    public void doPost(HttpRequest request, HttpResponse response){

    }
}
