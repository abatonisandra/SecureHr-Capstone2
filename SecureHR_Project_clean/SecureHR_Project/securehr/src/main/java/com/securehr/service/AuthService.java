package com.securehr.service;

import com.securehr.dto.JwtResponse;
import com.securehr.dto.LoginRequest;
import com.securehr.dto.SignupRequest;
import com.securehr.entity.Role;
import com.securehr.entity.User;
import com.securehr.exception.BadRequestException;
import com.securehr.repository.RoleRepository;
import com.securehr.repository.UserRepository;
import com.securehr.security.CustomUserDetails;
import com.securehr.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public JwtResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(principal);

        Set<String> roles = principal.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toSet());

        return new JwtResponse(token, "Bearer", principal.getUser().getFullName(), principal.getUsername(), roles);
    }

    public void register(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(resolveRoles(request.getRoles()));

        userRepository.save(user);
    }

    private Set<Role> resolveRoles(Set<String> requestedRoles) {
        Set<String> roles = (requestedRoles == null || requestedRoles.isEmpty())
                ? Collections.singleton("ROLE_EMPLOYEE")
                : requestedRoles;

        return roles.stream()
                .map(roleName -> roleRepository.findByName(Role.RoleName.valueOf(roleName))
                        .orElseThrow(() -> new BadRequestException("Invalid role: " + roleName)))
                .collect(Collectors.toSet());
    }
}
