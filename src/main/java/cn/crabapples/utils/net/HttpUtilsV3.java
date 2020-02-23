package cn.crabapples.utils.net;

import cn.crabapples.utils.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 网络请求工具类
 * 2019年2月22日 下午7:05:49
 *
 * @author H
 * TODO 发起网络请求时使用（默认为POST请求,主要用于带文件传输）
 * Admin
 */
public class HttpUtilsV3 {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtilsV3.class);
    private static final Map<String, String> HEADER = new TreeMap<String, String>();
    private static final int CONN_TIMEOUT = 30000;    //链接超时时间
    private static final int READ_TIMEOUT = 30000;    //响应超时时间
    private static final String boundary = IdUtils.getRandomString(15); //随机种子

    static {
        HEADER.put("Accept", "*/*");
        HEADER.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
        HEADER.put("Connection", "Keep-Alive");
        HEADER.put("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    /**
     * 发送Http请求(默认请求头)
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 返回响应结果
     */
    public static String SendHttpRequest(String url, RequestMethod method, HashMap<String, ?>... params) throws IOException {
        logger.debug("即将开始发送请求(默认请求头),url:[{}],参数[{}],请求方式[{}]", url, params, method);
        String result = sendRequest(url, null, method, params);
        logger.debug("请求结束,结果:[{}]", result);
        return result;
    }

    /**
     * 发送Http请求(自定义请求头)
     *
     * @param url    请求地址
     * @param params 请求参数
     * @param header 请求头(若不设置则使用默认请求头)
     * @return 返回响应结果
     */
    public static String SendHttpRequest(String url, Map<String, String> header, RequestMethod method, HashMap<String, ?>... params) throws IOException {
        logger.debug("即将开始发送请求(自定义请求头),url:[{}],参数[{}],请求方式[{}],请求头:[{}]", url, params, method, header);
        String result = sendRequest(url, header, method, params);
        logger.debug("请求结束,结果:[{}]", result);
        return result;
    }

    /**
     * 发送http请求
     *
     * @param url    请求地址
     * @param params 参数
     * @param header 请求头
     * @return 响应消息
     */
    private static String sendRequest(String url, Map<String, String> header, RequestMethod method, HashMap<String, ?>... params) throws IOException {
        logger.info("开始发送请求,url:[{}],参数[{}],请求方式[{}],请求头:[{}]", url, params, method, header);
        OutputStream out = null;
        BufferedReader reader = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            logger.debug("设置请求方式(默认为POST)");
            if (method == RequestMethod.GET) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
            }
            logger.debug("请求方式设置为:[{}]",conn.getRequestMethod());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            setRequestHeader(conn, header);
            conn.setConnectTimeout(CONN_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            out = conn.getOutputStream();
            logger.debug("准备发送数据:[{}]", params);
            sendData(conn.getOutputStream(), params);
            out.write(("\r\n--" + boundary + "--").getBytes());
            logger.debug("数据发送完成");
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            StringBuilder result = new StringBuilder();
            logger.debug("开始接收返回值");
            reader = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String str = reader.readLine();
                if (null == str) {
                    break;
                }
                result.append(str);
            }
            in.close();
            logger.info("请求结束,返回结果:[{}]", result.toString());
            return result.toString();
        } catch (IOException e) {
            logger.error("请求失败,详情:[]", e);
            throw e;
        }
    }

    /**
     * 发送数据
     *
     * @param out    输出流
     * @param params 参数
     * @throws IOException
     */
    private static void sendData(OutputStream out, HashMap<String, ?>... params) throws IOException {
        logger.info("发送数据开始");
        for (HashMap<String, ?> hashMap : params) {
            Set<String> keys = hashMap.keySet();
            for (String key : keys) {
                if (hashMap.get(key) instanceof String) {
                    sendStr(key, (String) hashMap.get(key), out);
                } else if (hashMap.get(key) instanceof File) {
                    sendFile(key, (File) hashMap.get(key), out);
                } else if (hashMap.get(key) instanceof File[]) {
                    for (File file : (File[]) hashMap.get(key)) {
                        sendFile(key, file, out);
                    }
                }
            }
        }
        logger.info("发送数据结束");
    }

    /**
     * 发送字符串
     *
     * @param key   参数名
     * @param value 参数值
     * @param out   输出流
     * @throws IOException
     */
    private static void sendStr(String key, String value, OutputStream out) throws IOException {
        logger.debug("当前发送的参数名:[{}],参数值:[{}]", key, value);
        StringBuffer sb = new StringBuffer();
        sb.append("\r\n").append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + key + "\"; " + "\r\n\r\n");
        sb.append(value).append("\r\n\r\n");
        out.write(sb.toString().getBytes());
        out.flush();
        logger.debug("参数:[{}]发送完成", key);
    }

    /**
     * 发送文件
     *
     * @param key  参数名
     * @param file 文件
     * @param out  输出流
     */
    private static void sendFile(String key, File file, OutputStream out) {
        logger.debug("当前发送的文件:[{}],参数值:[{}]", key, file);
        StringBuffer sb = new StringBuffer();
        try {
            String contextType = "";
            Path path = Paths.get(file.getPath());
            contextType = Files.probeContentType(path);
            sb.append("\r\n").append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + key + "\"; filelength=\"" + file.length() + "\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type: " + contextType + "\r\n\r\n");
            out.write(sb.toString().getBytes());
            InputStream in = new FileInputStream(file);
            byte[] datas = new byte[1024];
            int end = 0;
            while ((end = in.read(datas)) != -1) {
                out.write(datas, 0, end);
            }
            out.flush();
            in.close();
        } catch (IOException e) {
            logger.error("文件:[{}]发送失败,详情:[]", key, e);
        }
        logger.debug("文件:[{}]发送完成", key);
    }

    /**
     * 设置请求头
     *
     * @param conn 传入需要设置请求头的HttpURLConnection
     * @return 返回设置完毕之后的HttpURLConnection
     */
    private static HttpURLConnection setRequestHeader(HttpURLConnection conn, Map<String, String> header) {
        logger.debug("开始设置请求头:[{}]", header);
        if (header == null) {
            Set<String> keySet = HEADER.keySet();
            for (String key : keySet) {
                conn.setRequestProperty(key, HEADER.get(key));
            }
        } else {
            logger.debug("使用默认请求头:[{}]", header);
            Set<String> keySet = header.keySet();
            for (String key : keySet) {
                conn.setRequestProperty(key, header.get(key));
            }
        }
        logger.debug("请求头设置完毕");
        return conn;
    }

}