package com.user.user.Service.ServiceImpl;

import com.user.user.DAO.RequestDAO;
import com.user.user.Entity.ForgotPasswordOtp;
import com.user.user.Entity.LoginOtp;
import com.user.user.Entity.User;
import com.user.user.Exception.OtpNotFoundException;
import com.user.user.Repository.ForgotPasswordOtpRepository;
import com.user.user.Repository.LoginOtpRepository;
import com.user.user.Repository.UserRepository;
import com.user.user.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

@Service
@SuppressWarnings("unused")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final ForgotPasswordOtpRepository forgotPasswordOtpRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;
    private final LoginOtpRepository loginOtpRepository;

    public UserServiceImpl(
            UserRepository userRepository, EmailService emailService,
            ForgotPasswordOtpRepository forgotPasswordOtpRepository,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager,
            JWTService jwtService, LoginOtpRepository loginOtpRepository){
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordOtpRepository = forgotPasswordOtpRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.loginOtpRepository = loginOtpRepository;
    }
    @Override
    public ResponseEntity<String> userRegister(RequestDAO requestDAO) {
        if(userRepository.existsByEmail(requestDAO.getEmail())){
            return new ResponseEntity<>("User with email "+ requestDAO.getEmail() + " exists", HttpStatus.BAD_REQUEST);
        }
        //create entity instance
        User user = new User();
        LoginOtp loginOtp = new LoginOtp();
        try{
            //set user details
            user.setEmail(requestDAO.getEmail());
            user.setFirstName(requestDAO.getFirstName());
            user.setSecondName(requestDAO.getSecondName());
            user.setRole(requestDAO.getRole());
            //todo remember to encrypt the password before saving.
            user.setPassword(encoder.encode(requestDAO.getPassword()));

            userRepository.save(user);

            //todo write logic for mail to be sent to the user's email
            Random random = new Random();
            Integer otp = random.nextInt(9000) + 1000;


            loginOtp.setEmail(requestDAO.getEmail());
            loginOtp.setOtp(otp);

            loginOtpRepository.save(loginOtp);

            String subject = "Activate account OTP";
            String body = "Use this otp " + otp + " to activate your account";

            emailService.sendEmail(requestDAO.getEmail(), subject,  body);
            return new ResponseEntity<>("User created successfully, check email for account activation code", HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
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

        String subject = "Forgot password OTP";
        String body = "Use this otp " + otp + " to reset your password";

        emailService.sendEmail(requestDAO.getEmail(), subject,  body);


        return ResponseEntity.ok("otp sent to mail" );

    }


    @Override
    public ResponseEntity<RequestDAO> activateAccount(RequestDAO requestDAO) {

        RequestDAO requestDAO1 = new RequestDAO();
        try{
            if(!userRepository.existsByEmail(requestDAO.getEmail())){

                requestDAO1.setResponseMessage("Email does not exist!");
                requestDAO1.setStatusCode(404);

                return new ResponseEntity<>(requestDAO1, HttpStatus.NOT_FOUND);
            }

            Integer otp = requestDAO.getOtp();
            if(otp == null){
                requestDAO1.setResponseMessage("Invalid otp!");
                requestDAO1.setStatusCode(400);
            }
            LoginOtp loginOtp = loginOtpRepository
                    .findByEmailAndIsVerifiedFalse(requestDAO.getEmail())
                    .orElseThrow(NoSuchElementException::new);

            Integer savedOtp = loginOtp.getOtp();
            if(!Objects.equals(savedOtp, otp)){
                requestDAO1.setResponseMessage("otp mismatch!");
                requestDAO1.setStatusCode(400);
            }
            User unactivatedUser = userRepository.findByEmail(requestDAO.getEmail()).orElseThrow();
            unactivatedUser.setActivated(true);
            userRepository.save(unactivatedUser);
            loginOtp.setVerified(true);

            loginOtpRepository.save(loginOtp);

            requestDAO1.setResponseMessage("Account activated");
            requestDAO1.setStatusCode(200);

            return new ResponseEntity<>(requestDAO1, HttpStatus.OK);
        }catch (NoSuchElementException e){

            requestDAO1.setStatusCode(404);
            requestDAO1.setResponseMessage("Otp not found. Check and try again");

            return new ResponseEntity<>(requestDAO1, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){

            requestDAO1.setStatusCode(500);
            requestDAO1.setResponseMessage("An error has occurred");

            return new ResponseEntity<>(requestDAO1, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<RequestDAO> userLogin(RequestDAO requestDAO) {
        RequestDAO requestDAO1 = new RequestDAO();
        try {
            if(!userRepository.existsByEmail(requestDAO.getEmail())){

                requestDAO1.setResponseMessage("Account does not exist!");
                requestDAO1.setStatusCode(404);

                return new ResponseEntity<>(requestDAO1, HttpStatus.NOT_FOUND);
            }
            User user = userRepository.findByEmail(requestDAO.getEmail()).orElseThrow();
            if(!user.isEnabled()){
                requestDAO1.setResponseMessage("Account not activated");
                requestDAO1.setStatusCode(400);

                return new ResponseEntity<>(requestDAO1, HttpStatus.BAD_REQUEST);
            }


            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDAO.getEmail(),requestDAO.getPassword()));
            String token = jwtService.generateToken(user);

           requestDAO1.setToken(token);
           requestDAO1.setStatusCode(200);
           requestDAO1.setResponseMessage("Login successful");

           return new ResponseEntity<>(requestDAO1, HttpStatus.OK);

        }catch (Exception e){

            requestDAO1.setStatusCode(400);
            requestDAO1.setResponseMessage("Login not successful");
            return new ResponseEntity<>(requestDAO1, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<String> confirmOtp(RequestDAO requestDAO) {
        if(!forgotPasswordOtpRepository.existsByEmail(requestDAO.getEmail())){
            return new ResponseEntity<>("email does not exist", HttpStatus.NOT_FOUND);
        }

        try{
            ForgotPasswordOtp forgotPasswordOtp = forgotPasswordOtpRepository
                    .findByEmailAndIsVerifiedFalse(requestDAO.getEmail())
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
            return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> changePassword(RequestDAO requestDAO) {

        if(requestDAO.getEmail() == null || requestDAO.getPassword()==null){
            return new ResponseEntity<>("Email/password field is null", HttpStatus.BAD_REQUEST);
        }
        if(!userRepository.existsByEmail(requestDAO.getEmail())){
            return new ResponseEntity<>("User with email "+ requestDAO.getEmail() + " exists", HttpStatus.BAD_REQUEST);
        }
        try{
            User user = userRepository.findByEmail(requestDAO.getEmail()).orElseThrow();
            user.setPassword(encoder.encode(requestDAO.getPassword()));

            userRepository.save(user);

            return new ResponseEntity<>("Password set successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> logout(RequestDAO requestDAO) {

        try{
            //todo add the authentication logout logic

            return ResponseEntity.ok("Successfully logged out");
        } catch (Exception e){
            throw e;
        }
    }

}
