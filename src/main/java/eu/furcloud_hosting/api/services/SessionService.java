package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.data.Account;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.api.services.database.DatabaseSessionService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.LoginException;
import eu.furcloud_hosting.exceptions.SessionException;
import org.springframework.http.HttpStatus;

public class SessionService {

    public String createSession(String accountId) throws LoginException {
        try {
            String sessionId = IDService.generateID();
            DatabaseSessionService databaseSessionService = new DatabaseSessionService();
            databaseSessionService.saveSession(accountId, sessionId);
            return sessionId;
        } catch (DatabaseException e) {
            throw new LoginException("Failed to create session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Account validateSession(String sessionId) throws SessionException {
        try {
            DatabaseSessionService databaseSessionService = new DatabaseSessionService();
            String accountId = databaseSessionService.getAccountIdFromSessionId(sessionId);
            DatabaseAccountService databaseAccountService = new DatabaseAccountService();
            return databaseAccountService.getAccountFromId(accountId);
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw new SessionException("Failed to validate session", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            throw new SessionException("Invalid session", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteSession(String sessionId) throws SessionException {
        try {
            DatabaseSessionService databaseSessionService = new DatabaseSessionService();
            databaseSessionService.deleteSession(sessionId);
        } catch (DatabaseException e) {
            throw new SessionException("Failed to delete session", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            throw new SessionException("Invalid session", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteAllSessions(String accountId) throws SessionException {
        try {
            DatabaseSessionService databaseSessionService = new DatabaseSessionService();
            databaseSessionService.deleteAllSessions(accountId);
        } catch (DatabaseException e) {
            throw new SessionException("Failed to delete sessions", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
