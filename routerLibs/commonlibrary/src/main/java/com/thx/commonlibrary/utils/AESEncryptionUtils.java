package com.thx.commonlibrary.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: 带偏移量
 * @Author: tanghongxiang
 * @Version: V1.00
 * @since 2021/10/20 3:43 下午
 */
public class AESEncryptionUtils {

    //偏移量
    public static final String VIPARA = "i996needMoney996";
    //编码方式
    public static final String CODE_TYPE = "UTF-8";
    //填充类型（注：CBC）<算法/模式/补码方式>
    public static final String AES_TYPE = "AES/CBC/PKCS7Padding";
    //私钥,AES固定格式为128/192/256 bits.即：16/24/32bytes
    private static final String AES_KEY = "18B49DE86CAD4401";

    /**
     * 加密带偏移量
     */
    public static String encrypt(String cleartext) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes(CODE_TYPE));
            return Base64.encodeToString(encryptedData, Base64.DEFAULT).replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密带偏移量
     *
     * @param encrypted 密文
     * @param ips       偏移量
     * @return 解密后的明文
     */
    public static String decrypt(String encrypted, String ips) {
        if (TextUtils.isEmpty(ips)) return encrypted;
        try {
            byte[] byteMi = Base64.decode(encrypted, Base64.DEFAULT);
            IvParameterSpec zeroIv = new IvParameterSpec(ips.getBytes());
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, CODE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * JUST TEST
     * @param content
     * @return
     */
    public static String aesEncryption(String content) {
        // 对称加密数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tyg", AESEncryptionUtils.VIPARA);
            jsonObject.put("cipherText", AESEncryptionUtils.encrypt(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
