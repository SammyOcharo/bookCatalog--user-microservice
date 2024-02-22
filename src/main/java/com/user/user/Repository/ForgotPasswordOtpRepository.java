package com.user.user.Repository;

import com.user.user.Entity.ForgotPasswordOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordOtpRepository extends JpaRepository<ForgotPasswordOtp, Long> {

    boolean existsByEmail(String email);

    Optional<ForgotPasswordOtp> findByEmail(String email);
}
