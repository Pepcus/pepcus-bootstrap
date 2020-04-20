package com.pepcus.apps.services.crypto;

/**
 * Generic interface for password encryption.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public interface AppEncryptorDecryptor {

  public String encrypt(String plainPassword);

  public String decrypt(String encryptedPassword);

}
