package eu.furcloud_hosting.api.controllers;

import eu.furcloud_hosting.api.data.Account;
import eu.furcloud_hosting.api.data.OperatingSystem;
import eu.furcloud_hosting.api.models.PurchaseBody;
import eu.furcloud_hosting.api.models.ResponseStatus;
import eu.furcloud_hosting.api.services.JSONService;
import eu.furcloud_hosting.api.services.SessionService;
import eu.furcloud_hosting.api.services.VMService;
import eu.furcloud_hosting.exceptions.SessionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/vm")
public class VMController {

    @PostMapping("/create")
    public ResponseEntity<String> createVM(@RequestBody PurchaseBody purchaseBody) {
        try {
            SessionService sessionService = new SessionService();
            String sessionID = purchaseBody.getSessionID();
            Account account = sessionService.validateSession(sessionID);
            if (!VMService.areArgumentsValid(purchaseBody)) throw new IllegalArgumentException("Invalid arguments");

        } catch (SessionException e) {

        }
        return null;
        //TODO: Implement
    }

    @GetMapping("/listos")
    public ResponseEntity<String> listOS() {
        OperatingSystem[] operatingSystems = OperatingSystem.values();
        HashMap<String, String> data = new HashMap<>();
        for (OperatingSystem operatingSystem : operatingSystems) {
            data.put(operatingSystem.getLabel(), operatingSystem.getIcon());
        }
        String response = JSONService.createJsonData(ResponseStatus.SUCCESS, data);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
