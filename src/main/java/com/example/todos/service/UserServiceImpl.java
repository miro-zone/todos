package com.example.todos.service;

import com.example.todos.entity.User;
import com.example.todos.response.UserResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse getUserInfo() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication required");
        }
        
        User user = (User)authentication.getPrincipal();
        
        return new UserResponse(
                user.getId(),
                user.getFirstName()+ " " + user.getLastName(),
                user.getEmail(),
                user.getAuthorities()
        );
    }
}
