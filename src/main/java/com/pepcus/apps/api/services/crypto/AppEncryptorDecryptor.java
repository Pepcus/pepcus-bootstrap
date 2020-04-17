package com.pepcus.apps.api.services.crypto;

/**
 * Generic interface for password encryption.
 * 
 */
public interface AppEncryptorDecryptor {

    public String encrypt(String plainPassword);

    public String decrypt(String encryptedPassword);

}
