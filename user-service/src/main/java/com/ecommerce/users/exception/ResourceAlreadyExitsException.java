package com.ecommerce.users.exception;

public class ResourceAlreadyExitsException extends RuntimeException{
    public ResourceAlreadyExitsException(String message) {
        super(message);
    }
}
