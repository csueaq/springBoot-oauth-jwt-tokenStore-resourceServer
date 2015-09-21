package resourceserver.controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import resourceserver.data.pojo.User;

import java.util.concurrent.Callable;

/**
 * Created by Izzy on 17/08/15.
 */
@RestController
public class UserController {

    static final String USER_URL_PATH = "/user";

    @RequestMapping(value = USER_URL_PATH + "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<User> test () {
        return () -> {
            User testUser = new User();
            testUser.setAge(100);
            testUser.setEmail("test@test.com");
            return testUser;
        };
    }

}
