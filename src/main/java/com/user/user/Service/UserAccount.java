package com.user.user.Service;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.User;
import org.springframework.http.ResponseEntity;

public interface UserAccount {
    ResponseEntity<RequestDAO> userAccountDetails(Long id);
}
