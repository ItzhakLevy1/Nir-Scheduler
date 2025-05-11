package com.nirSchedular.nirSchedularMongo.repo;

import com.nirSchedular.nirSchedularMongo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);   // Checks if a user with the given email exists in the database, useful for validating registrations or preventing duplicate accounts

    Optional<User> findByEmail(String email);   // Finds a user by their email address, returning an Optional<User> to handle the case where the user may not exist, used in login, authentication, and user lookup operations
}

// No need to create an implementation class for this interface, as Spring Data MongoDB will automatically provide the implementation at runtime based on the method signatures defined in the interface.