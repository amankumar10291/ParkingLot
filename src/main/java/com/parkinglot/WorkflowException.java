package com.parkinglot;

public class WorkflowException extends RuntimeException {

    private int status;
    private String message;


    public WorkflowException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

}
