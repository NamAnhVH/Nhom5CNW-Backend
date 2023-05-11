package com.WebLearning.WebLearning.Security;

import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof CustomUserDetails;
    }

    public Account getAccount() {
        return ((CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getAccount();
    }

}

