package cn.crabapples.utils.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO 对字符串进行加密
 *
 * @author Mr.He
 * @date 2019/01/08 02:15
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class SignUtils {
    private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);

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

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}