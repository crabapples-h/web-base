package cn.crabapples.utils;

/**
 * TODO 字符串工具类
 *
 * @author Mr.He
 * @date 2019/7/23 23:16
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
public class StringUtils {
    /**
     * 将字符串首字母转换为大写
     * @param string 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String stringFirstCharToUpcase(String string){
        char [] chars = string.toCharArray();
        if(chars[0] >= 97 && chars[0] <= 122){
            chars[0] -= 32;
        }
        return String.valueOf(chars);
    }
}
