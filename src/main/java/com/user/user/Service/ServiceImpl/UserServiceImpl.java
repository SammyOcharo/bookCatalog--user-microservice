package com.user.user.Service.ServiceImpl;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.ForgotPasswordOtp;
import com.user.user.Entity.User;
import com.user.user.Exception.UserDoesNotExistException;
import com.user.user.Exception.UserExistsException;
import com.user.user.Repository.ForgotPasswordOtpRepository;
import com.user.user.Repository.UserRepository;
import com.user.user.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ForgotPasswordOtpRepository forgotPasswordOtpRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public UserServiceImpl(
            UserRepository userRepository,
            ForgotPasswordOtpRepository forgotPasswordOtpRepository,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager, JWTService jwtService){
        this.userRepository = userRepository;
        this.forgotPasswordOtpRepository = forgotPasswordOtpRepository;
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
        if(!userRepository.existsByEmail(requestDAO.getEmail())){
            return new ResponseEntity<>("user with email provided does not exist", HttpStatus.NOT_FOUND);
        }

        ForgotPasswordOtp forgotPasswordOtp = new ForgotPasswordOtp();

        //todo write logic for mail to be sent to the user's email
        Random random = new Random();
        Integer otp = random.nextInt(9000) + 1000;


        forgotPasswordOtp.setEmail(requestDAO.getEmail());
        forgotPasswordOtp.setOtp(otp);

        forgotPasswordOtpRepository.save(forgotPasswordOtp);


        return ResponseEntity.ok("otp sent to mail " + otp);

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

    @Override
    public ResponseEntity<String> confirmOtp(RequestDAO requestDAO) {
        if(!forgotPasswordOtpRepository.existsByEmail(requestDAO.getEmail())){
            return new ResponseEntity<>("email does not exist", HttpStatus.NOT_FOUND);
        }

        try{
            ForgotPasswordOtp forgotPasswordOtp = forgotPasswordOtpRepository
                    .findByEmail(requestDAO.getEmail())
                    .stream()
                    .reduce((first, second)-> second)
                    .orElseThrow();

            Integer otp = forgotPasswordOtp.getOtp();

            if (!Objects.equals(otp, requestDAO.getOtp())) {
                return new ResponseEntity<>("Otp does not Match", HttpStatus.BAD_REQUEST);
            }else {
                forgotPasswordOtp.setVerified(Boolean.TRUE);
                forgotPasswordOtpRepository.save(forgotPasswordOtp);
                return new ResponseEntity<>("Otp Verified.", HttpStatus.OK);
            }

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> changePassword(RequestDAO requestDAO) {
        if(!userRepository.existsByEmail(requestDAO.getEmail())){
            return new ResponseEntity<>("User with email "+ requestDAO.getEmail() + " exists", HttpStatus.BAD_REQUEST);
        }
        try{
            User user = userRepository.findByEmail(requestDAO.getEmail()).orElseThrow();
            user.setPassword(encoder.encode(requestDAO.getPassword()));

            userRepository.save(user);

            return new ResponseEntity<>("Password set successfully", HttpStatus.OK);
        }catch (Exception e){
            throw  e;
        }
    }
}
