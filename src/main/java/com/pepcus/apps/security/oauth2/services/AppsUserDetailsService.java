package com.pepcus.apps.security.oauth2.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pepcus.apps.db.entities.UserEntity;
import com.pepcus.apps.db.repositories.UserRepository;

/**
 * Implementation class of UserDetailsService to load user data for Security
 * 
 * @author sandeep.vishwakarma
 *
 */
@Service
public class AppsUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User
        		(user.getUsername(), user.getEncryptedPassword(), Collections.emptyList());
    }

}