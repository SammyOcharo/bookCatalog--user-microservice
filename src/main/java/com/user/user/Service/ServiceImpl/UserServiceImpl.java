package com.user.user.Service.ServiceImpl;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.User;
import com.user.user.Exception.UserDoesNotExistException;
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
        }catch (UserDoesNotExistException e){
            throw  e;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> forgetPassword(RequestDAO requestDAO) {
        //todo write logic for mail to be sent to the user's email
        return ResponseEntity.ok("otp sent to mail");
    }

    @Override
    public ResponseEntity<String> userLogin(RequestDAO requestDAO) {
        try{
            //todo login logic
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } catch (Exception e){
            throw e;
        }
    }
}
