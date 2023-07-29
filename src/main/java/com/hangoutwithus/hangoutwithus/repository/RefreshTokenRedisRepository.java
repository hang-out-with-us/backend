package com.hangoutwithus.hangoutwithus.repository;

import com.hangoutwithus.hangoutwithus.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {
    void deleteRefreshTokenByEmail(String email);

    Optional<RefreshToken> findRefreshTokenByEmail(String email);
}
