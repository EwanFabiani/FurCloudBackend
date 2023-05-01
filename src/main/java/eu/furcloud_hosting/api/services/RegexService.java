package eu.furcloud_hosting.api.services;

public class RegexService {

    public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{4,24}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,256}$";
    public static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$";

}
