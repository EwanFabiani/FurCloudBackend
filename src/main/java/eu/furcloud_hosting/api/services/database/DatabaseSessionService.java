package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSessionService extends DatabaseService {

    public DatabaseSessionService() throws DatabaseException {
    }

    public void saveSession(String accountId, String sessionId) throws DatabaseException {
        String query = "INSERT INTO sessions (account_id, session_id) VALUES (?, ?)";
        try {
            databaseManager.executeUpdate(query, accountId, sessionId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save session");
        }
    }

    public String getAccountIdFromSessionId(String sessionId) throws DatabaseException {
        String query = "SELECT account_id FROM sessions WHERE session_id = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, sessionId)) {
            return rs.getString("account_id");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get account id from session id");
        }
    }

}
