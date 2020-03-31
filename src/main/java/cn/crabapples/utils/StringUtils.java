package cn.crabapples.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * TODO 字符串工具类
 *
 * @author Mr.He
 * 2019/7/23 23:16
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * 将字符串首字母转换为大写
     * @param source 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String firstCharToUpCase(String source){
        logger.debug("需要将首字母转换为大写的字符串:[{}]",source);
        char [] chars = source.toCharArray();
        if(chars[0] >= 97 && chars[0] <= 122){
            chars[0] -= 32;
        }
        String target = String.valueOf(chars);
        logger.debug("转换后的字符串:[{}]",target);
        return target;
    }
    /**
     * 将字符串首字母转换为小写
     * @param source 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String firstCharToLowCase(String source){
        logger.debug("需要将首字母转换为小写的字符串:[{}]",source);
        char [] chars = source.toCharArray();
        if(chars[0] >= 65-32 && chars[0] <= 90){
            chars[0] += 32;
        }
        String target = String.valueOf(chars);
        logger.debug("转换后的字符串:[{}]",target);
        return target;
    }

    /**
     * SHA1计算
     *
     * @param string 需要进行计算的字符串
     * @return sha1值
     */
    public static String getSHA1(String string) throws NoSuchAlgorithmException {
        logger.info("开始对[{}]计算sha1值", string);
        String sha1 = encode("sha1", string);
        logger.info("[{}]计算sha1值结束[{}]", string, sha1);
        return sha1;
    }

    /**
     * MD5计算
     *
     * @param string 需要进行计算的字符串
     * @return MD5值
     */
    public static String getMD5(String string) throws NoSuchAlgorithmException {
        logger.info("开始对[{}]计算md5值", string);
        String md5 = encode("md5", string);
        logger.info("[{}]计算md5值结束[{}]", string, md5);
        return md5;
    }

    /**
     * 执行计算操作
     *
     * @param type  计算方式
     * @param value 需要计算的值
     * @return 计算后的结果
     */
    private static String encode(String type, String value) throws NoSuchAlgorithmException {
        logger.debug("开始对[{}]执行计算[{}]操作", value, type);

        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(value.getBytes());
            String data = getFormattedText(messageDigest.digest());
            logger.debug("对[{}]执行计算[{}]完成,结果:[{}]", value, type, data);
            return data;
        } catch (NoSuchAlgorithmException e) {
            logger.debug("对[{}]执行计算[{}]时出现错误,原因:[{}]", value, type, e);
            throw e;
        }
    }

    /**
     * 将计算结果转为字符串
     *
     * @param bytes 计算结果
     * @return 转为字符串后的值
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * 获取随机数字和字母的随机组合(大写),长度为参数
     *
     * @param length 需要获取的随机数长度
     * @return 随机字符串，长度为length
     */
    public static String getRandomString(int length) {
        logger.debug("开始生成随机字符串，共[{}]位", length);
        Random ran = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(chars[ran.nextInt(26)]);
        }
        logger.debug("随机字符串生成完毕:[{}]", str.toString());
        return str.toString();
    }

    /**
     * 获取不带横线的UUID
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

}
