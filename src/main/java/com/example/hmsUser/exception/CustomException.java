package com.example.hmsUser.exception;

public class CustomException {

    public static class AppointmentConflictException extends RuntimeException {
        public AppointmentConflictException(String message) {
            super(message);
        }
    }

    public static class AppointmentNotFoundException extends RuntimeException {
        public AppointmentNotFoundException(String message) {
            super(message);
        }
    }

}

