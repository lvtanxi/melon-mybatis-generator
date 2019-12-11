package com.melon.mybatis.generator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 2018-08-08
 * Time: 17:36
 * Description:
 */
public class StringUtil {


    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static String DOT = ".";


    private StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    public static String convertToJava(boolean isTable, String dbKey) {
        String hump = lineToHump(dbKey.toLowerCase());
        if (isTable) {
            hump = captureName(hump);
        }
        return hump;
    }

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return
     */
    private static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
    /**
     * 将字符串的首字母转小写
     *
     * @param str 需要转换的字符串
     * @return
     */
    public static String lowerName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     */
    private static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     */
    public static String line(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "/"+matcher.group(1));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线,效率比上面高
     */
    private static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        String lineToHump = "test_parent_no_leader";
        System.out.println(line(lineToHump));// f_parent_no_leader
    }


}
