package com.ihypnus.ihypnuscare.utils;

import android.text.TextUtils;

/**
 * @Package com.ihypnus.ihypnuscare.utils
 * @author: llw
 * @Description:
 * @date: 2018/6/11 19:03
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ConvertUtils {
    static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String bytes2HexString(byte[] bytes) {
        if(bytes == null) {
            return null;
        } else {
            int len = bytes.length;
            if(len <= 0) {
                return null;
            } else {
                char[] ret = new char[len << 1];
                int i = 0;

                for(int var4 = 0; i < len; ++i) {
                    ret[var4++] = hexDigits[bytes[i] >>> 4 & 15];
                    ret[var4++] = hexDigits[bytes[i] & 15];
                }

                return new String(ret);
            }
        }
    }

    public static byte[] hexString2Bytes(String hexString) {
        if(TextUtils.isEmpty(hexString)) {
            return null;
        } else {
            int len = hexString.length();
            if(len % 2 != 0) {
                hexString = "0" + hexString;
                ++len;
            }

            char[] hexBytes = hexString.toUpperCase().toCharArray();
            byte[] ret = new byte[len >> 1];

            for(int i = 0; i < len; i += 2) {
                ret[i >> 1] = (byte)(hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
            }

            return ret;
        }
    }

    private static int hex2Dec(char hexChar) {
        if(hexChar >= 48 && hexChar <= 57) {
            return hexChar - 48;
        } else if(hexChar >= 65 && hexChar <= 70) {
            return hexChar - 65 + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] chars2Bytes(char[] chars) {
        if(chars != null && chars.length > 0) {
            int len = chars.length;
            byte[] bytes = new byte[len];

            for(int i = 0; i < len; ++i) {
                bytes[i] = (byte)chars[i];
            }

            return bytes;
        } else {
            return null;
        }
    }

    public static char[] bytes2Chars(byte[] bytes) {
        if(bytes == null) {
            return null;
        } else {
            int len = bytes.length;
            if(len <= 0) {
                return null;
            } else {
                char[] chars = new char[len];

                for(int i = 0; i < len; ++i) {
                    chars[i] = (char)(bytes[i] & 255);
                }

                return chars;
            }
        }
    }
}
