package com.example.rest.utils;

import com.example.exceptions.UserException;
import com.example.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.utils.Constants.NO_USER_ID;

public class Utils {

    public static int getUserIdIfExists() {
        try {
            return getUserId();
        } catch (UserException ignored) {
            return NO_USER_ID;
        }
    }

    public static int getUserId() throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        throw new UserException("User id not found");
    }
}
