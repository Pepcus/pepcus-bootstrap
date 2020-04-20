package com.pepcus.apps.services.crypto;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * To hash password using Bcrypt blowfish alogrithm.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public class BCryptPasswordEncryptor implements AppEncryptorDecryptor {

  public String encrypt(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public String decrypt(String encryptedPassword) {
    throw new UnsupportedOperationException("Not supported");
  }
}
