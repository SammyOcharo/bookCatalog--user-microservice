package com.user.user.Repository;

import com.user.user.Entity.ForgotPasswordOtp;
import com.user.user.Entity.LoginOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginOtpRepository extends JpaRepository<LoginOtp, Long> {
    Optional<LoginOtp> findByEmail(String email);
    @Query(value = "SELECT * FROM login_otp WHERE email = :email AND is_verified = false ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<LoginOtp> findByEmailAndIsVerifiedFalse(@Param("email") String email);
}
