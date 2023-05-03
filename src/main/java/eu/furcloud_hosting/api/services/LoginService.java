package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.models.AccountStatus;
import eu.furcloud_hosting.api.services.database.DataSyncService;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.exceptions.AccountNotFoundException;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.LoginException;
import org.springframework.http.HttpStatus;

public class LoginService {

    public String authenticateUser(String identifier, String password) throws LoginException {
        if (DataSyncService.isNull(identifier, password)) {
            throw new LoginException("Missing required fields.", HttpStatus.BAD_REQUEST);
        }
        try {
            DatabaseAccountService databaseAccountService = new DatabaseAccountService();
            String accountId = databaseAccountService.getAccountIdFromIdentifier(identifier);
            if (!SecurityService.verifyCredentials(accountId, password)) {
                throw new LoginException("The credentials you provided are invalid.", HttpStatus.UNAUTHORIZED);
            }
            throwTextStatusExceptions(databaseAccountService, accountId);
            return accountId;
        } catch (DatabaseException e) {
            throw new LoginException("There was an error while attempting to authenticate your account.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccountNotFoundException e) {
            throw new LoginException("Invalid username or email.", HttpStatus.NOT_FOUND);
        }
    }

    public void throwTextStatusExceptions(DatabaseAccountService databaseAccountService, String accountId) throws LoginException, DatabaseException {
        AccountStatus accountStatus = databaseAccountService.getAccountStatus(accountId);
        if (accountStatus == AccountStatus.BANNED) {
            throw new LoginException("Your account has been banned.", HttpStatus.UNAUTHORIZED);
        }
        if (accountStatus == AccountStatus.UNVERIFIED) {
            throw new LoginException("Your account has not been verified yet.", HttpStatus.UNAUTHORIZED);
        }
        if (accountStatus == AccountStatus.DELETED) {
            throw new LoginException("This account has been deleted.", HttpStatus.UNAUTHORIZED);
        }
        if (accountStatus == AccountStatus.SUSPENDED) {
            throw new LoginException("Your account has been suspended and is currently under review.", HttpStatus.UNAUTHORIZED);
        }
    }

}
