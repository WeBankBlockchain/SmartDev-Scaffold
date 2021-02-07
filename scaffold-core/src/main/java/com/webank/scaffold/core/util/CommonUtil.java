package com.webank.scaffold.core.util;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/26
 */
public class CommonUtil {
    private CommonUtil(){}


    public static String makeFirstCharUpperCase(StringBuilder s){
        char ch = s.charAt(0);
        s.setCharAt(0, Character.toUpperCase(ch));
        return s.toString();
    }
}
