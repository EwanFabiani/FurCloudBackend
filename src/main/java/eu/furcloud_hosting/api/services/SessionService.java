package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.services.database.DatabaseSessionService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.LoginException;
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

}
