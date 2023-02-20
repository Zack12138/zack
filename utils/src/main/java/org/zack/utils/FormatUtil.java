package org.zack.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

    private FormatUtil(){}


    static{
        //初始化 将接口实现类载入map

    }

    public static String parseURL(String url){

        url = parseEscapeChar(url);

        return url;
    }


    /**
     * 转移自定字符
     * @param str
     * @return
     */
    private static String parseEscapeChar(String str){

        String pattern = "\\{(\\w*?)-?(\\w*?)\\}";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        Matcher m  = r.matcher(str);

        if(m.find()){
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            System.out.println(m.group(2));
        }else {
            System.out.println("未匹配");
        }

        return str;
    }

    public static void main(String[] args) {
        parseEscapeChar("123{data-qwe}456");
    }

}
