package webserver.v2;

import webserver.v2.controller.Controller;
import webserver.v2.controller.CreateUserController;
import webserver.v2.controller.ListUserController;
import webserver.v2.controller.LoginController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new ListUserController());
    }

    public static Controller getController(String requestMapping) {
        return controllers.get(requestMapping);
    }
}
