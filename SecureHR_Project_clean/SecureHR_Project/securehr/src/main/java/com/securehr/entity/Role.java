package com.securehr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    public enum RoleName {
        ROLE_ADMIN,
        ROLE_HR,
        ROLE_EMPLOYEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RoleName name;

    public Role() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RoleName getName() { return name; }
    public void setName(RoleName name) { this.name = name; }
}
