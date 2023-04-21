package com.spike.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @PACKAGE_NAME: com.example.fxpgJava.utils
 * @NAME: EncryptAndDecrypte
 * @USER: spike
 * @DATE: 2023/4/11 10:33
 * @PROJECT_NAME: FXPG_Java
 */

public class EncryptAndDecrypte {
    static String strKey = "yukl4y6scvelf5Y6bnhGrMC3PbryXrxw";
    static String strIV = "RooMfrffc48=";

    /**
     * 默认加密字符串。
     *
     * @param encryptString
     * @return
     */
    public static String encryptString(String encryptString) throws Exception {
        if (!checkStringNull(encryptString)) {
            return encryptString;
        }
        return EncryptAndDecrypte.encrypt(encryptString.getBytes(StandardCharsets.UTF_8), fromBase64String(strKey), fromBase64String(strIV));
    }

    /**
     * 默认解密字符串
     *
     * @param encryptedConnectionString
     * @return
     */
    public static String decrypteString(String encryptedConnectionString) throws Exception {
        if (!checkStringNull(encryptedConnectionString)) {
            return encryptedConnectionString;
        }
        return EncryptAndDecrypte.decrypt(fromBase64String(encryptedConnectionString), fromBase64String(strKey), fromBase64String(strIV));
    }

    /**
     * 使用指定的 Key 和 IV 加密 。
     *
     * @param cryptoBytes 明文
     * @param key 密钥
     * @param iv IV
     * @return Base64编码的密文
     * @throws Exception
     */
    private static String encrypt(byte[] cryptoBytes, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] cipherTextBytes = cipher.doFinal(cryptoBytes);
        return toBase64String(cipherTextBytes);
    }

    /**
     * 使用指定的 Key 和 IV 解密 。
     *
     * @param cryptoBytes data Base64编码的密文
     * @param key 密钥
     * @param iv IV
     * @return 明文
     * @throws Exception
     */
    private static String decrypt(byte[] cryptoBytes, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] cipherTextBytes = cipher.doFinal(cryptoBytes);
        return new String(cipherTextBytes, "UTF-8");
    }


    private static String toBase64String(byte[] listByte) {
        String result = Base64.getEncoder().encodeToString(listByte);
        return result;
    }

    private static byte[] fromBase64String(String src) {
        byte[] decode = Base64.getDecoder().decode(src.getBytes(StandardCharsets.UTF_8));
        return decode;
    }

    private static boolean checkStringNull(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
