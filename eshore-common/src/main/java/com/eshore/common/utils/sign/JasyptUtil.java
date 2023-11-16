package com.eshore.common.utils.sign;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Jasypt加密类
 *
 * @author liuan
 */
public class JasyptUtil {

    /**
     * @return
     */
    public static String encryptPass(String pass, String value) {
        PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
        encryptOr.setConfig(cryptOr(pass));
        String result = encryptOr.encrypt(value);
        return result;
    }

    /**
     * @return
     */
    public static String decyptPass(String pass, String value) {
        PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
        encryptOr.setConfig(cryptOr(pass));
        String result = encryptOr.decrypt(value);
        return result;
    }

    /**
     * @return
     */
    public static PBEConfig cryptOr(String pass) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(pass);
        config.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
//        config.setProviderName(null);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    public static void main(String[] args) {
        // 加密
        // 盐值替换成自己熟悉的口令，此口令为解密密钥，需要妥善保管。
        System.out.println(encryptPass("jtsb", "root"));
        System.out.println(encryptPass("jtsb", "841009831017!qaZ"));

        System.out.println(encryptPass("jtsb", "121212aB!"));

        System.out.println(encryptPass("jtsb", "s@LRd21iK"));
        System.out.println(encryptPass("jtsb", "Eshore2018!23"));
        System.out.println(encryptPass("jtsb", "hwqwsipdezgufebb"));

        System.out.println(encryptPass("jtsb", "3KOf6EVW"));

        System.out.println(encryptPass("jtsb", "!3Wr@e28.*"));

        System.out.println(encryptPass("jtsb", "123456"));
        System.out.println(encryptPass("jtsb", "GmY9e1M9"));

        System.out.println(decyptPass("jtsb","uUFNImw0rzgvQwoUTVxZmeVs1Wgsxy8d"));

        System.out.println(encryptPass("jtsb", "vQjkKB1D"));
        System.out.println(decyptPass("jtsb", "O+JraTQjHDhz1uKcCLN4QQ=="));
        System.out.println(decyptPass("jtsb", "ddaAkISmT5pEU1Pwu/gsnJsdTx62N3j2"));




        System.out.println(decyptPass("jtsb", "gbzrQ6NFZJ2a8sozvzVM9lsh2EQ2p3bRozHtiKVVAm0="));

    }


}

