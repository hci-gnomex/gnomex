// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import javax.crypto.SecretKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import java.security.Key;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import java.security.spec.KeySpec;

public class StringEncrypter
{
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    public static final String RC4_ENCRYPTION_SCHEME = "RC4";
    public static final String DEFAULT_ENCRYPTION_KEY = "anONSENsicalSet0FChars&Nums56@346";
    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;
    private static final String UNICODE_FORMAT = "UTF8";
    
    public StringEncrypter(final String encryptionScheme) throws EncryptionException {
        this(encryptionScheme, "anONSENsicalSet0FChars&Nums56@346");
    }
    
    public StringEncrypter(final String encryptionScheme, final String encryptionKey) throws EncryptionException {
        if (encryptionKey == null) {
            throw new IllegalArgumentException("encryption key was null");
        }
        if (encryptionKey.trim().length() < 24) {
            throw new IllegalArgumentException("encryption key was less than 24 characters");
        }
        try {
            final byte[] keyAsBytes = encryptionKey.getBytes("UTF8");
            if (encryptionScheme.equals("DESede")) {
                this.keySpec = new DESedeKeySpec(keyAsBytes);
            }
            else {
                if (!encryptionScheme.equals("DES")) {
                    throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
                }
                this.keySpec = new DESKeySpec(keyAsBytes);
            }
            this.keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
            this.cipher = Cipher.getInstance(encryptionScheme);
        }
        catch (InvalidKeyException e) {
            throw new EncryptionException(e);
        }
        catch (UnsupportedEncodingException e2) {
            throw new EncryptionException(e2);
        }
        catch (NoSuchAlgorithmException e3) {
            throw new EncryptionException(e3);
        }
        catch (NoSuchPaddingException e4) {
            throw new EncryptionException(e4);
        }
    }
    
    public String encrypt(final String unencryptedString) throws IllegalArgumentException {
        if (unencryptedString == null || unencryptedString.trim().length() == 0) {
            throw new IllegalArgumentException("unencrypted string was null or empty");
        }
        try {
            final SecretKey key = this.keyFactory.generateSecret(this.keySpec);
            this.cipher.init(1, key);
            final byte[] encryptedBytes = this.cipher.doFinal(unencryptedString.getBytes("UTF-8"));
            final Base64 encoder = new Base64();
            final String encryptedString = new String(encoder.encode(encryptedBytes), "UTF-8");
            return encryptedString;
        }
        catch (Exception e) {
            Logger.getLogger(StringEncrypter.class.getName()).log(Level.SEVERE, "Unable to encrypt password", e);
            return "";
        }
    }
    
    public String decrypt(final String encryptedString) throws IllegalArgumentException {
        if (encryptedString == null || encryptedString.trim().length() <= 0) {
            throw new IllegalArgumentException("encrypted string was null or empty");
        }
        try {
            final SecretKey key = this.keyFactory.generateSecret(this.keySpec);
            this.cipher.init(2, key);
            final Base64 encoder = new Base64();
            final byte[] encryptedBytes = encoder.decode(encryptedString.getBytes("UTF-8"));
            final byte[] decryptedBytes = this.cipher.doFinal(encryptedBytes);
            final String decryptedString = new String(decryptedBytes, "UTF-8");
            return decryptedString;
        }
        catch (Exception e) {
            Logger.getLogger(StringEncrypter.class.getName()).log(Level.SEVERE, "Unable to decrypt password", e);
            return "";
        }
    }
    
    public static class EncryptionException extends Exception
    {
        private static final long serialVersionUID = 1L;
        
        public EncryptionException(final Throwable t) {
            super(t);
        }
    }
}
