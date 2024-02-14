package com.user.user.Controller;

import com.user.user.DAO.RequestDAO;
import com.user.user.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apps/api/v1/auth/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("user-register/")
    public ResponseEntity<String> userRegister(@RequestBody RequestDAO requestDAO){
        return userService.userRegister(requestDAO);
    }

    @PostMapping("user-forget-password/")
    public ResponseEntity<String> forgetPassword(@RequestBody RequestDAO requestDAO){
        return userService.forgetPassword(requestDAO);
    }

    @PostMapping("user-login/")
    public ResponseEntity<String> userLogin(@RequestBody RequestDAO requestDAO){
        return userService.userLogin(requestDAO);
    }


}
