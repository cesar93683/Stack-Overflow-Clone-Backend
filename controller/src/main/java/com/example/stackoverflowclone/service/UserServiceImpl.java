package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.UserDTO;
import com.example.stackoverflowclone.entity.User;
import com.example.stackoverflowclone.exceptions.UserException;
import com.example.stackoverflowclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUserById(int id) throws UserException {
        return new UserDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id)));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void register(User user) {
        userRepository.save(user);
    }
}
