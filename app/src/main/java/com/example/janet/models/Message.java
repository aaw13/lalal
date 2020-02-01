package com.example.janet.models;

public class Message {
    MessageTypesEnum sender;
    String message;
    public Message(String message, MessageTypesEnum sender)
    {
        this.message = message;
        this.sender  = sender;
    }
    public MessageTypesEnum getSender() {
        return sender;
    }
    public String getMessage()
    {
        return message;
    }
}
