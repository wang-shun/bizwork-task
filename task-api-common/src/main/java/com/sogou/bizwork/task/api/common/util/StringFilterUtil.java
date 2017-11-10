package com.sogou.bizwork.task.api.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * 处理物料中的特殊字符
 * 
 * @author qianlei
 */
public class StringFilterUtil {

    private static final char[] specialChars = new char[] { '\3', '\7', '\t', '\b', '\r', '\f' };

    public static String filter(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 去除非法字符
        for (char c : specialChars) {
            str = str.replaceAll(String.valueOf(c), "");
        }
        // 去除前后空格
        str = str.trim();
        
        return str;
    }
}