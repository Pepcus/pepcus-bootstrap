package com.pepcus.apps.services.crypto;

/**
 * Generic interface for with comment method for encryption/decryption.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public interface AppEncryptorDecryptor {

  public String encrypt(String plainPassword);

  public String decrypt(String encryptedPassword);

}
