package cn.crabapples.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2018年9月9日 下午4:43:47
 *
 * @author H
 * TODO Http请求保存文件的工具
 * Admin
 */
public class FileSaveUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileSaveUtils.class);

    private String ROOT;
    /**
     * @param root 文件保存路径
     */
    public FileSaveUtils(String root) {
        this.ROOT = root;
    }

    /**
     * 保存文件,返回文件路径
     *
     * @param multipartFile 传入的文件流
     * @return 返回文件的保存路径
     * @throws IOException 文件IO异常
     */
    public String saveFile(MultipartFile multipartFile) throws IOException {
        Date date = new Date();
        String fullPath = ROOT + new SimpleDateFormat("YYYY-MM-dd").format(date);
        logger.info("保存文件,路径[{}]", fullPath);
        String fullName = new SimpleDateFormat("HH-mm-ss-").format(date);
        logger.info("保存文件,文件名[{}]", fullName);
        File fold = new File(fullPath);
        try {
            if (!fold.exists()) {
                logger.debug("目录不存在,创建目录[{}]", fold.getPath());
                fold.mkdirs();
            }
            fullName += date.getTime();
            String originalFilename = multipartFile.getOriginalFilename();
            int last = originalFilename.lastIndexOf(".");
            int length = originalFilename.length();
            fullName += originalFilename.substring(last, length);
            logger.debug("生成全文件名[{}]", fullName);
            File file = new File(fullPath + "/" + fullName);
            logger.info("准备写入文件");
            writeFile(multipartFile, file);
            logger.info("文件写入完成");
            fullPath = file.getPath();
        } catch (IOException e) {
            logger.error("保存文件时出现错误[]", e);
            return null;
        }
        logger.info("文件保存完成,路径[{}]", fullPath);
        return fullPath;
    }

    /**
     * 写入文件
     *
     * @param mFile 传入的文件
     * @param file  需要保存到的文件
     * @throws IOException 文件IO异常
     */
    private static void writeFile(MultipartFile mFile, File file) throws IOException {
        logger.debug("开始写入文件");
        InputStream is = mFile.getInputStream();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] b = new byte[1024];
        while (bis.read(b) != -1) {
            bos.write(b);
        }
        bos.flush();
        bos.close();
        logger.debug("文件写入完成");
    }

    /**
     * 根据地址获取名字
     *
     * @param path 文件地址
     * @return 文件名
     */
    public static String getNameByPath(String path) {
        String fileName = "/file" + path.split("file")[1];
        logger.info("文件名为:[{}]",fileName);
        return fileName;
    }
}
