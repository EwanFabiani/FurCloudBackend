package eu.furcloud_hosting.api.services;

public class RegexService {

    public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{4,24}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,256}$";
    public static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$";

    public static boolean isValidUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

}
