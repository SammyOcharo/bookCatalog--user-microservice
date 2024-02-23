package com.user.user.Controller;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.User;
import com.user.user.Service.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/apps/api/v1/auth/account")
public class UserAccountController {

    private final UserAccount userAccount;

    public UserAccountController(UserAccount userAccount){
        this.userAccount = userAccount;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDAO> userAcoountDetails(@PathVariable Long id){
        return userAccount.userAcoountDetails(id);
    }

}
