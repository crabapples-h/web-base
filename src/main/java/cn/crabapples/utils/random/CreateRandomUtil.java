package cn.crabapples.utils.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 随机数生成工具类
 * Time 2018年9月28日 上午9:38:43
 * Admin
 */
public class CreateRandomUtil {
    private static final Logger logger = LoggerFactory.getLogger(CreateRandomUtil.class);
    private static final char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * 获取随机数字和字母的随机组合(大写),长度为参数
     *
     * @param length 需要获取的随机数长度
     * @return 随机字符串，长度为length
     */
    public static String getRandom(int length) {
        logger.debug("开始生成随机字符串，共[{}]位", length);
        Random ran = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(chars[ran.nextInt(26)]);
        }
        logger.debug("随机字符串生成完毕:[{}]", str.toString());
        return str.toString();
    }
}