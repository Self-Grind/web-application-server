package webserver.v2.controller;

import webserver.v2.HttpRequest;
import webserver.v2.HttpResponse;

import java.io.IOException;

public interface Controller {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
