package com.rt_chatApp.Thymeleaf.Service;

import com.rt_chatApp.security.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    public String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User user){
            return user.getDisplayName();
        }

        return null;
    }

}
