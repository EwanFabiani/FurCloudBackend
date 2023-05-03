package eu.furcloud_hosting.api.models;

public class Account {

    private String accountId;
    private String username;
    private String email;
    private AccountStatus status;
    private boolean admin;

    public Account(String accountId, String username, String email, AccountStatus status, boolean admin) {
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.status = status;
        this.admin = admin;
    }

}
