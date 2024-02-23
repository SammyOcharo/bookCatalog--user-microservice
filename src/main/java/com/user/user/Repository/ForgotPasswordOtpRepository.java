package com.user.user.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import com.user.user.Entity.ForgotPasswordOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordOtpRepository extends JpaRepository<ForgotPasswordOtp, Long> {

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM forgot_password_otp WHERE email = :email AND is_verified = false ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<ForgotPasswordOtp> findByEmailAndIsVerifiedFalse(@Param("email") String email);
}
