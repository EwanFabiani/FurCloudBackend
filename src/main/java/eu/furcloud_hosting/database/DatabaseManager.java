package eu.furcloud_hosting.database;

import java.sql.*;

import org.apache.commons.dbutils.DbUtils;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mariadb://172.17.0.3:3306/furcloud";
    private static final String user = "root";
    private static final String password = "374rR2847375d6Wb6B5r6Gs9p1VFqd881ssFNYcL8634asrFfS1rUAUN39R178ra";

    private boolean transactionActive = false;

    private Connection connection;

    public DatabaseManager() throws SQLException {
        DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
        connection = DriverManager.getConnection(DB_URL, user, password);
    }

    public ResultSet executeQuery(String query, String... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }
            return ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public int executeUpdate(String query, String... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void beginTransaction() throws SQLException {
        if (transactionActive) {
            throw new IllegalStateException("Transaction already active");
        }
        connection.setAutoCommit(false);
        transactionActive = true;
    }

    public void commitTransaction() throws SQLException {
        if (!transactionActive) {
            throw new IllegalStateException("No transaction active");
        }
        connection.commit();
        connection.setAutoCommit(true);
        transactionActive = false;
    }

    public void rollbackTransaction() throws SQLException {
        if (!transactionActive) {
            throw new IllegalStateException("No transaction active");
        }
        connection.rollback();
        connection.setAutoCommit(true);
        transactionActive = false;
    }

    public boolean isTransactionActive() {
        return transactionActive;
    }

    public void close() {
        DbUtils.closeQuietly(connection);
    }

}