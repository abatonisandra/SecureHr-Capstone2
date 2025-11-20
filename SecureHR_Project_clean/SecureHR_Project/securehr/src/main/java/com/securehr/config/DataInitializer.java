package com.securehr.config;

import com.securehr.entity.Role;
import com.securehr.entity.User;
import com.securehr.repository.RoleRepository;
import com.securehr.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Arrays.stream(Role.RoleName.values()).forEach(roleName ->
                roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(createRole(roleName))));

        if (!userRepository.existsByEmail("admin@securehr.com")) {
            User admin = new User();
            admin.setEmail("admin@securehr.com");
            admin.setFullName("SecureHR Admin");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setRoles(Set.copyOf(roleRepository.findAll()));
            userRepository.save(admin);
        }
    }

    private Role createRole(Role.RoleName name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
