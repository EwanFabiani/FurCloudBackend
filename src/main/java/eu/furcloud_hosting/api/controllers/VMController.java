package eu.furcloud_hosting.api.controllers;

import eu.furcloud_hosting.api.models.DataRequestModel;
import eu.furcloud_hosting.api.services.VMService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vm")
public class VMController {

    @PostMapping("/start")
    public ResponseEntity<String> startVM(@RequestBody DataRequestModel dataRequestModel) {
        String sessionId = dataRequestModel.getSessionId();
        String vmId = dataRequestModel.getData();
        try {
            VMService vmService = new VMService();
            vmService.startVM(sessionId, vmId);
        } catch (DatabaseException e) {
            return ResponseEntity.status(500).body("Failed to start VM");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok("VM started successfully");

    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopVM(@RequestBody DataRequestModel dataRequestModel) {
        String sessionId = dataRequestModel.getSessionId();
        String vmId = dataRequestModel.getData();
        try {
            VMService vmService = new VMService();
            vmService.stopVM(sessionId, vmId);
        } catch (DatabaseException e) {
            return ResponseEntity.status(500).body("Failed to start VM");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok("VM stopped successfully");

    }

    @PostMapping("/restart")
    public ResponseEntity<String> restartVM(@RequestBody DataRequestModel dataRequestModel) {
        String sessionId = dataRequestModel.getSessionId();
        String vmId = dataRequestModel.getData();
        try {
            VMService vmService = new VMService();
            vmService.restartVM(sessionId, vmId);
        } catch (DatabaseException e) {
            return ResponseEntity.status(500).body("Failed to start VM");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok("VM stopped successfully");

    }

}
