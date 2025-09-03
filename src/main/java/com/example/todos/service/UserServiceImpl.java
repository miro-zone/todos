package com.example.todos.service;

import com.example.todos.entity.User;
import com.example.todos.repository.UserRepository;
import com.example.todos.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse getUserInfo() throws AccessDeniedException {
        User user = getCurrentUser();
        
        return new UserResponse(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getAuthorities()
        );
    }

    @Override
    @Transactional
    public void deleteUser() throws AccessDeniedException {
        User currentUser = getCurrentUser();
        if (isLastAdmin(currentUser)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete last admin");
        }
        userRepository.delete(currentUser);
    }

    private boolean isLastAdmin(User user){
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return userRepository.adminsCount() <= 1;
        }
        return false;
    }
    
    private User getCurrentUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication required");
        }
        
        return (User) authentication.getPrincipal();
    }
}
