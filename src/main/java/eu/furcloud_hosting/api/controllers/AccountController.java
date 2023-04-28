package eu.furcloud_hosting.api.controllers;

import eu.furcloud_hosting.api.models.AccountCreationModel;
import eu.furcloud_hosting.api.services.JSONService;
import eu.furcloud_hosting.api.services.VerificationService;
import eu.furcloud_hosting.api.services.database.DataSyncService;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.api.services.database.DatabasePasswordService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.InvalidVerificationCodeException;
import eu.furcloud_hosting.exceptions.PasswordHashingException;
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
        if (DataSyncService.isNull(username, email, password)) {
            String error = JSONService.createJsonError("Missing required fields");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        try {
            DatabaseAccountService databaseAccountService = new DatabaseAccountService();
            DatabasePasswordService databasePasswordService = new DatabasePasswordService();
            DataSyncService.beginTransactions(databaseAccountService, databasePasswordService);

            if (databaseAccountService.doesAccountExistEmail(email)) {
                String error = JSONService.createJsonError("Email already in use by another account");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            if (databaseAccountService.doesAccountExistUsername(username)) {
                String error = JSONService.createJsonError("Username already taken");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            String accountId = databaseAccountService.createAccount(username, email);

            VerificationService verificationService = new VerificationService();
            String verificationCode = verificationService.createVerificationCode(accountId);
            verificationService.sendVerificationEmail(email, accountId, verificationCode);

            DataSyncService.commitTransactions(databaseAccountService, databasePasswordService);

            databasePasswordService.saveCredentials(accountId, password);

            String response = JSONService.createJsonSuccess("Account created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DatabaseException | SQLException | PasswordHashingException e) {
            String error = JSONService.createJsonError("Failed to create account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
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
}
