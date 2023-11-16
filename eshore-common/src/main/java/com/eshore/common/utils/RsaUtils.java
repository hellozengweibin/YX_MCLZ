package com.eshore.common.utils;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author shanglangxin
 * @since 2022/11/8 12:34
 */
public class RsaUtils {

    /**
     * 算法名称
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 默认密钥大小
     */
    private static final int KEY_SIZE = 2048;
    /**
     * 密钥对生成器
     */
    private static KeyPairGenerator keyPairGenerator = null;

    private static KeyFactory keyFactory = null;
    /**
     * 缓存的密钥对
     */
    private static KeyPair keyPair = null;

    /**
     * Base64 编码/解码器 JDK1.8
     */
    private static Base64.Decoder decoder = Base64.getDecoder();
    private static Base64.Encoder encoder = Base64.getEncoder();

    public static final String PEM_CODE_PUB = "MEEwDQYJKoZIhvcNAQEBBQADMAAwLQImDr5/bK6tmdEMYTJXsD/AXIOwE2a9/bfkPvtWUR7vzkvB33tPcEsCAwEAAQ==";

    public final static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArlSdHxWjfSbGvHWkhcryEyS9hdresAc6UnA9vazAjgj4tounQ9c2ddnMv0IwS2I6atxF2P4n8j3ged9jZ1AfKMubbBbDjmbJIecQNWw0a5l8ffymp/y9PGuBjEiD4tMIU3zwT/0/RvO2pvUSvguWSzArlLvWDBQL90pfwf+eKaoDGW4G0FN0DuPhkrz1jJNUFRBKyxvUyoJL6TaHsLTnhf2mF4XX599kCDtLNpcB0zniZxwPT+AjpT4fRSj+EB2Z5U/pnTwUTe5f1jnzok1iBoFBxtbvOSXKljZU1hhyxAxMFXHLTLWKz1Jot46MqIYlem0hjXigME3fyOCYDIaSqwIDAQAB";
    public final static String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCuVJ0fFaN9Jsa8daSFyvITJL2F2t6wBzpScD29rMCOCPi2i6dD1zZ12cy/QjBLYjpq3EXY/ifyPeB532NnUB8oy5tsFsOOZskh5xA1bDRrmXx9/Kan/L08a4GMSIPi0whTfPBP/T9G87am9RK+C5ZLMCuUu9YMFAv3Sl/B/54pqgMZbgbQU3QO4+GSvPWMk1QVEErLG9TKgkvpNoewtOeF/aYXhdfn32QIO0s2lwHTOeJnHA9P4COlPh9FKP4QHZnlT+mdPBRN7l/WOfOiTWIGgUHG1u85JcqWNlTWGHLEDEwVcctMtYrPUmi3joyohiV6bSGNeKAwTd/I4JgMhpKrAgMBAAECggEAWkCSC4q/Vkah8uWQ5HdZ9hE6UTcrvMQxLIbRYAY0thPs90y7rsKXflHgz0+sAwr6pWIegyWpdp8UPOQq6UywpQUsFLyAR5PtIzEl0hKP3PjcW7lWanfNXJ9ntUHBjcelctum6jJ49PmtoSgVqPAEwi5REbJhMWWbgE4v3xbAQl7Xw+FbYNwCcjH7CTp59ASFizIm2S4R6a9PRsK3tc23/lzfSRxyj25gtgQaa00vTDLDr6D+CzBaMt7S8EIbcV3Rnqv8XFOtKhRrK25I4xD+yOKST0O12Rl0kKpt31e+zw1a1oBFYB6lHg0vRU9RL9F9D9nQ5A3Y4WAPKukwXwwBQQKBgQDk62C7LE/PQG6PcoJV8RzluFXlvFZdZ/WjSELgkPBzrJCzzXj4RXrplbrJfPf7QPY/iTiDs/tQqqwKmILtL7wCNboOjCadDZI2+hW38RmbRZxWb1r/lYSb33ssOk+EExScuX1w3mzS9eJAmBmaF4ZXrid2Yd6sCfA7rqTzKkdISwKBgQDC9A8RDvEBQYD4AysvR1SWN8oM9yIJjULOkLbw2+zxAgjgphaYH0XljEcVsLiL5D3ysRDAPDILxvJIeKtwugrQzoA7JQC4b5p7FpsJeAp7mNOhwURpDMbbp9ogopA21va4VNx5RcJBAfncdMnKCGGez8YmUtK5WzKPwswA7twjIQKBgQC6428VYBfIG9InSlUd1LrC0LtP03KuzzC7HUN8Etg7l6aLJDSefBrcmGDmHiBV/7MKK/z3iPij4rKR8/DvswjKxRIgzkq37Vxxhbo1caB2I15/MmoYPvkESG9GrFQ+f1VUnhrLCsvNbvBHy+Ua6BnFe2i8PSqNSY/XKb2AyANqAQKBgFZQaDtNOjcsIUWtYXlVQ4YevT5wTImeGW4LdHO/a3BpTjToVCiV8ARpzy8zRwHo0+SXsKrr+9xrAOpFQuJt/Evmoa+N+nuuLd9BavMOStXeHBUJ1JgOKB0oe9LnJqKhGisSp2qqEA9oKJAKxSzF+TKvGzPsh4+5aPFtdZUULDMBAoGBAKugeBrk7NK1OUxzVPWMnM3ebmOPyzJ11fVGZTlFE1lJgTrSfZnoOA9fgUc1snqiZaUec9vDGwxMswM+mZCm2exZSoldLoxeL1o7dwkV/8vLTUAgSQc8edUCBIUJODZ1jpev586ywO/BHmVAuPEkB3C+c5K3Tej0b8i6vJkCS5mK";

    public static final String PEM_CODE_PRI = "";

    /** 初始化密钥工厂 */
    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyFactory = KeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private RsaUtils() {
    }

    public static synchronized Map<String, Object> generateKeyPair() {
        try {
            keyPairGenerator.initialize(KEY_SIZE,
                    new SecureRandom(UUID.randomUUID().toString().replaceAll("-", "").getBytes()));
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = encoder.encodeToString(rsaPublicKey.getEncoded());
        String privateKeyString = encoder.encodeToString(rsaPrivateKey.getEncoded());
        Map<String, Object> keyPairMap = new HashMap<String, Object>();
        keyPairMap.put("public", publicKeyString);
        keyPairMap.put("private", privateKeyString);
        return keyPairMap;
    }

    public static PublicKey getPublicKey(String pubKey) {
        try {
            byte[] keyBytes = decoder.decode(pubKey);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKey(String priKey) {
        try {
            byte[] keyBytes = decoder.decode(priKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByPublic(byte[] content, PublicKey publicKey) {
        if (publicKey == null) {
            publicKey = (PublicKey) getPublicKey(PEM_CODE_PUB);
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //该密钥能够加密的最大字节长度
            int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
            byte[][] arrays = splitBytes(content, splitLength);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte[] array : arrays) {
                stringBuffer.append(Base64Utils.encodeToString(cipher.doFinal(array)));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByPrivate(byte[] content, PrivateKey privateKey) {
        if (privateKey == null) {
            privateKey = (PrivateKey) getPrivateKey(PEM_CODE_PRI);
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            //该密钥能够加密的最大字节长度
            int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8 - 11;
            byte[][] arrays = splitBytes(content, splitLength);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte[] array : arrays) {
                stringBuffer.append(Base64Utils.encodeToString(cipher.doFinal(array)));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByPrivate(String content, PrivateKey privateKey) {
        if (privateKey == null) {
            privateKey = (PrivateKey) getPrivateKey(PEM_CODE_PRI);
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //该密钥能够加密的最大字节长度
            int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
            byte[] contentBytes = Base64Utils.decodeFromString(content);
            byte[][] arrays = splitBytes(contentBytes, splitLength);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte[] array : arrays) {
                stringBuffer.append(new String(cipher.doFinal(array)));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[][] splitBytes(byte[] bytes, int splitLength) {
        //bytes与splitLength的余数
        int remainder = bytes.length % splitLength;
        //数据拆分后的组数，余数不为0时加1
        int quotient = remainder != 0 ? bytes.length / splitLength + 1
                : bytes.length / splitLength;
        byte[][] arrays = new byte[quotient][];
        byte[] array = null;
        for (int i = 0; i < quotient; i++) {
            //如果是最后一组（quotient-1）,同时余数不等于0，就将最后一组设置为remainder的长度
            if (i == quotient - 1 && remainder != 0) {
                array = new byte[remainder];
                System.arraycopy(bytes, i * splitLength, array, 0, remainder);
            } else {
                array = new byte[splitLength];
                System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
            }
            arrays[i] = array;
        }
        return arrays;
    }

    public static void main(String[] args) {
        Map<String, Object> stringObjectMap = generateKeyPair();
        String aPublic = encryptByPublic("123123sdfdsdfds".getBytes(StandardCharsets.UTF_8), getPublicKey(stringObjectMap.get("public").toString()));
        String aPrivate = decryptByPrivate(aPublic, getPrivateKey(stringObjectMap.get("private").toString()));
        System.out.println(aPrivate);
    }

}
