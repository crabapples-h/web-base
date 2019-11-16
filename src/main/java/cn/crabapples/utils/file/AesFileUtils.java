package cn.crabapples.utils.file;

import cn.crabapples.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.SecureRandom;

/**
 * TODO AES加密文件工具
 *
 * @author Mr.He
 * @date 2019/8/5 21:37
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class AesFileUtils {
    private static final Logger logger = LoggerFactory.getLogger(AesFileUtils.class);

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
     * 对文件执行加解密操作
     *
     * @param keyString  密钥
     * @param sourceFile 需要加/解密的文件
     * @param targetPath 文件输出路径
     * @param type       需要执行的操作(加/解密)
     * @return 输出的文件
     * @throws Exception 运行过程中可能出现的异常
     */
    public static String doFinal(String keyString, File sourceFile, String targetPath, int type) throws Exception {
        logger.info("开始加解密操作,密钥:[{}],源文件:[{}],输出文件:[{}],操作方式:[{}]", keyString, sourceFile, targetPath, type);
        try {
            /**
             * 初始化加密方式
             */
            Cipher cipher = Cipher.getInstance("AES");
            File path = new File(targetPath);
            File targetFile = new File(targetPath + "/" + sourceFile.getName());
            /**
             * 判断输出路径是否存在
             */
            if (!path.exists()) {
                logger.warn("输出路径:[{}]不存在,即将创建对应路径", path);
                path.mkdir();
            }
            /**
             * 判断输出文件是否存在
             */
            if (!targetFile.exists()) {
                logger.warn("输出文件:[{}]不存在,即将创建对应文件", path);
                targetFile.createNewFile();
            }
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            byte[] data = new byte[1024];
            /**
             * 当操作类型为加密时
             */
            if (type == Cipher.ENCRYPT_MODE) {
                logger.debug("初始化操作为加密");
                /**
                 * 初始化Cipher为加密
                 */
                cipher.init(Cipher.ENCRYPT_MODE, createKey(keyString));
                /**
                 * 创建加密流读入文件
                 */
                logger.debug("即将开启加密流读入文件");
                CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
                logger.debug("即将开启文件流输出文件");
                for (int i = 0; i != -1; i = cipherInputStream.read(data)) {
                    fileOutputStream.write(data, 0, i);
                }
                logger.debug("文件输出完成,即将关闭加密流");
                cipherInputStream.close();
            } else if (type == Cipher.DECRYPT_MODE) {
                logger.debug("初始化操作为解密");
                /**
                 * 初始化Cipher为解密
                 */
                cipher.init(Cipher.DECRYPT_MODE, createKey(keyString));
                /**
                 * 创建解密流输出文件
                 */
                logger.debug("即将开启加密流输出文件");
                CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
                logger.debug("即将开启文件流读入文件");
                for (int i = 0; i != -1; i = fileInputStream.read(data)) {
                    cipherOutputStream.write(data, 0, i);
                }
                logger.debug("文件输出完成,即将关闭加密流");
                cipherOutputStream.close();
            } else {
                /**
                 * 当输入的类型不匹配加/解密时抛出异常
                 */
                logger.error("为匹配到相关加解密操作,原因:输入方式为[{}]", type);
                throw new ApplicationException("请传入正确的操作方式");
            }
            logger.info("文件加解密完成,即将关闭所有已开启的流");
            if (null != fileInputStream) {
                fileInputStream.close();
            }
            if (null != fileOutputStream) {
                fileOutputStream.close();
            }
            String absolutePath = targetFile.getAbsolutePath();
            logger.info("文件加解密完成,输出路径:[{}]", absolutePath);
            return absolutePath;
        } catch (Exception e) {
            logger.error("出现错误:[{}]", e.getMessage());
            throw e;
        }
    }
}