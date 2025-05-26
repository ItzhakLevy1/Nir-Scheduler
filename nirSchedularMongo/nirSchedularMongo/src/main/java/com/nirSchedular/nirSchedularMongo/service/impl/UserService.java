package com.nirSchedular.nirSchedularMongo.service.impl;

import com.nirSchedular.nirSchedularMongo.dto.LoginRequest;
import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.dto.UserDTO;
import com.nirSchedular.nirSchedularMongo.entity.User;
import com.nirSchedular.nirSchedularMongo.exception.OurException;
import com.nirSchedular.nirSchedularMongo.repo.UserRepository;
import com.nirSchedular.nirSchedularMongo.service.interfac.IUserService;
import com.nirSchedular.nirSchedularMongo.utils.JWTUtils;
import com.nirSchedular.nirSchedularMongo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service    // This annotation indicates that this class is a service component in the Spring context
public class UserService implements IUserService {

    @Autowired  // This annotation is used for dependency injection
    private UserRepository userRepository;  // This is the repository interface for User entity

    @Autowired
    private PasswordEncoder passwordEncoder;    // This is used for encoding passwords

    @Autowired
    private JWTUtils jwtUtils;  // This is a utility class for handling JWT tokens

    @Autowired
    private AuthenticationManager authenticationManager;    // This is used for authenticating users




    /**
     * Utility method to check if an email is already taken.
     *
     * @param email the email to check.
     * @return true if the email exists in the repository, false otherwise.
     */
    public boolean isEmailTaken(String email) {

        return userRepository.existsByEmail(email);
    }

    // Registers a new user
    @Override
    public Response register(User user) {
        Response response = new Response();

        try {
            // Set default role if not provided
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            // Check if the email (used as ID) already exists
            if (userRepository.existsByEmail(user.getId())) {
                throw new OurException("Email Already Exists");
            }

            // Encrypt the user's password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user); // Save to database

            // Convert entity to DTO for response
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while registering a user :" + e.getMessage());
        }

        return response;
    }

    // Logs in an existing user
    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            // Authenticate email and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // Retrieve user and generate token
            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("User Not Found"));

            var token = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while logging in a user :" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response;

        try {
            // Fetch all users and map them to DTOs
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);

            // Set response
            response = new Response("successful", HttpStatus.OK.value());
            response.setUserList(userDTOList);

        } catch (Exception e) {
            // Handle exception and set error response
            response = new Response("Error while getting all users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response;

        try {
            // Fetch user by ID
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            // Map user to DTO including booking details
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndAppointments(user);

            // Set response
            response = new Response("successful", HttpStatus.OK.value());
            response.setUser(userDTO);

        } catch (OurException e) {
            // Handle custom exception and set error response
            response = new Response(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            // Handle generic exceptions
            response = new Response("Error while getting user booking history: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }


    @Override
    public Response deleteUser(String userId) {
        Response response;

        try {
            // Check if user exists
            userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            // Delete the user
            userRepository.deleteById(userId);

            // Set response
            response = new Response("successful", HttpStatus.OK.value());

        } catch (OurException e) {
            // Handle custom exception and set error response
            response = new Response(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            // Handle generic exceptions
            response = new Response("Error while deleting a user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }


    @Override
    public Response getUserById(String userId) {
        Response response;

        try {
            // Fetch user by ID
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            // Map user to DTO
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            // Set response
            response = new Response("successful", HttpStatus.OK.value());
            response.setUser(userDTO);

        } catch (OurException e) {
            // Handle custom exception and set error response
            response = new Response(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            // Handle generic exceptions
            response = new Response("Error while getting a user by id: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }


    @Override
    public Response getMyInfo(String email) {
        Response response;

        try {
            // Fetch user by email
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));

            // Map user to DTO
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            // Set response
            response = new Response("successful", HttpStatus.OK.value());
            response.setUser(userDTO);

        } catch (OurException e) {
            // Handle custom exception and set error response
            response = new Response(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            // Handle generic exceptions
            response = new Response("Error while getting user info: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

}

/**
 * Summary:
 * The UserService class provides a service layer to manage user operations such as registration, login, fetching user details, and deletion.
 * The utility method isEmailTaken improves code reusability by centralizing the check for email existence.
 * Each method handles specific user-related operations, ensures proper exception handling, and returns a Response object with the result.
 */
