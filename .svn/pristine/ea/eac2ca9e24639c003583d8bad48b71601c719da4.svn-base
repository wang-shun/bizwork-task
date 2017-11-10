package com.sogou.bizwork.task.api.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * 资源文件工具类。
 * 
 * @author shenzongqiang
 *
 */
@Component
public final class MessageUtils {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key, Object... params) {
        return messageSource.getMessage(key, params, Locale.getDefault());
    }

    public static String printStackTrace(Exception e) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        sb.append("class: ").append(stacks[1].getClassName())
                .append("; method: ").append(stacks[1].getMethodName())
                .append("; line: ").append(stacks[1].getLineNumber())
                .append("\r\n");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sb.append(sw);
        return sb.toString();
    }

    public static String getTraceInfo() {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        sb.append("class: ").append(stacks[1].getClassName())
                .append("; method: ").append(stacks[1].getMethodName())
                .append("; line: ").append(stacks[1].getLineNumber());
        return sb.toString();
    }
}
