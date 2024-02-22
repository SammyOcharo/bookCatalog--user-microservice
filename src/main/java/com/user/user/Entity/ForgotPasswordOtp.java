package com.user.user.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "forgotPassword_otp")
public class ForgotPasswordOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private Integer otp;
    private boolean isVerified=false;


    public ForgotPasswordOtp() {
    }

    public ForgotPasswordOtp(Long id, String email, Integer otp, boolean isVerified) {
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

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
