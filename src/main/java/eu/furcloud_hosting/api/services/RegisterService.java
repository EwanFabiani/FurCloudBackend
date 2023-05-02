package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.services.database.DataSyncService;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.exceptions.AccountNotFoundException;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.LoginException;
import org.springframework.http.HttpStatus;

public class RegisterService {

    public static String createAccount(String username, String password, String email) throws LoginException {
        if (DataSyncService.isNull(identifier, password)) {
            throw new LoginException("Missing required fields", HttpStatus.BAD_REQUEST);
        }
        try {
            DatabaseAccountService databaseAccountService = new DatabaseAccountService();
            String accountId = databaseAccountService.getAccountFromIdentifier(identifier);
            if (!SecurityService.verifyCredentials(accountId, password)) {
                throw new LoginException("Incorrect password", HttpStatus.UNAUTHORIZED);
            }
            return accountId;
        } catch (DatabaseException e) {
            throw new LoginException("Failed to authenticate user", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccountNotFoundException e) {
            throw new LoginException("Invalid username or email", HttpStatus.NOT_FOUND);
        }
    }

}
