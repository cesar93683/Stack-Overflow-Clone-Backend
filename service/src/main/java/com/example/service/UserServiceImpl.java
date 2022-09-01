package com.example.service;

import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.exceptions.ServiceException;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUserById(int id) throws ServiceException {
        return new UserDTO(userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("User not found with id: " + id)));
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

    @Override
    public int getUserId(String username) throws ServiceException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("User not found with username: " + username));
        return user.getId();
    }

}
