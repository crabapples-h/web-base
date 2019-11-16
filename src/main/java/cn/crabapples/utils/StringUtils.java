package cn.crabapples.utils;

import cn.crabapples.utils.random.CreateRandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO 字符串工具类
 *
 * @author Mr.He
 * @date 2019/7/23 23:16
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
    /**
     * 将字符串首字母转换为大写
     * @param source 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String firstCharToUpcase(String source){
        logger.debug("需要将首字母转换为大写的字符串:[{}]",source);
        char [] chars = source.toCharArray();
        if(chars[0] >= 97 && chars[0] <= 122){
            chars[0] -= 32;
        }
        String target = String.valueOf(chars);
        logger.debug("转换后的字符串:[{}]",target);
        return target;
    }
}
