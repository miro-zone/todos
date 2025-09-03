package com.example.todos.service;

import com.example.todos.entity.Authority;
import com.example.todos.entity.User;
import com.example.todos.repository.UserRepository;
import com.example.todos.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }

        User user = buildNewUser(request);
        userRepository.save(user);
    }

    private User buildNewUser(RegisterRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .authorities(initialAuthorities())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();


    }

    private Set<Authority> initialAuthorities() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(ROLE_EMPLOYEE));

        if (userRepository.count() == 0) {
            authorities.add(new Authority(ROLE_ADMIN));
        }

        return authorities;
    }
}
