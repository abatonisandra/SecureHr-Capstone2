package com.securehr.service;

import com.securehr.dto.EmployeeRequest;
import com.securehr.dto.EmployeeResponse;
import com.securehr.entity.Employee;
import com.securehr.entity.User;
import com.securehr.exception.BadRequestException;
import com.securehr.exception.ResourceNotFoundException;
import com.securehr.repository.EmployeeRepository;
import com.securehr.repository.UserRepository;
import com.securehr.security.CustomUserDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public EmployeeResponse create(EmployeeRequest request, Authentication authentication) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Employee email already exists");
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(request, employee);
        employee.setManager(resolveManager(authentication));
        return mapToResponse(employeeRepository.save(employee));
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getEmployeeEntity(id);
        BeanUtils.copyProperties(request, employee);
        return mapToResponse(employeeRepository.save(employee));
    }

    public void delete(Long id) {
        Employee employee = getEmployeeEntity(id);
        employeeRepository.delete(employee);
    }

    public List<EmployeeResponse> listAll() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse getEmployee(Long id, Authentication authentication) {
        Employee employee = getEmployeeEntity(id);
        if (hasRole(authentication, "ROLE_ADMIN") || hasRole(authentication, "ROLE_HR")) {
            return mapToResponse(employee);
        }
        User currentUser = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        if (employee.getManager() != null && Objects.equals(employee.getManager().getId(), currentUser.getId())) {
            return mapToResponse(employee);
        }
        throw new AccessDeniedException("You can only view records you own");
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    private Employee getEmployeeEntity(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    private User resolveManager(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
            return userRepository.findById(customUserDetails.getUser().getId())
                    .orElse(customUserDetails.getUser());
        }
        return null;
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        BeanUtils.copyProperties(employee, response);
        if (employee.getManager() != null) {
            response.setManagerEmail(employee.getManager().getEmail());
        }
        return response;
    }
}

