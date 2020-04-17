package com.pepcus.apps.api.services.crypto;

import static com.pepcus.apps.api.constant.ApplicationConstants.AES_ALGO;
import static com.pepcus.apps.api.constant.ApplicationConstants.AES_PKC_PADDING;
import static com.pepcus.apps.api.constant.ApplicationConstants.UTF8;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.pepcus.apps.api.exception.APIErrorCodes;
import com.pepcus.apps.api.exception.ApplicationException;

import lombok.Data;

/**
 * Encryptor\Decryptor with use of AES crypto alogrithm
 * 
 */
@Data
public class AESEncryptorDecryptor implements AppEncryptorDecryptor {
    
    private Cipher encipher;
    private Cipher decipher;
    
    /**
     * Constructor
     * 
     * @param key
     * @param initVector
     */
    public AESEncryptorDecryptor(String key, String initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(UTF8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(UTF8), AES_ALGO);
            encipher = Cipher.getInstance(AES_PKC_PADDING);
            encipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            decipher = Cipher.getInstance(AES_PKC_PADDING);
            decipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        } catch (Exception ex) {
            throw ApplicationException.createEncryptionError(APIErrorCodes.ENCRYPTION_ERROR, ex.getMessage());
        }
    }
    
    /**
     * Constructor without Init Vector
     * @param key
     * @param initVector
     */
    public AESEncryptorDecryptor(String key) {
        try {
        	byte[] defaultBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec iv = new IvParameterSpec(defaultBytes);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(UTF8), AES_ALGO);
            encipher = Cipher.getInstance(AES_PKC_PADDING);
            encipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            decipher = Cipher.getInstance(AES_PKC_PADDING);
            decipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        } catch (Exception ex) {
            throw ApplicationException.createEncryptionError(APIErrorCodes.ENCRYPTION_ERROR, ex.getMessage());
        }
    }

    /**
     * This method is used to encrypt the value.
     * 
     * @param value
     * @return
     */
    @Override
    public String encrypt(String value) {
        try {
            byte[] encrypted = encipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            throw ApplicationException.createEncryptionError(APIErrorCodes.ENCRYPTION_ERROR, ex.getMessage());
        }
    }

    /**
     * This method is used to decrypt the encrypted value.
     * 
     * @param encryptedValue
     * @return
     */
    @Override
    public String decrypt(String encryptedValue) {
        try {
            byte[] original = decipher.doFinal(Base64.decodeBase64(encryptedValue));
            return new String(original);
        } catch (Exception ex) {
            throw ApplicationException.createEncryptionError(APIErrorCodes.DECRYPTION_ERROR, ex.getMessage());
        }
    }
    
}
