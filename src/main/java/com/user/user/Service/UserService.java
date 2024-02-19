package com.user.user.Service;

import com.user.user.DAO.RequestDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseEntity<String> userRegister(RequestDAO requestDAO);

    ResponseEntity<String> forgetPassword(RequestDAO requestDAO);

    ResponseEntity<RequestDAO> userLogin(RequestDAO requestDAO);
}
