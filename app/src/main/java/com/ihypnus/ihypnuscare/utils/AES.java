//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ihypnus.ihypnuscare.utils;

import android.text.TextUtils;
import android.util.Base64;

import com.wenming.library.encryption.IEncryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES implements IEncryption {
    private static final String UTF_8 = "UTF-8";
    private static final String TAG = "AES";
    public static String DEFAULT_KEY = "De@2%db@k+i)Ve=b";

    public AES() {
    }

    public String encrypt(String conetent) throws Exception {
        return this.encrypt(conetent, DEFAULT_KEY);
    }

    public String encrypt(String sSrc, String sKey) {
        try {
            if(TextUtils.isEmpty(sKey)) {
                return null;
            } else if(sKey.length() != 16) {
                return null;
            } else {
                byte[] raw = new byte[0];
                raw = sKey.getBytes("utf-8");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(1, skeySpec);
                byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
                return ConvertUtils.bytes2HexString(encrypted);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public String decrypt(String sSrc, String sKey) {
        try {
            if(TextUtils.isEmpty(sKey)) {
                return null;
            } else if(sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            } else {
                byte[] raw = sKey.getBytes("UTF-8");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(2, skeySpec);
                byte[] encrypted1 = Base64.decode(sSrc, 0);

                try {
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original, "UTF-8");
                    return originalString;
                } catch (Exception var9) {
                    return null;
                }
            }
        } catch (Exception var10) {
            return null;
        }
    }

    public String decrypt(String content) throws Exception {
        return this.decrypt(content, DEFAULT_KEY);
    }

    public static void main(String[] args) throws Exception {
    }
}
