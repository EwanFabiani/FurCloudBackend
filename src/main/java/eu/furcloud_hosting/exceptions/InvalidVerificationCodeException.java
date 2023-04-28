package eu.furcloud_hosting.exceptions;

public class InvalidVerificationCodeException extends Exception{

    public InvalidVerificationCodeException() {
        super("Invalid verification code");
    }

}
