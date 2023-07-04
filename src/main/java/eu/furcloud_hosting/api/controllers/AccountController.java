package eu.furcloud_hosting.api.controllers;

import eu.furcloud_hosting.api.data.Account;
import eu.furcloud_hosting.api.models.AccountCreationModel;
import eu.furcloud_hosting.api.models.DataRequestModel;
import eu.furcloud_hosting.api.models.LoginModel;
import eu.furcloud_hosting.api.models.ResponseStatus;
import eu.furcloud_hosting.api.services.*;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountCreationModel accountCreationModel) {
        String username = accountCreationModel.getUsername();
        String email = accountCreationModel.getEmail();
        String password = accountCreationModel.getPassword();
        try {
            RegisterService registerService = new RegisterService();
            registerService.createAccount(username, email, password);
            String response = JSONService.createJsonSuccess("Account created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
            LoginService loginService = new LoginService();
            String accountId = loginService.authenticateUser(identifier, password);
            SessionService sessionService = new SessionService();
            String sessionId = sessionService.createSession(accountId);
            HashMap<String, String> responseMap = new HashMap<>();
            responseMap.put("sessionId", sessionId);
            String response = JSONService.createJsonData(ResponseStatus.SUCCESS, responseMap);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (LoginException e) {
            String error = JSONService.createJsonError(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<String> getAccount(@PathVariable String sessionId) {
        try {
            SessionService sessionService = new SessionService();
            Account account = sessionService.validateSession(sessionId);
            String response = JSONService.createJsonData(ResponseStatus.SUCCESS, account.toHashMap());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SessionException e) {
            String error = JSONService.createJsonError(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    @PostMapping("/logout/{sessionId}")
    public ResponseEntity<String> logout(@PathVariable String sessionId) {
        try {
            SessionService sessionService = new SessionService();
            sessionService.deleteSession(sessionId);
            String response = JSONService.createJsonSuccess("Logged out successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SessionException e) {
            String error = JSONService.createJsonError(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    @PostMapping("/delete_sessions/{sessionId}")
    public ResponseEntity<String> deleteSessions(@PathVariable String sessionId) {
        try {
            SessionService sessionService = new SessionService();
            Account account = sessionService.validateSession(sessionId);
            String accountId = account.getAccountId();
            sessionService.deleteAllSessions(accountId);
            String response = JSONService.createJsonSuccess("Deleted all logged in sessions successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SessionException e) {
            String error = JSONService.createJsonError(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    //TODO: THIS IS UNFINISHED
    @PostMapping("/modify/username")
    public ResponseEntity<String> modifyUsername(@RequestBody DataRequestModel dataRequestModel) {
        SessionService sessionService = new SessionService();
        try {
            Account account = sessionService.validateSession(dataRequestModel.getSessionId());
        } catch (SessionException e) {
            e.printStackTrace();
        }
        return null;
    }

}