package com.eshore.common.utils;


import com.eshore.common.constant.CommConstants;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AesUtil {

    //密钥 (需要前端和后端保持一致) 此为视频固定秘钥请勿乱修改
    private static final String KEY = "e577774eaa06fd6c557539af6929681d";
    private static final String IV = "1234567890abcdef";

    public static String encrypt(String data) throws Exception {
        return encrypt(data, KEY, IV);
    }

    /**
     * 告警推送MQ数据加密处理
     *
     * @param text
     * @param code
     * @return
     */
    public static String encrypt(String text, String code) {
        if (code.length() != 16) {
            return null;
        }
        byte[] keyByte = code.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(Cipher instance, String data) throws Exception {
        return encrypt(instance, data, KEY, IV);
    }

    public static String encrypt(String data, String key, String iv) throws Exception {
        return encrypt(Cipher.getInstance(CommConstants.CIPHER_PADDING.NO_PADDING), data, key, iv);
    }

    public static String encrypt(Cipher cipher, String data, String key, String iv) throws Exception {
        try {
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength += blockSize - plaintextLength % blockSize;
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String data) throws Exception {
        return decrypt(data, KEY, IV);
    }

    public static void main(String[] args) throws Exception {
        String data = "IOYbZvUM1JxWG7JskIZoX2kIyeA27TgdQ/LY2n+bW++TK+AmXAR+LrhLG01r2lJc+n2xw4Yfs2rL+DpE8iykBUZE54wvnfeYxtnEKn4PhE8pUTLoBMWayvAgcCAAbpVdNYBR5IaMGkbCkr6qQAq4gbBHSXpXLgLyy63ojnHEs04CbLiGjwKkZhRrB5rn9KsRIfINsgXbEl9ql/1Ridg6XTHb/Qf6Jqd1/6C9vdOfTER8PavtuZWQK9dfeHpZvtc62XgNwpVOFN7dlaAUOrStQA==";
        String decrypt = decrypt(data, KEY, IV);
        System.out.println(decrypt);
    }

    public static String decrypt(Cipher instance, String data) throws Exception {
        return decrypt(instance, data, KEY, IV);
    }

    public static String decrypt(String data, String key, String iv) throws Exception {
        return decrypt(Cipher.getInstance(CommConstants.CIPHER_PADDING.NO_PADDING), data, key, iv);
    }

    public static String decrypt(Cipher cipher, String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = Base64.getDecoder().decode(data);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
