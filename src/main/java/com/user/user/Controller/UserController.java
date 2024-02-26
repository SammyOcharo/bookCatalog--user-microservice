package com.user.user.Controller;

import com.user.user.DAO.RequestDAO;
import com.user.user.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("unused")
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

    @PostMapping("activate-account/")
    public ResponseEntity<RequestDAO> activateAccount(@RequestBody RequestDAO requestDAO){

        return userService.activateAccount(requestDAO);
    }


    @PostMapping("user-login/")
    public ResponseEntity<RequestDAO> userLogin(@RequestBody RequestDAO requestDAO){
        return userService.userLogin(requestDAO);
    }

    @PostMapping("user-forget-password/")
    public ResponseEntity<String> forgetPassword(@RequestBody RequestDAO requestDAO){
        return userService.forgetPassword(requestDAO);
    }
    @PostMapping("user-forget-password/confirm-otp/")
    public ResponseEntity<String> confirmOtp(@RequestBody RequestDAO requestDAO){

        return userService.confirmOtp(requestDAO);
    }

    @PostMapping("user-forget-password/change-password/")
    public ResponseEntity<String> changePassword(@RequestBody RequestDAO requestDAO){

        return userService.changePassword(requestDAO);
    }

    @PostMapping("logout/")
    public ResponseEntity<String> logout(@RequestBody RequestDAO requestDAO){

        return userService.logout(requestDAO);
    }

}
