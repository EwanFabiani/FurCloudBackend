package eu.furcloud_hosting.api.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.furcloud_hosting.api.models.Account;
import eu.furcloud_hosting.api.models.AccountCreationModel;
import eu.furcloud_hosting.api.models.LoginModel;
import eu.furcloud_hosting.api.models.ResponseStatus;
import eu.furcloud_hosting.api.services.*;
import eu.furcloud_hosting.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
            String response = JSONService.createJSON(ResponseStatus.SUCCESS, responseMap);
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
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            String accountJson = gson.toJson(account);
            HashMap<String, String> responseMap = gson.fromJson(accountJson, type);
            String response = JSONService.createJSON(ResponseStatus.SUCCESS, responseMap);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SessionException e) {
            String error = JSONService.createJsonError(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }
}