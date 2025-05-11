package com.nirSchedular.nirSchedularMongo.dto;

import lombok.Data;

@Data   // Lombok will generate getters, setters, toString, equals, and hashCode methods
public class LoginRequest {

    private String email;   // Email address of the user
    private String password;   // Password of the user

}

/* The LoginRequest DTO is used to capture and transfer login credentials (email and password) from the client (usually via a POST request) to the backend authentication logic */