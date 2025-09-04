package com.example.todos.service;

import com.example.todos.entity.Authority;
import com.example.todos.entity.User;
import com.example.todos.repository.UserRepository;
import com.example.todos.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserResponse promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        boolean alreadyAdmin = user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (!alreadyAdmin) {
            user.getRawAuthorities().add(new Authority("ROLE_ADMIN"));
            userRepository.save(user);
        }
        return toResponse(user);
    }

    @Override
    @Transactional
    public void deleteNonAdminUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete admin user");
        }
        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        return new UserResponse(user.getId(), fullName, user.getEmail(), user.getAuthorities());
    }
}
