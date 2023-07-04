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

    public String getAccountIdFromSessionId(String sessionId) throws DatabaseException, IllegalArgumentException {
        String query = "SELECT account_id FROM sessions WHERE session_id = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, sessionId)) {
            if (rs.next()) {
                return rs.getString("account_id");
            } else {
                throw new IllegalArgumentException("No account with session id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get account id from session id");
        }
    }

    public boolean sessionExists(String sessionId) throws DatabaseException {
        String query = "SELECT * FROM sessions WHERE session_id = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, sessionId)) {
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if session exists");
        }
    }

    public void deleteSession(String sessionId) throws DatabaseException, IllegalArgumentException{
        String query = "DELETE FROM sessions WHERE session_id = ?";
        try {
            if (!sessionExists(sessionId)) {
                throw new IllegalArgumentException("Session does not exist");
            }
            databaseManager.executeUpdate(query, sessionId);
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Failed to delete session");
        }
    }

    public void deleteAllSessions(String accountId) throws DatabaseException {
        String query = "DELETE FROM sessions WHERE account_id = ?";
        try {
            databaseManager.executeUpdate(query, accountId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete sessions");
        }
    }
}
