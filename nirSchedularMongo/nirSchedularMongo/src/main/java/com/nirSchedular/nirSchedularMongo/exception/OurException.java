package com.nirSchedular.nirSchedularMongo.exception;

public class OurException extends RuntimeException{

    // Constructor that takes a message and passes it to the superclass constructor
    public OurException(String message) {
        super(message);
    }
}
