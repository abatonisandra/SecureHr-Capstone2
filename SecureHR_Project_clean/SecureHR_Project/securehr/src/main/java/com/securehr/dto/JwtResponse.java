package com.securehr.dto;

import java.util.Set;

public class JwtResponse {
    private String token;
    private String type;
    private String fullName;
    private String email;
    private Set<String> roles;

    public JwtResponse(String token, String type, String fullName, String email, Set<String> roles) {
        this.token = token;
        this.type = type;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public String getType() { return type; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Set<String> getRoles() { return roles; }
}
