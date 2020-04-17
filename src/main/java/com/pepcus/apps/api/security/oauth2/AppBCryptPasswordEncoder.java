package com.pepcus.apps.api.security.oauth2;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * To handle salting prefix discrepany between PHP and Java libary
 * 
 */
public class AppBCryptPasswordEncoder extends BCryptPasswordEncoder { 
    private final String LATEST_SALT_PREFIX = "$2y";
    private final String OLD_SALT_PREFIX_ONLY_IN_JAVA = "$2a";
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //Checking and replacing "$2y" prefix with "$2a" to make it compatible with Java BCrypt library.
        if (encodedPassword.contains(LATEST_SALT_PREFIX)) {
            encodedPassword = encodedPassword.replace(LATEST_SALT_PREFIX, 
                    OLD_SALT_PREFIX_ONLY_IN_JAVA);
        }
        return super.matches(rawPassword, encodedPassword);
    }

}
