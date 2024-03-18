package uk.ac.warwick.security;

import org.springframework.security.core.GrantedAuthority;

public enum AuthorityGroup implements GrantedAuthority {

    STUDENT("STUDENT"),
    ACADEMIC("ACADEMIC"),
    STAFF("STAFF")
    ;

    private String name;

    AuthorityGroup(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
