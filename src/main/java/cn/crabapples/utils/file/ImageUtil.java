package cn.crabapples.utils.file;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2018年9月9日 下午4:43:47
 * @author H
 * TODO 保存图片的工具
 * Admin
 */
@Component
public class ImageUtil {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	private String Path;
	/**
	 * @param path 图片保存路径
	 */
	public ImageUtil(String path) {
		Path = path;
	}

	/**
	 * 保存图片-不压缩.返回文件路径
	 * @param multipartFile 传入的文件流
	 * @return	返回文件的保存路径
	 * @throws IOException 文件IO异常
	 */
	public String SaveIMG(MultipartFile multipartFile) throws IOException {
		Date date = new Date();
		String path = Path +"/"+ new SimpleDateFormat("YYYY-MM-dd_").format(date);
		String fileName = new SimpleDateFormat("HH-mm-ss-").format(date);
		File fold = new File(path);
		try {
			if(!fold.exists()) {
				fold.mkdirs();
			}
			fileName+=new Date().getTime();
			String originalFilename = multipartFile.getOriginalFilename();
			int last = originalFilename.lastIndexOf(".");
			int length = originalFilename.length();
			try {
				fileName+=originalFilename.substring(last,length);
			}catch (StringIndexOutOfBoundsException e) {
				throw new IOException("没有文件");
			}
			File file = new File(path+"/"+fileName);
			writeFile(multipartFile, file);
			path = file.getPath().replace(Path, "\\file\\");
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("文件保存出错");
			return null;
		}
		System.out.println("文件路径: "+path);
		return path.replace("\\", "/");
	};

	/**
	 * 保存图片-压缩至1/2.返回文件路径
	 * @param mFile 需要保存的文件
	 * @return 文件路径
	 * @throws IOException 文件保存IO异常
	 */
	public String SaveIMG_5(MultipartFile mFile) throws IOException {
		Date date = new Date();
		String path = Path +"/"+ new SimpleDateFormat("YYYY-MM-dd_").format(date);
		String fileName = new SimpleDateFormat("HH-mm-ss-").format(date);
		File fold = new File(path);
		try {
			if(!fold.exists()) {
				fold.mkdirs();
			}
			fileName += new Date().getTime();
			String originalFilename = mFile.getOriginalFilename();
			int last = originalFilename.lastIndexOf(".");
			int length = originalFilename.length();
			try {
				fileName += originalFilename.substring(last,length);
			}catch (StringIndexOutOfBoundsException e) {
				throw new IOException("没有文件");
			}
			File file = new File(path+"/"+fileName);
			writeFile(mFile, file);
			path = file.getPath().replace(Path, "\\file\\");
			Thumbnails.of(file).scale(1f).outputQuality(0.5f).toFile(file);
		}catch(IOException e) {
			System.out.println("文件保存出错");
			return null;
		}
		System.out.println("文件路径: "+path);
		return path.replace("\\", "/");
	}

	/**
	 * 写入文件
	 * @param mFile 传入的文件
	 * @param file	需要保存到的文件
	 * @throws IOException 文件IO异常
	 */
	private static void writeFile(MultipartFile mFile,File file) throws IOException {
		InputStream is = mFile.getInputStream();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedInputStream bis = new BufferedInputStream(is);
		byte [] b = new byte[1024];
		while(bis.read(b)!=-1) {
			bos.write(b);
		}
		bos.flush();
		bos.close();
		changeIMG(file,file);
	}

	/**
	 * 根据地址获取名字
	 * @param path 文件地址
	 * @return 文件名
	 */
	public static String getNameForPath(String path){
		String fileName = "/file"+path.split("file")[1];
		System.out.println("文件名为: "+fileName);
		return fileName;
	}

	/**
	 * 压缩图片
	 * @param file  	原路径
	 * @param file1		新路径
	 * @throws IOException
	 */
	public static void changeIMG(File file,File file1) throws IOException {
		Thumbnails.of(file).scale(1f).outputQuality(0.25f).toFile(file1);
	}
}
