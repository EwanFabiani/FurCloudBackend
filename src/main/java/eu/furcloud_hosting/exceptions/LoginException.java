package eu.furcloud_hosting.exceptions;

import org.springframework.http.HttpStatus;

public class LoginException extends Exception {

    private String message;
    private HttpStatus statusCode;

    public LoginException(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

}
