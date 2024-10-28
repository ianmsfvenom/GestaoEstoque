package gestaoestoque.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CriptoUtils {
    private static final String SALT = "ON4a5XntBWkAm7FE9DeD1d4JofY";

    public static String gerarHashComSalt(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] textoComSalt = (senha + SALT).getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(textoComSalt);
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256", e);
        }
    }

    public static boolean compararSenha(String senhaFornecida, String hashArmazenado) {
        String hashGerado = gerarHashComSalt(senhaFornecida);
        return hashGerado.equals(hashArmazenado);
    }
    
    private static byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }
    
    public static String encryptAES(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptAES(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
    
    public static SecretKey loadKeyAES() {
        byte[] decodedKey = PreferencesUtils.AES_KEY.getBytes();
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
