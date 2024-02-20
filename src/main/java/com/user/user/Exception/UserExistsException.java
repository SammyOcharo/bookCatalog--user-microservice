package com.user.user.Exception;

public class UserExistsException extends RuntimeException{
    public UserExistsException(String message) {
        super(message);
    }
}
