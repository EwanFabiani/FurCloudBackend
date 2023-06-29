package eu.furcloud_hosting.api.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Account {

    private String accountId;
    private String username;
    private String email;
    private AccountStatus status;
    private boolean admin;
    private Date createdAt;

    public Account(String accountId, String username, String email, AccountStatus status, boolean admin, Date createdAt) {
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.status = status;
        this.admin = admin;
        this.createdAt = createdAt;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("accountId", accountId);
        map.put("username", username);
        map.put("email", email);
        map.put("status", status.toString());
        map.put("admin", String.valueOf(admin));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(createdAt);
        map.put("createdAt", date);
        return map;
    }

}
