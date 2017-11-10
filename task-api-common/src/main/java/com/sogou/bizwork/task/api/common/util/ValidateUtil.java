package com.sogou.bizwork.task.api.common.util;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class ValidateUtil {

    public static final String[] illegalChars = new String[] { "\\", "|", "｜", "^", "$", "?", "？", "［", "］", "{",
            "｛", "}", "｝", "\\\\", "＼", "￥", ",", "，", "；", ";", "@", "＠", "。", "：", ":", "！", "!", "、", "'", "‘", "’",
            "“", "”", "\"", "=", "【", "】", "<", ">", "＜", "＞", "《", "》", "~", "～", "•", "•", "\u0003", "\u0005",
            "©", "®", "?", "×", "÷", "+" };

    public static final String httpurl = "^(https|http)(://).*";

    /**
     * 判断是否为全角字符或中文字符
     * 
     * @param c
     * @return
     */
    public static boolean isChineseOrFullWidthCharacter(char c) {
        if (("" + c).getBytes().length > 1) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串的字节数是否符合要求
     * 
     * @param string
     * @param min
     * @param max
     * @return
     */
    public static boolean isStringBytesLengthRight(String string, int min, int max) {
        int count = 0;
        if (string != null) {
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (isChineseOrFullWidthCharacter(chars[i])) {
                    count += 2;
                } else {
                    count += 1;
                }
            }
        }
        if (count >= min && count <= max) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否包含限制字符
     * @param string
     * @return
     */
    public static boolean hasIllegalChar(String string) {
        for (String illegalChar : illegalChars) {
            if (string.contains(illegalChar)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断金额是否在给定区间内
     * @param num
     * @param min
     * @param max
     * @return
     */
    public static boolean isNumberRight(BigDecimal num, BigDecimal min, BigDecimal max) {
        if (num.compareTo(min) >= 0 && num.compareTo(max) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断Float类型数值大小
     * @param num
     * @param min
     * @param max
     * @return
     */
    public static boolean isNumberRight(Float num, Float min, Float max) {
        if (num.compareTo(min) >= 0 && num.compareTo(max) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断URL格式是否正确
     */
    public static boolean isUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        if (!url.matches(httpurl)) {
            return false;
        }
        if (URLUtils.getLookupUrl(url) != null) {
            return true;
        } else {
            return false;
        }
    }

}
