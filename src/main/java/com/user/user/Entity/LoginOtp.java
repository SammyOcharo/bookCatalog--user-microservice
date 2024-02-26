package com.user.user.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "LOGIN_OTP")
public class LoginOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private Integer otp;

    private boolean isVerified=false;


    public LoginOtp() {
    }

    public LoginOtp(Long id,
                    String email,
                    Integer otp,
                    boolean isVerified) {
        this.id = id;
        this.email = email;
        this.otp = otp;
        this.isVerified = isVerified;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getOtp() {
        return otp;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
