package eu.furcloud_hosting.api.services;

import java.util.UUID;

public class IDService {

    public static String generateID() {
        return UUID.randomUUID().toString();
    }
}
