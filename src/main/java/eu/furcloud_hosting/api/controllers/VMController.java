package eu.furcloud_hosting.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vm")
public class VMController {

    @GetMapping("/os")
    public ResponseEntity<String> getOS() {
        return null;
    }

}
