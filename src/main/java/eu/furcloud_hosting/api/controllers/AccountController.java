package eu.furcloud_hosting.api.controllers;

import eu.furcloud_hosting.api.models.AccountCreationModel;
import eu.furcloud_hosting.api.models.LoginModel;
import eu.furcloud_hosting.api.services.*;
import eu.furcloud_hosting.api.services.database.DataSyncService;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.api.services.database.DatabasePasswordService;
import eu.furcloud_hosting.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountCreationModel accountCreationModel) {
        String username = accountCreationModel.getUsername();
        String email = accountCreationModel.getEmail();
        String password = accountCreationModel.getPassword();
        try {
            if (DataSyncService.isNull(username, email, password)) {
                throw new RegisterException("Missing required fields", HttpStatus.BAD_REQUEST);
            }
            try {
                DatabaseAccountService databaseAccountService = new DatabaseAccountService();
                DatabasePasswordService databasePasswordService = new DatabasePasswordService();
                DataSyncService.beginTransactions(databaseAccountService, databasePasswordService);

                if (databaseAccountService.doesAccountExistEmail(email)) {
                    throw new RegisterException("Email already in use by another account", HttpStatus.CONFLICT);
                }
                if (databaseAccountService.doesAccountExistUsername(username)) {
                    throw new RegisterException("Username already taken", HttpStatus.CONFLICT);
                }
                String accountId = databaseAccountService.createAccount(username, email);

                databasePasswordService.saveCredentials(accountId, password);

                VerificationService verificationService = new VerificationService();
                String verificationCode = verificationService.createVerificationCode(accountId);
                verificationService.sendVerificationEmail(email, accountId, verificationCode);

                DataSyncService.commitTransactions(databaseAccountService, databasePasswordService);

                String response = JSONService.createJsonSuccess("Account created successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (DatabaseException | SQLException | PasswordHashingException e) {
                String error = JSONService.createJsonError("Failed to create account");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }catch (RegisterException e) {
            String error = JSONService.createJsonError(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    @PostMapping("/verify/{code}")
    public ResponseEntity<String> verifyAccount(@PathVariable String code) {
        try {
            VerificationService verificationService = new VerificationService();
            verificationService.verifyAccount(code);
            String response = JSONService.createJsonSuccess("Account verified successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DatabaseException e) {
            String error = JSONService.createJsonError("Failed to verify account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (InvalidVerificationCodeException e) {
            String error = JSONService.createJsonError("Invalid verification code");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginModel loginModel) {
        String identifier = loginModel.getIdentifier();
        String password = loginModel.getPassword();
        try {
            if (DataSyncService.isNull(identifier, password)) {
                throw new LoginException("Missing required fields", HttpStatus.BAD_REQUEST);
            }

            AccountService accountService = new AccountService();
            String accountId = accountService.getAccountFromIdentifier(identifier);
            SecurityService securityService = new SecurityService();

            if (!securityService.verifyCredentials(accountId, password)) {
                throw new LoginException("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
            String response = JSONService.createJsonSuccess("Login successful");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (LoginException e) {
            String error = JSONService.createJsonError(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }
}