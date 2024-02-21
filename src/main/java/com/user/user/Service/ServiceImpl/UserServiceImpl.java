package com.user.user.Service.ServiceImpl;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.User;
import com.user.user.Exception.UserDoesNotExistException;
import com.user.user.Exception.UserExistsException;
import com.user.user.Repository.UserRepository;
import com.user.user.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager, JWTService jwtService){
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    @Override
    public ResponseEntity<String> userRegister(RequestDAO requestDAO) {
        if(userRepository.existsByEmail(requestDAO.getEmail())){
            return new ResponseEntity<>("User with email "+ requestDAO.getEmail() + " exists", HttpStatus.BAD_REQUEST);
        }
        //create entity instance
        User user = new User();
        try{
            //set user details
            user.setEmail(requestDAO.getEmail());
            user.setFirstName(requestDAO.getFirstName());
            user.setSecondName(requestDAO.getSecondName());
            user.setRole(requestDAO.getRole());
            //todo remember to encrypt the password before saving.
            user.setPassword(encoder.encode(requestDAO.getPassword()));

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
    public ResponseEntity<RequestDAO> userLogin(RequestDAO requestDAO) {
        RequestDAO requestDAO1 = new RequestDAO();
        System.out.println("we are here");
        System.out.println(requestDAO.getEmail());
        System.out.println(requestDAO.getPassword());

//        try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDAO.getEmail(),requestDAO.getPassword()));
        System.out.println("we are here");
        User user = userRepository.findByEmail(requestDAO.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
               

       requestDAO1.setToken(token);
       requestDAO1.setStatusCode(200);
       requestDAO1.setResponseMessage("Login successful");

       return new ResponseEntity<>(requestDAO1, HttpStatus.OK);


//        }catch (Exception e){
//
//            requestDAO1.setStatusCode(400);
//            requestDAO1.setResponseMessage("Login not successful");
//            return new ResponseEntity<>(requestDAO1, HttpStatus.BAD_REQUEST);
//        }

    }


}
