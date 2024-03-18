package uk.ac.warwick.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.warwick.domain.AuthenticationResponse;
import uk.ac.warwick.service.AccountService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AccountService accountService;

    @GetMapping("/student")
    public ResponseEntity<AuthenticationResponse> studentLogin() {
        return ResponseEntity.ok(accountService.studentAccess());
    }

    @GetMapping("/academic")
    public ResponseEntity<AuthenticationResponse> academicLogin() {
        return ResponseEntity.ok(accountService.academicAccess());
    }

    @GetMapping("/staff")
    public ResponseEntity<AuthenticationResponse> staffLogin() {
        return ResponseEntity.ok(accountService.staffAccess());
    }

}

