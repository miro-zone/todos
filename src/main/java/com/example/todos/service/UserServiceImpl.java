package com.example.todos.service;

import com.example.todos.entity.User;
import com.example.todos.repository.UserRepository;
import com.example.todos.response.UserResponse;
import com.example.todos.util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    @Override
    public UserResponse getUserInfo()  {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        
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
        User currentUser = findAuthenticatedUser.getAuthenticatedUser();
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
    
}
