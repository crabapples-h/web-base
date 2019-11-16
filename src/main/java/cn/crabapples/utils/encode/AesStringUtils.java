package cn.crabapples.utils.encode;

import cn.crabapples.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * TODO AES加密字符串
 *
 * @author wishforyou.xia@gmail.com
 * @date 2019/7/3 23:24
 */
public class AesStringUtils {
    private static final Logger logger = LoggerFactory.getLogger(AesStringUtils.class);

    /**
     * 用于将密钥种子转换为KEY
     *
     * @param seed 密钥种子
     * @return 密钥
     * @throws Exception 生成密钥可能出现的异常
     */
    private static Key createKey(String seed) throws Exception {
        logger.debug("开始生成密钥");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(seed.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        logger.debug("密钥生成结束");
        return secretKey;
    }

    /**
     * 对字符串进行加解密操作
     * @param keyString 密钥
     * @param string    需要加解密的字符串
     * @param type      需要执行的操作(加/解密)
     * @return 输出的字符串
     * @throws Exception 运行过程中可能出现的异常
     */
    public static String doFinal(String keyString, String string, int type) throws Exception {
        logger.info("开始加解密操作,密钥:[{}],内容:[{}],操作方式:[{}]", keyString, string, type);
        String data = null;
        try {
            /**
             * 初始化加密方式
             */
            Cipher cipher = Cipher.getInstance("AES");
            /**
             * 当操作类型为加密时
             */
            if (type == Cipher.ENCRYPT_MODE) {
                logger.debug("初始化操作为加密");
                /**
                 * 初始化Cipher为加密
                 */
                cipher.init(Cipher.ENCRYPT_MODE, createKey(keyString));
                byte[] dataByte = cipher.doFinal(string.getBytes());
                byte[] encodeByte = Base64.getEncoder().encode(dataByte);
                data = new String(encodeByte);
            } else if (type == Cipher.DECRYPT_MODE) {
                logger.debug("初始化操作为解密");
                /**
                 * 初始化Cipher为解密
                 */
                cipher.init(Cipher.DECRYPT_MODE, createKey(keyString));
                byte[] decodeByte = Base64.getDecoder().decode(string.getBytes());
                byte[] dataByte = cipher.doFinal(decodeByte);
                data = new String(dataByte);
            } else {
                /**
                 * 当输入的类型不匹配加/解密时抛出异常
                 */
                logger.error("为匹配到相关加解密操作,原因:输入方式为[{}]",type);
                throw new ApplicationException("请传入正确的操作方式");
            }
        } catch (Exception e) {
            logger.error("出现错误:[{}]", e.getMessage());
            throw e;
        }
        logger.info("加解密操作完成,元数据:[{}],操作后数据:[{}]", string, data);
        return data;
    }
}