package br.com.pdasolucoes.standardconfig.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.annotation.SuppressLint;
import android.util.Base64;

public class Cryptography {

    private Cryptography() {

    }

    public static byte[] calculateMD5(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes(Charset.forName("UTF8")));

            return messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeBase64(byte[] input) {
        byte[] encodedBytes = Base64.encode(input, Base64.NO_WRAP);
        return new String(encodedBytes, Charset.forName("UTF8"));
    }

    public static byte[] decodeBase64(String input) {
        return Base64.decode(input.getBytes(Charset.forName("UTF8")), Base64.NO_WRAP);
    }

    private static SecretKey getSecretKey() {
        //String secretKeyBase64 = MyApplication.getStringResource(R.string.SECURITY_KEY);
        String secretKeyBase64 = "SECURITY_KEY";
        byte[] secretKeyBytes = Cryptography.decodeBase64(secretKeyBase64);
        return new SecretKeySpec(secretKeyBytes, "AES");
    }

    @SuppressLint("TrulyRandom")
    public static byte[] encrypt(byte[] input) throws Exception {
        SecretKey secretKey = getSecretKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return aesCipher.doFinal(input);
    }

    public static String encrypt(String input) {
        String output = input;

        try {
            byte[] decryptedBytes = input.getBytes(Charset.forName("UTF8"));
            byte[] encryptedBytes = Cryptography.encrypt(decryptedBytes);
            output = Cryptography.encodeBase64(encryptedBytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return output;
    }

    public static byte[] decrypt(byte[] input) throws Exception {
        SecretKey secretKey = getSecretKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey);

        return aesCipher.doFinal(input);
    }

    public static String decrypt(String input) {
        String output = input;

        try {
            byte[] encryptedBytes = Cryptography.decodeBase64(input);
            byte[] decryptedBytes = Cryptography.decrypt(encryptedBytes);
            output = new String(decryptedBytes, Charset.forName("UTF8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return output;
    }
}
