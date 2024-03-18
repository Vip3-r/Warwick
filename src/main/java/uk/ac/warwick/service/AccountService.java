package uk.ac.warwick.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import uk.ac.warwick.domain.AuthenticationResponse;
import uk.ac.warwick.model.Account;
import uk.ac.warwick.security.AuthorityGroup;

import java.util.List;

import static uk.ac.warwick.utility.JwtUtility.generateToken;

@Service
public class AccountService {

    private static Account student, academic, staff;

    @PostConstruct
    public void init() {
        student = new Account("Student", "secret", List.of(AuthorityGroup.STUDENT));
        academic = new Account("Academic", "secret", List.of(AuthorityGroup.ACADEMIC));
        staff = new Account("Staff", "secret", List.of(AuthorityGroup.STAFF));
    }

    public AuthenticationResponse studentAccess() {
        return generateToken(student);
    }

    public AuthenticationResponse academicAccess() {
        return generateToken(academic);
    }

    public AuthenticationResponse staffAccess() {
        return generateToken(staff);
    }

    public Account loadUserByUsername(String username) {
        if (username.equals("Academic")) {
            return academic;
        }
        if (username.equals("Staff")) {
            return staff;
        }
        return student;
    }
}
