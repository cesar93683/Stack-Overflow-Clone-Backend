package com.example.rest.utils;

import com.example.exceptions.ServiceException;
import com.example.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.utils.Constants.NO_USER_ID;

public class Utils {

    public static int getUserIdIfExists() {
        try {
            return getUserId();
        } catch (ServiceException ignored) {
            return NO_USER_ID;
        }
    }

    public static int getUserId() throws ServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        throw new ServiceException("User id not found");
    }
}
