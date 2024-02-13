package com.user.user.Service.ServiceImpl;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.User;
import com.user.user.Repository.UserRepository;
import com.user.user.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public ResponseEntity<String> userRegister(RequestDAO requestDAO) {
        User user = new User();
        try{
            user.setEmail(requestDAO.getEmail());
            user.setFirstName(requestDAO.getFirstName());
            user.setSecondName(requestDAO.getSecondName());
            user.setRole(requestDAO.getRole());
            //todo remember to encrypt the password before saving.
            user.setPassword(requestDAO.getPassword());

            userRepository.save(user);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> forgetPassword(RequestDAO requestDAO) {
        return null;
    }
}
