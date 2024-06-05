package com.morris.opensquare.repositories;

import com.morris.opensquare.models.security.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDetails, String> {
    Optional<UserDetails> findByUsername(String username);
}
