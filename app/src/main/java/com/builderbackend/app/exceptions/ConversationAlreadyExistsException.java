package com.builderbackend.app.exceptions;

public class ConversationAlreadyExistsException extends Exception{
    
    public ConversationAlreadyExistsException(String message) {
        super(message);
    }
}