package cn.crabapples.utils;

import cn.crabapples.exception.ApplicationException;
import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * TODO google身份验证器
 *
 * @author Mr.He
 * 12/14/19 7:12 PM
 * e-mail crabapples.cn@gmail.com
 * qq 294046317
 * pc-name root
 */

public class GoogleAuthenticatorSimpleUtils {
    // 生成的key长度( Generate secret key length)
    private static final int KEY_LENGTH = 10;

    public static final String SEED = "g8GjEvTbW5oVSV7avL47357438reyhreyuryetredLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";
    // Java实现随机数算法
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    // 最多可偏移的时间
    int window_size = 3; // default 3 - max 17

    /**
     * set the windows size. This is an integer value representing the number of
     * 30 second windows we allow The bigger the window, the more tolerant of
     * clock skew we are.
     *
     * @param s window size - must be >=1 and <=17. Other values are ignored
     */
    public void setWindowSize(int s) {
        if (s >= 1 && s <= 17)
            window_size = s;
    }

    /**
     * Generate a random secret key. This must be saved by the server and
     * associated with the users account to verify the code displayed by Google
     * Authenticator. The user must register this secret on their device.
     * 生成一个随机秘钥
     *
     * @return secret key
     */
    public static String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            byte[] buffer = sr.generateSeed(KEY_LENGTH);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            return new String(bEncodedKey);
        } catch (NoSuchAlgorithmException e) {
// should never occur... configuration error
        }
        return null;
    }

    /**
     * Return a URL that generates and displays a QR barcode. The user scans
     * this bar code with the Google Authenticator application on their
     * smartphone to register the auth code. They can also manually enter the
     * secret if desired
     *
     * @param user   user id (e.g. fflinstone)
     * @param host   host or system that the code is for (e.g. myapp.com)
     * @param secret the secret that was previously generated for this user
     * @return the URL for the QR code to scan
     */
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s?secret=%s";
        return String.format(format, user, host, secret);
    }

    /**
     * 生成一个google身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。
     *
     * @param user   账号
     * @param secret 密钥
     * @return
     */
    public static String getQRBarcode(String user, String secret) {
        String format = "otpauth://totp/%s?secret=%s";
        return String.format(format, user, secret);
    }

    /**
     * Check the code entered by the user to see if it is valid 验证code是否合法
     *
     * @param secret   The users secret.
     * @param code     The code displayed on the users device
     * @param timeMsec The time in msec (System.currentTimeMillis() for example)
     * @return
     */
    public boolean check_code(String secret, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
// convert unix msec time into a 30 second "window"
// this is per the TOTP spec (see the RFC for details)
        long t = (timeMsec / 1000L) / 30L;
// Window is used to check codes generated in the near past.
// You can use this value to tune how far you're willing to go.
        for (int i = -window_size; i <= window_size; ++i) {
            long hash;
            try {
                hash = verify_code(decodedKey, t + i);
            } catch (Exception e) {
// Yes, this is bad form - but
// the exceptions thrown would be rare and a static
// configuration problem
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
// return false;
            }
            if (hash == code) {
                return true;
            }
        }
// The validation code is invalid.
        return false;
    }

    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
// We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
// We are dealing with signed bytes:
// we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }

    /**
     * TODO AES加密字符串
     *
     * @author wishforyou.xia@gmail.com
     * @date 2019/7/3 23:24
     */
    public static class AesUtils {
        private static final Logger logger = LoggerFactory.getLogger(AesUtils.class);

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
        public static String doFinalAsString(String keyString, String string, int type) throws Exception {
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
        public static String doFinalAsFile(String keyString, File sourceFile, String targetPath, int type) throws Exception {
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
}
