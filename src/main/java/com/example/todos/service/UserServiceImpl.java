package com.example.todos.service;

import com.example.todos.entity.User;
import com.example.todos.repository.UserRepository;
import com.example.todos.request.UpdatePasswordRequest;
import com.example.todos.response.UserResponse;
import com.example.todos.util.FindAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional
    public void updatePassword(UpdatePasswordRequest request) throws AccessDeniedException {
        User currentUser = findAuthenticatedUser.getAuthenticatedUser();
        
        // Check if old password is correct
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        
        // Check if new password is different from old password
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("New password must be different from old password");
        }
        
        // Check if new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }
        
        // Update the password
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }
}
