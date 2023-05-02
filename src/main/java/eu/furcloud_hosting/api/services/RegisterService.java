package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.services.database.DataSyncService;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.api.services.database.DatabasePasswordService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.PasswordHashingException;
import eu.furcloud_hosting.exceptions.RegisterException;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;

public class RegisterService {

    public void createAccount(String username, String email, String password) throws RegisterException {
        try {
            doArgumentsMeetRequirements(username, email, password);
            DatabaseAccountService databaseAccountService = new DatabaseAccountService();
            DatabasePasswordService databasePasswordService = new DatabasePasswordService();
            DataSyncService.beginTransactions(databaseAccountService, databasePasswordService);
            String accountId = databaseAccountService.createAccount(username, email);
            databasePasswordService.saveCredentials(accountId, password);
            DataSyncService.commitTransactions(databaseAccountService, databasePasswordService);
        } catch (DatabaseException | SQLException | PasswordHashingException e) {
            throw new RegisterException("Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void doArgumentsMeetRequirements(String username, String email, String password) throws RegisterException {
        if (DataSyncService.isNull(username, email, password)) {
            throw new RegisterException("Missing required fields", HttpStatus.BAD_REQUEST);
        }
        if (!RegexService.isValidUsername(username)) {
            throw new RegisterException("Username does not meet requirements", HttpStatus.BAD_REQUEST);
        }
        if (!RegexService.isValidEmail(email)) {
            throw new RegisterException("Invalid email", HttpStatus.BAD_REQUEST);
        }
        if (!RegexService.isValidPassword(password)) {
            throw new RegisterException("Password does not meet requirements", HttpStatus.BAD_REQUEST);
        }
    }

}
