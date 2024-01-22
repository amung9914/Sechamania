package com.blog.config;

import com.blog.entity.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null == authentication || !authentication.isAuthenticated()){
            return null;
        }
        Object principal =  authentication.getPrincipal();
        if(principal instanceof UserDetails){
            return Optional.of(((UserDetails)principal).getUsername());
        }
        return null;
    }
}
