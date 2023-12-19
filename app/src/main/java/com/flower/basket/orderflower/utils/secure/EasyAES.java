package com.flower.basket.orderflower.utils.secure;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//AES (Advanced Encryption Standard)
public class EasyAES {

    //-----category constants-----
    /**
     * The preset Initialization Vector is 0 of 16 Bits
     */
    private static final IvParameterSpec DEFAULT_IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    /**
     * The encryption algorithm uses AES
     */
    private static final String ALGORITHM = "AES";
    /**
     * AES uses CBC mode and PKCS5Padding
     */
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    //-----Member variables-----
    /**
     * Obtain the secret key for AES encryption and decryption
     */
    private Key key;
    /**
     * Initialization Vector used in AES CBC mode
     */
    private IvParameterSpec iv;
    /**
     * Cipher properties
     */
    private Cipher cipher;

    /**
     * Constructor, use 128 Bits AES key (calculate MD5 of any length key) and preset IV
     *
     * @param key Pass in any length AES key
     */
    public EasyAES(final String key) {
        this(key, 128);
    }

    /**
     * Constructor, use 128 Bits or 256 Bits AES key (calculate MD5 or SHA256 of any length key) and preset IV
     *
     * @param key Pass in any length AES key
     * @param bit Incoming AES key length, the value can be 128, 256 (Bits)
     */
    public EasyAES(final String key, final int bit) {
        this(key, bit, null);
    }

    /**
     * Constructor, use 128 Bits or 256 Bits AES key (calculate MD5 or SHA256 of arbitrary length key), use MD5 to calculate IV value
     *
     * @param key Pass in any length AES key
     * @param bit Incoming AES key length, the value can be 128, 256 (Bits)
     * @param iv Pass in an IV string of any length
     */
    public EasyAES(final String key, final int bit, final String iv) {
        if (bit == 256) {
            this.key = new SecretKeySpec(getHash("SHA-256", key), ALGORITHM);
        } else {
            this.key = new SecretKeySpec(getHash("MD5", key), ALGORITHM);
        }

        if (iv != null) {
            this.iv = new IvParameterSpec(getHash("MD5", iv));
        } else {
            this.iv = DEFAULT_IV;
        }

        init();
    }

    //-----Object method-----
    /**
     * Get the Hash value of the string
     *
     * @param algorithm incoming hash algorithm
     * @param text Pass in the string to be hashed
     * @return returns the hashed content
     */
    private static byte[] getHash(final String algorithm, final String text) {
        try {
            return getHash(algorithm, text.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Obtain the Hash value of the data
     *
     * @param algorithm incoming hash algorithm
     * @param data pass in the data to be hashed
     * @return returns the hashed content
     */
    private static byte[] getHash(final String algorithm, final byte[] data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * initialization
     */
    private  void  init () {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * encrypted text
     *
     * @param str pass in the text to be encrypted
     * @return returns the encrypted text
     */
    public String encrypt(final String str) {
        try {
            return encrypt(str.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * encrypted data
     *
     * @param data pass in the data to be encrypted
     * @return return encrypted data
     */
    public String encrypt(final byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final byte[] encryptData = cipher.doFinal(data);
            return new String(Base64.encode(encryptData, Base64.DEFAULT), "UTF-8");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * decrypt text
     *
     * @param str pass in the text to be decrypted
     * @return returns the decrypted text
     */
    public String decrypt(final String str) {
        try {
            return decrypt(Base64.decode(str, Base64.DEFAULT));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * decrypt text
     *
     * @param data pass in the data to be decrypted
     * @return returns the decrypted text
     */
    public String decrypt(final byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            final byte[] decryptData = cipher.doFinal(data);
            return new String(decryptData, "UTF-8");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String encryptString(String content) {
        //Set password and iv string here
        EasyAES ea = new EasyAES("****************", 128, "################");
        return ea.encrypt(content);
    }

    public static String decryptString(String content) {
        String result = null;
        try {
            //Set password and iv string here
            EasyAES ea = new EasyAES("****************", 128, "################");
            result = ea.decrypt(content);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
