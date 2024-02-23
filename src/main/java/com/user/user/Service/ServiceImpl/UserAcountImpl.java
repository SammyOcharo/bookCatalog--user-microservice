package com.user.user.Service.ServiceImpl;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.User;
import com.user.user.Repository.UserRepository;
import com.user.user.Service.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAcountImpl implements UserAccount {

    private final UserRepository userRepository;

    public UserAcountImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ResponseEntity<RequestDAO> userAcoountDetails(Long id) {

        RequestDAO requestDAO = new RequestDAO();

        if(id == null) {
            throw new NullPointerException("ID is null");
        }
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User does not exist."));

            requestDAO.setEmail(user.getEmail());
            requestDAO.setStatusCode(200);
            requestDAO.setFirstName(user.getFirstName());
            requestDAO.setSecondName(user.getSecondName());
            requestDAO.setRole(user.getRole());
            requestDAO.setUsername(user.getUsername());

            return new ResponseEntity<>(requestDAO, HttpStatus.OK);
        } catch (UsernameNotFoundException | NullPointerException e) {
            requestDAO.setResponseMessage(e.getMessage());
            requestDAO.setStatusCode(404);

            requestDAO.setEmail(null);
            requestDAO.setFirstName(null);
            requestDAO.setSecondName(null);
            requestDAO.setRole(null);
            requestDAO.setUsername(null);
            return new ResponseEntity<>(requestDAO, HttpStatus.NOT_FOUND);
        }
    }
}
