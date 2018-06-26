package com.ihypnus.ihypnuscare.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {

    private static final String TAG = "StringUtils";

    public static boolean isNullOrEmpty(Object value) {
        return (value == null) || ("".equals(value)) || (" ".equals(value)) || ("null".equals(value) || value.toString().length() == 0);
    }

    public static boolean isNullOrEmpty1(Object value) {
        return (value == null) || ("".equals(value)) || (" ".equals(value)) || ("null".equals(value) || value.toString().length() == 0 || ("0.00".equals(value)) || ("0".equals(value)) || ("0.0".equals(value)));
    }

    public static boolean isNullOrEmpty2(Object value) {
        return (value == null) || ("".equals(value)) || (" ".equals(value)) || ("null".equals(value) || value.toString().length() == 0 || ("0.00".equals(value)) || ("0".equals(value)) || ("0.0".equals(value)) || ("¥0.00".equals(value)) || ("¥0".equals(value)) || ("¥0.0".equals(value)));
    }


    public static boolean isTimeOk(String textStr) {

        return false;
    }

    /**
     * 验证手机格式
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean isPrefixPhoneNum(String mobiles) {
        String telRegex = "(0|86|17951)?[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 同时判断是否为固定电话或手机号码
     */
    public static boolean validationPhone(String fixedPhone) {
        if (fixedPhone.equals("4008-098-098") || fixedPhone.equals("4008098098")) {
            return true;
        } else {
            Pattern p = Pattern.compile("(^(0[0-9]{2,3})?([2-9][0-9]{6,7})$|(^([1][34578])\\d{9}$))");
            Matcher m = p.matcher(fixedPhone);
            return m.matches();
        }
    }

    /**
     * 同时判断是否为固话或手机号码
     */
    public static boolean isPhoneNum(String fixedPhone) {

        Pattern p = Pattern.compile("(^(0[0-9]{2,3})?([2-9][0-9]{6,7})$|(^([1][34578])\\d{9}$))");
        Matcher m = p.matcher(fixedPhone);
        return m.matches();
    }

    /**
     * 判断当前号码是否可以拨打
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;

        isValid = isMobileNO(phoneNumber);
        if (!isValid) {
            isValid = isPhoneNumberEmergencyPhone(phoneNumber);
            if (!isValid) {
                isValid = isOperatorNumber(phoneNumber);
            }
        }
        return isValid;
    }


    // 判断是否是紧急求救电话号码
    public static boolean isPhoneNumberEmergencyPhone(String phoneNumber) {
        boolean isValid = false;
        if (phoneNumber == null || phoneNumber.equals("")) {
            return isValid;
        }

        String number = phoneNumber.toString().trim();

        if (number.equals("999") || number.equals("112")
                || number.equals("110") || number.equals("120")
                || number.equals("119") || number.equals("122")) {

            isValid = true;
            return isValid;
        }

        return isValid;
    }

    // 判断是否是三大电信运营商客服电话
    public static boolean isOperatorNumber(String phoneNumber) {
        boolean isValid = false;
        if (phoneNumber == null || phoneNumber.equals("")) {
            return isValid;
        }

        String number = phoneNumber.toString().trim();

        if (number.equals("10086") || number.equals("10010")
                || number.equals("10000")) {
            isValid = true;
            return isValid;
        }
        return isValid;
    }

    /**
     * 中文转码
     *
     * @param str
     * @return
     */
    public static String urlEncoder(String str) {
        if (isNullOrEmpty(str))
            return "";
        String ret = "";
        try {
            ret = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            LogOut.d(TAG, "error==" + e.toString());
        }
        return ret;
    }

    /**
     * 中文解码
     *
     * @param str
     * @return
     */
    public static String urlDecoder(String str) {
        if (isNullOrEmpty(str))
            return "";
        String ret = "";
        try {
            ret = URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            LogOut.d(TAG, "error==" + e.toString());
        }
        return ret;
    }

    public static String list2Str(ArrayList<String> list) {
        String result = ""; // 's','s','s'
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(",");
            sb.append(s);
        }
        if (sb != null) {
            result = sb.deleteCharAt(0).toString();
        }
        LogOut.d(TAG, "list2Str:" + result);
        return result;
    }


    public static long convertString2long(String val) {
        long result = 0;
        if (isNullOrEmpty(val))
            return result;
        try {
            result = Long.parseLong(val);
        } catch (NumberFormatException e) {
        }

        return result;
    }

    /**
     * 得到文件名
     *
     * @param url
     * @return
     */
    public static String getFileNa(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }

    /**
     * 得到文件後綴名
     *
     * @param url
     * @return
     */
    public static String getFileEx(String url) {
        return url.substring(url.lastIndexOf("."));
    }

    public static boolean removeFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean bool = file.delete();
            LogOut.i("deletebool", "delete" + bool);
            return bool;
        }
        return false;
    }

    /**
     * 截取字符串
     *
     * @param arrStr
     * @param mSplit 截取表示 ，注意 转义 \\
     * @param i
     * @return
     */
    public static String getSplitStr(String arrStr, String mSplit, int i) {

        String result = "";
        if (arrStr != null && !"".equals(arrStr)) {
            String[] tempStr = arrStr.split(mSplit);
            if (tempStr != null && tempStr.length > i) {
                result = tempStr[i];
            }
        }
        return result;
    }


    /**
     * String 转 Double
     */
    public static double convertString2Double(String val) {
        double result = 0;
        if (isNullOrEmpty(val))
            return result;
        try {
            result = Double.parseDouble(val);
        } catch (NumberFormatException e) {
        }

        return result;
    }

    /**
     * 限定字符长度
     *
     * @param content     源字符串
     * @param checkLength 限定长度 如没有限定长度 传 0
     * @return 返回 限定长度字符串 +" .... "
     */
    public static String checkStringLength(String content, int checkLength) {
        String result = "";
        if (content == null || content.equals("null")) {
            return result;
        }

        if (content.length() > checkLength) {
            if (checkLength > 0) {
                result = content.substring(0, checkLength) + "...";
            } else {
                result = result + "...";
            }
        } else {
            result = content == null ? "" : content;
        }
        return result;
    }

    /**
     * 正则表达式 判断是否纯数字
     */
    public static int ContentType(String content) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 字符串转Double类型
     */
    public static double parseDouble(String content) {
        double d = 0.0;
        if (content == null || content.equals("")) {
            return d;
        }

        try {
            d = Double.parseDouble(content);
        } catch (Exception e) {
            LogOut.e(TAG, content + "parseDouble Exception");
        }
        return d;
    }

    /**
     * 判断字符串是否可用 返回空字符串或字符串本身
     */
    public static String checkStringIsNull(String content) {
        if (content == null || content.equals("null") || content.equals("Null")) {
            return "";
        } else {
            return content;
        }
    }

    /**
     * 获取文件名称并进行中文转码 utf-8
     */
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        String fileName = urlEncoder(file.getName());
        return fileName;
    }

    ;

    public static String convertInteger2String(int val) {
        if (val == 0) {
            return "";
        }
        return String.valueOf(val);
    }

    public static String convertDouble2String(double val) {
        if (val == 0) {
            return "";
        }
        return String.valueOf(val);
    }

    public static int convertString2Integer(String val) {
        int result = 0;
        if (isNullOrEmpty(val))
            return result;
        try {
            result = Integer.parseInt(val);
        } catch (NumberFormatException e) {
        }

        return result;
    }


    // 替换、过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {
        String filterStr = "";
        Matcher m = null;
        try {
            str = str.replaceAll("【", "[").replaceAll("】", "]")
                    .replaceAll("！", "!");// 替换中文标号
            String regEx = "[『』]"; // 清除掉特殊字符
            Pattern p = Pattern.compile(regEx);
            m = p.matcher(str);
            filterStr = m.replaceAll("").trim();
        } catch (PatternSyntaxException e) {
            LogOut.d(TAG, "error==" + e.toString());
        } catch (Exception e) {
            LogOut.d(TAG, "error==" + e.toString());
        }
        return filterStr;
    }

    // 将textview中的字符全角化。即将所有的数字、字母及标点全部转为全角字符，
    // 使它们与汉字同占两个字节，这样就可以避免由于占位导致的排版混乱问题
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /**
     * 检查是否有特殊字符串
     *
     * @param expressNum
     * @return
     */
    public static boolean hasSpecialCharacters(String expressNum) {
        if (TextUtils.isEmpty(expressNum)) {
            return false;
        }
        String regularExpression = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regularExpression);
        Matcher m = p.matcher(expressNum);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ^ 匹配一行的开头位置
     * (?![0-9]+$) 预测该位置后面不全是数字
     * (?![a-zA-Z]+$) 预测该位置后面不全是字母
     * [0-9A-Za-z] {6,12} 由6-12位数字或这字母组成
     * `~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？ 允许输入特殊字符
     */
    public static boolean Passwordcheck(String pwd) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[`~!@#$^&*()=|{}':;',\\\\[\\\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？0-9A-Za-z]{6,15}$";
        return pwd.matches(regex);
    }

    //纯中文判断
    public static boolean checkName(String s) {
        s = new String(s.getBytes());//用GBK编码
        String pattern = "[\u4e00-\u9fa5]+";
        Pattern p = Pattern.compile(pattern);
        Matcher result = p.matcher(s);
        return result.matches();
    }

    //地址判断中文，英文，数字
    public static boolean checkAdressName(String s) {
        s = new String(s.getBytes());//用GBK编码
        String pattern = "[()-—a-zA-Z0-9\\u4e00-\\u9fa5（）]+";
        Pattern p = Pattern.compile(pattern);
        Matcher result = p.matcher(s);
        return result.matches();
    }

    //身份证号码验证
    public static boolean checkIdNumber(String s) {
//        s = new String(s.getBytes());//用GBK编码
        String pattern = "(^\\d{15}$)|(^\\d{17}([0-9]|X|x)$)";
        Pattern p = Pattern.compile(pattern);
        Matcher result = p.matcher(s);
        return result.matches();
    }

    //身份证号码验证
    public static boolean checkIdCard(String s) {
//        s = new String(s.getBytes());//用GBK编码
        String pattern = "([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x))";
        Pattern p = Pattern.compile(pattern);
        Matcher result = p.matcher(s);
        return result.matches();
    }

    //    //公司名称判断中文，英文，数字
//    public static boolean checkCompanyName(String s) {
//        s = new String(s.getBytes());//用GBK编码
//        String pattern = "[()a-zA-Z0-9\\u4e00-\\u9fa5（）]+";
//        Pattern p = Pattern.compile(pattern);
//        Matcher result = p.matcher(s);
//        return result.matches();
//    }
    //    public boolean checkfilename(String s){
//        s=new String(s.getBytes());//用GBK编码
//        String pattern="[\u4e00-\u9fa5]+";
//        Pattern p=Pattern.compile(pattern);
//        Matcher result=p.matcher(s);
//        return result.find(); //是否含有中文字符
//    }
    public static boolean hasFullChar(String str) {
        if (str.getBytes().length == str.length()) {
            return false;
        }
        return true;
    }

    public static boolean isLetterAndNum(String str) {
        String strExp = "/^[A-Za-z0-9]+$/";
        return strExp.matches(str);
    }

    //包含中文
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为纯字母
     *
     * @param str
     * @return
     */
    public static boolean isChart(String str) {
        Pattern pattern = Pattern.compile("[A-za-z]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 密码检测
     *
     * @param passwrod
     * @return
     */
    public static String checkPasswrod(String passwrod) {
        String error = "";
        //第一步判断 是否是数字+英文 并且长度在6-12位之间
        boolean isSplit = StringUtils.Passwordcheck(passwrod);
        int type = 0; //type =0 数字 type =1 字符
        if (StringUtils.isNumeric(passwrod.substring(0, 1))) {
            type = 0;
        } else if (StringUtils.isChart(passwrod.substring(0, 1))) {
            type = 1;
        }
        //第二步 遍历字符串 看同一类型的字符是否超过3个
        int count = 0;
        if (isSplit) {
            for (int i = 0; i < passwrod.length(); i++) {
                if (count > 3) {
                    break;
                }
                String s1 = passwrod.substring(i, i + 1 >= passwrod.length() - 1 ? i : i + 1);
                //如果为数字类型 则判断为同一类型的字符 加1
                int t = 0;
                if (StringUtils.isNumeric(s1)) {
                    t = 0;
                } else if (StringUtils.isChart(s1)) {
                    t = 1;
                }
                //如果上个字符类型与当前类型一直 则相同类型数量+1 否则清零
                if (type == t) {
                    count++;
                } else {
                    count = 0;
                }
                type = t;
            }
        }
        if (count > 2) {
            error = "密码字符 不能连续输入字母或数字 3个以上";
        }
        return error;
    }


    // 过滤特殊字符
    public static String StringFilter2(String str) throws PatternSyntaxException {
        try {
            // 只允许字母和数字
            // String   regEx  =  "[^a-zA-Z0-9]";
            // 清除掉所有特殊字符
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch (Exception e) {
            LogOut.d(TAG, "error==" + e.toString());
        }
        return str;
    }

    /**
     * 判断是否包含特殊字符
     *
     * @param content
     * @return
     */
    public static boolean hasSpecialString(CharSequence content) {
        String regEx = "[`~!@#$%^&*()+=|{}\"':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？√ ×]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return m.find();
    }

    /**
     * 公司名称限制限制只能输入英文符号的“- / ()”与中文符号的“（）”
     *
     * @param content
     * @return
     */
    public static boolean hasSpecialString1(CharSequence content) {
        String regEx = "[- /()（）]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return m.find();
    }

    public static String removeAllSpace(String content) {
        return content.trim().replace(" ", "");
    }

    /**
     * 添加后台--客户资料--公司名称--判断是否包含特殊字符
     * 公司名称限制限制只能输入英文符号的“- / ()”与中文符号的“（）”
     *
     * @param content
     * @return
     */
    public static boolean hasIllegalString(String content) {
        char[] chars = content.toCharArray();
        for (char aChar : chars) {
            if (isSpaceOnly(aChar + "")) {
                return true;
            }
            if (StringUtils.isChineseOnly(aChar + "") || StringUtils.isNumber(aChar + "") || StringUtils.isContainEnglish(aChar + "") || StringUtils.hasSpecialString1(aChar + "")) {

            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤输入第三方表情
     *
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return ((codePoint >= 0xA0) && (codePoint <= 0xFF)) || !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xEFFF)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x11FFFF)) ||
                ((codePoint >= 0x20FF) && (codePoint <= 0x32FF));
    }

    /**
     * 判断用户输入的内容是否非法
     *
     * @param input
     * @return true —— 非法   false —— 正常
     */
    public static boolean isInputIllegal(String input) {
//        String regEx = "[/\"“”$&＆%％%:：？?@<>||*￥$\\\\]";
        String regEx = "[\"\'\\\\''<>&%{}]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 判断用户输入的内容是否非法   增加烦斜杠
     *
     * @param input
     * @return true —— 非法   false —— 正常
     */
    public static boolean isInputIllegal2(String input) {
//        String regEx = "[/\"“”$&＆%％%:：？?@<>||*￥$\\\\]";
        String regEx = "[\"\'\\\\/''<>&%{}]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 判断用户输入的内容是否是字母或数字
     *
     * @param input
     * @return true —— 是   false —— 不是
     */
    public static boolean isInputCharOrNum(String input) {
        String regEx = "[a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 判断用户输入的内容是否是汉字/字母或数字
     *
     * @param input
     * @return true —— 是   false —— 不是
     */
    public static boolean isInputCarNum(String input) {
        String regEx = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 判断用户输入的内容是否是包含字母
     *
     * @param input
     * @return true —— 是   false —— 不是
     */
    public static boolean isContainEnglish(String input) {
        String regEx = "[a-zA-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 判断用户输入的内容是否是包含数字
     *
     * @param input
     * @return true —— 是   false —— 不是
     */
    public static boolean isContainNum(String input) {
        String regEx = "[0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }


    /**
     * 判断用户输入的内容是否只含有换行符/n
     *
     * @param input
     * @return true -是  false-否
     */
    public static boolean isWrapOnly(String input) {
        String regEx = "[\n]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isChineseOrAlphabet(String input) {
        Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z]+$");
        Matcher isChineseOrAlphabet = pattern.matcher(input);
        if (!isChineseOrAlphabet.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 是否只是汉字
     *
     * @param input
     * @return
     */
    public static boolean isChineseOnly(String input) {
        Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
        Matcher isChinese = pattern.matcher(input);
        return isChinese.matches();
    }

    public static boolean isSpaceOnly(String input) {
        String regEx = "[ ]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 字符串组合（积分充值Dialog有用到）
     * 不用的参数可以传“”
     *
     * @param str1
     * @param str2
     * @param str3
     * @param str4
     * @param str5
     * @return
     */
    public static String patternStr(String str1, String str2, String str3, String str4, String str5) {
        String str = new StringBuilder().append(str1).append(str2).append(str3).append(str4).append(str5).toString();
        return str;
    }

    /**
     * 获取字符串中所有数字
     *
     * @param account
     * @return
     */
    public static String getNumbers(String account) {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(account);
        String result = "";
        while (matcher.find()) {
            result += matcher.group();
        }
        return result;
    }

    /**
     * xxfff add 把英文特殊字符替换成中文字符
     *
     * @param editStr
     * @return
     */
    public static String getReplaceStr(String editStr) {
        if (editStr.trim().length() > 0) {
            if (editStr.indexOf("&") != -1) {
                editStr = editStr.replaceAll("&", "＆");
            }
            if (editStr.indexOf("\\'") != -1) {
                editStr = editStr.replaceAll("\'", "′");
            }
            if (editStr.indexOf("\"") != -1) {
                editStr = editStr.replaceAll("\"", "〃");
            }
            if (editStr.indexOf("\"\"") != -1) {
                editStr = editStr.replaceAll("\"\"", "〃〃");
            }
            if (editStr.indexOf("<") != -1) {
                editStr = editStr.replaceAll("<", "＜");
            }
            if (editStr.indexOf(">") != -1) {
                editStr = editStr.replaceAll(">", "＞");
            }
            if (editStr.indexOf("/") != -1) {
                editStr = editStr.replaceAll("/", "╱");
            }
            if (editStr.indexOf("\\") != -1) {
                editStr = editStr.replaceAll("\\\\", "╲");
            }
            if (editStr.indexOf("%") != -1) {
                editStr = editStr.replaceAll("%", "％");
            }
        }
        return editStr;
    }

    /**
     * 从字符串中读取web地址
     *
     * @param content
     * @return
     */
    public static ArrayList<String> getWebString(String content) {
        String regExp = "(http|https)://([\\w-]+\\.)+[a-zA-Z0-9]+(/[a-zA-Z0-9/?%&=-~-]*)?(\\.[a-z]+)?";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(content);
        ArrayList<String> webList = new ArrayList<>();
        while (m.find()) {
            String webString = m.group();
            if (!webList.contains(webString)) {
                webList.add(webString);
            }
        }
        return webList;
    }

    /**
     * 将手机号码中间四位换为*号，如果固定电话，返回"8*"
     *
     * @param telephoneNum
     * @return
     */
    public static String transformTel(String telephoneNum) {
//        if (isMobileNO(telephoneNum))  //手机号码中间四位换为*号
//            return telephoneNum.substring(0, 3) + "****" + telephoneNum.substring(7, telephoneNum.length());
//        else if (validationPhone(telephoneNum)) { //固定电话，返回"8*"
//            return "********";
//        }
        if (validationPhone(telephoneNum)) { //号码中间四位换为*号
            return telephoneNum.substring(0, 3) + "****" + telephoneNum.substring(7, telephoneNum.length());
        }
        return telephoneNum;
    }

    /**
     * 将一串数字加密
     *
     * @param telephoneNum
     * @return
     */
    public static String transformTel2(String telephoneNum) {
        if (telephoneNum.length() > 10) { //号码中间四位换为*号
            return telephoneNum.substring(0, 3) + "****" + telephoneNum.substring(7, telephoneNum.length());
        }
        return telephoneNum;
    }

    /**
     * 去掉手机和电话号码中的*号
     *
     * @param telephoneNum
     * @return
     */
    public static String removeXing(String telephoneNum) {
        if (telephoneNum.contains("*")) {
            telephoneNum = telephoneNum.replace("*", "");
        }
        return telephoneNum;
    }

    /**
     * 验证邮箱的正则表达式
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {//
        String format = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        if (email.matches(format)) {
            return true;// 邮箱名合法，返回true
        } else {
            return false;// 邮箱名不合法，返回false
        }
    }

    /**
     * 长图Base64
     * xxfff add
     *
     * @param imgBase64Str
     * @return
     */
    public static String largImgBase64Str(String imgBase64Str) {
        String head = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "        <title>垂直居中</title>\n" +
                "        <style>\n" +
                "                body,html{\n" +
                "                        text-align:center;\n" +
                "                        margin:0;\n" +
                "                        padding:0;\n" +
                "                }\n" +
                "                body,html, div{\n" +
                "                        height:100%;\n" +
                "                        width:100%;\n" +
                "                }\n" +
                "                div:after,img{\n" +
                "                        vertical-align:middle;\n" +
                "                        display:inline-block;\n" +
                "                }\n" +
                "                div:after{\n" +
                "                        height:100%;\n" +
                "                        background:#c1c1c1;\n" +
                "                        content:' ';\n" +
                "                }\n" +
                "        </style>\n" +
                "</head>\n" +
                "<body>";

        String foot = "</body> \n" +
                "</html>";
        StringBuilder sbData = new StringBuilder();
        sbData.append(head);
        sbData.append("<div><img width = \"100%\" src= '");
        sbData.append("data:image/png;base64," + imgBase64Str);
        sbData.append("'/></div>");
        sbData.append(foot);
        return sbData.toString();
    }

    /**
     * 长图Base64--大图显示界面
     * xxfff add
     *
     * @param imgBase64Str
     * @return
     */
    public static String largImgBase64Str2(String imgBase64Str) {
        String head = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "        <title>垂直居中</title>\n" +
                "        <style>\n" +
                "                body,html{\n" +
                "                        text-align:center;\n" +
                "                        margin:0;\n" +
                "                        padding:0;\n" +
                "                }\n" +
                "                body,html, div{\n" +
                "                        height:100%;\n" +
                "                        width:100%;\n" +
                "                        background:#000000;\n" +
                "                }\n" +
                "                div:after,img{\n" +
                "                        vertical-align:middle;\n" +
                "                        display:inline-block;\n" +
                "                }\n" +
                "                div:after{\n" +
                "                        height:100%;\n" +
                "                        background:#c1c1c1;\n" +
                "                        content:' ';\n" +
                "                }\n" +
                "        </style>\n" +
                "</head>\n" +
                "<body>";

        String foot = "</body> \n" +
                "</html>";
        StringBuilder sbData = new StringBuilder();
        sbData.append(head);
        sbData.append("<div><img width = \"100%\" src= '");
        sbData.append("data:image/png;base64," + imgBase64Str);
        sbData.append("'/></div>");
        sbData.append(foot);
        return sbData.toString();
    }


    /**
     * 显示URL图片
     *
     * @param imgUrlStr
     * @return
     */
    public static String largImgUrlStr(String imgUrlStr) {
        String head = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "        <title>垂直居中</title>\n" +
                "        <style>\n" +
                "                body,html{\n" +
                "                        text-align:center;\n" +
                "                        margin:0;\n" +
                "                        padding:0;\n" +
                "                }\n" +
                "                body,html, div{\n" +
                "                        height:100%;\n" +
                "                        width:100%;\n" +
                "                }\n" +
                "                div:after,img{\n" +
                "                        vertical-align:middle;\n" +
                "                        display:inline-block;\n" +
                "                }\n" +
                "                div:after{\n" +
                "                        height:100%;\n" +
                "                        background:#c1c1c1;\n" +
                "                        content:' ';\n" +
                "                }\n" +
                "        </style>\n" +
                "</head>\n" +
                "<body>";

        String foot = "</body> \n" +
                "</html>";
        StringBuilder sbData = new StringBuilder();
        sbData.append(head);
        sbData.append("<div><img width = \"100%\" src= '" + imgUrlStr);
//        sbData.append("data:image/png;base64," + imgBase64Str);
        sbData.append("'/></div>");
        sbData.append(foot);
        return sbData.toString();
    }

    /**
     * 拼接字符串
     *
     * @param splice1
     * @param splice2
     * @param splice3
     * @return splice1-splice2-splice3
     */
    public static String spliceField(String splice1, String splice2, String splice3) {
        StringBuilder builder = new StringBuilder();
        builder = !TextUtils.isEmpty(splice1) ? builder.append(splice1) : builder;
        builder = !TextUtils.isEmpty(splice2) ? (!TextUtils.isEmpty(builder.toString()) ? builder.append("-").append(splice2) : builder.append(splice2)) : builder;
        builder = !TextUtils.isEmpty(splice3) ? (!TextUtils.isEmpty(builder.toString()) ? builder.append("-").append(splice3) : builder.append(splice3)) : builder;
        return builder.toString();//操作的内容
    }


    /**
     * 检测字符是否为全数字
     *
     * @param str 需要判断的字符
     * @return 是否为全数字
     */
    public static boolean isNumber(String str) {
        return str.matches("[0-9]+");
    }

    /**
     * 检测字符是否为车牌号
     *
     * @param str 需要判断的字符
     * @return 是否为车牌号
     */
    public static boolean isCarNum(String str) {
        String s = str.substring(0, 1);
        return s.matches("[a-zA-z]") && str.length() == 6;
    }

    /**
     * 判断是否普通7位车牌类型
     *
     * @param str 需要判断的车牌
     * @return
     */
    public static boolean isCommonCarNum(String str) {
        String regex = "^[京,津,渝,沪,冀,晋,辽,吉,黑,苏,浙,皖,闽,赣,鲁,豫,鄂,湘,粤,琼,川,贵,云,陕,秦,甘,陇,青,台,内蒙古,蒙,桂,宁,新,藏,澳,军,海,航,警][A-Z][0-9,A-Z]{5}$";
        return Pattern.compile(regex).matcher(str).matches();
    }

    /**
     * 检测字符是否为完整车牌号
     *
     * @param str 需要判断的字符
     * @return 是否为车牌号
     */
    public static boolean isCompleteCarNum(String str) {
        final boolean b1 = StringUtils.isContainChinese(str);
        final boolean b2 = StringUtils.isContainEnglish(str);
        final boolean b3 = StringUtils.isContainNum(str);
        if (b1 && b2 && b3) {
            return true;
        }
        return false;
    }

    /**
     * 全角字符串转换半角字符串
     *
     * @param text 非空的全角字符串
     * @return 半角字符串
     */
    public static String fullToHalf(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        char[] charArray = text.toCharArray();
        //对全角字符转换的char数组遍历
        for (int i = 0; i < charArray.length; ++i) {
            int charIntValue = (int) charArray[i];
            //如果符合转换关系,将对应下标之间减掉偏移量65248
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {     // 空格字符
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
    }

    /**
     * 拼接地址,由"/"隔开
     * 添加后台,五级地址
     *
     * @param province
     * @param city
     * @param area
     * @param down
     * @param street
     * @return
     */
    public static String slpliceAddress(String province, String city, String area, String down, String street) {
        StringBuilder builder = new StringBuilder();
        builder = !TextUtils.isEmpty(province) ? builder.append(province) : builder;
        builder = !TextUtils.isEmpty(city) ? (!TextUtils.isEmpty(builder.toString()) ? builder.append("/").append(city) : builder.append(city)) : builder;
        builder = !TextUtils.isEmpty(area) ? (!TextUtils.isEmpty(builder.toString()) ? builder.append("/").append(area) : builder.append(area)) : builder;
        builder = !TextUtils.isEmpty(down) ? (!TextUtils.isEmpty(builder.toString()) ? builder.append("/").append(down) : builder.append(down)) : builder;
        builder = !TextUtils.isEmpty(street) ? (!TextUtils.isEmpty(builder.toString()) ? builder.append("/").append(street) : builder.append(street)) : builder;
        return builder.toString();//操作的内容
    }

    /**
     * 请假加班模块,时间去掉年份
     *
     * @return
     */
    public static String displyPartTime(String time) {
        if (time.length() > 6) {
            return time.substring(5);
        }
        return time;
    }

    /**
     * 考试管理模块,获取年月
     *
     * @param time
     * @return
     */
    public static String getYearAndMonth(String time) {
        if (time.length() > 9) {
            return time.substring(0, 10);
        }
        return time;
    }

    public static String getParTime(String time) {
        if (time.length() >= 10) {
            return time.substring(0, 10);
        }
        return time;
    }

    /**
     * 格式化文件大小
     *
     * @param size
     */
    public static String formatFileSize(float size) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        if (size < 1024) {
            format.applyPattern("###,###,##0.00B");
        } else if (size >= 1024 && size < 1024 * 1024) {
            size /= 1024;
            format.applyPattern("###,###,##0.00KB");
        } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
            size /= (1024 * 1024);
            format.applyPattern("###,###,##0.00MB");
        } else if (size >= 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024) {
            size /= (1024 * 1024 * 1024);
            format.applyPattern("###,###,##0.00GB");
        } else if (size >= 1024 * 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024 * 1024) {
            size /= (1024 * 1024 * 1024 * 1024);
            format.applyPattern("###,###,##0.00GB");
        }
        return format.format(size);
    }

    /**
     * 验证输入的密码
     *
     * @param input
     * @return
     */
    public static boolean vertifyPassWord(String input) {
        Pattern p = Pattern.compile("^(?=.{6,14}$)[0-9a-zA-Z@!+-?]+$");
        Matcher m = p.matcher(input);
        return m.matches();

    }

    /**
     * 验证输入的密码
     *
     * @param input
     * @return
     */
    public static boolean vertifyIllegal(String input) {
        Pattern p = Pattern.compile("^[0-9a-zA-Z@!+-?]+$");
        Matcher m = p.matcher(input);
        return m.matches();

    }
}
