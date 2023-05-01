package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.LoginException;
import org.springframework.http.HttpStatus;

public class AccountService {

    public String getAccountFromIdentifier(String identifier) throws LoginException {
        try {
            DatabaseAccountService databaseAccountService = new DatabaseAccountService();
            if (identifier.matches(RegexService.EMAIL_REGEX)) {
                if (!databaseAccountService.doesAccountExistEmail(identifier)) {
                    throw new LoginException("Invalid credentials", HttpStatus.UNAUTHORIZED);
                }
                return databaseAccountService.getAccountFromEmail(identifier);
            } else if (identifier.matches(RegexService.USERNAME_REGEX)) {
                if (!databaseAccountService.doesAccountExistUsername(identifier)) {
                    throw new LoginException("Invalid credentials", HttpStatus.UNAUTHORIZED);
                }
                return databaseAccountService.getAccountFromUsername(identifier);
            } else {
                throw new LoginException("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        }catch (DatabaseException e) {
            throw new LoginException("Failed to login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
