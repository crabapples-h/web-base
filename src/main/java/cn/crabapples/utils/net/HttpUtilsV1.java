package cn.crabapples.utils.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * TODO Http请求工具类
 *
 * @author Mr.He
 * @date 2019/11/14 21:27
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class HttpUtilsV1 {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtilsV1.class);

    /**
     * 发送请求
     * @param url url
     * @param param 参数
     * @param method 请求方式
     * @return 回结果
     */
    private static String sendRequest(String url, String param, String method) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(method);
            if(null != param) {
                out = new PrintWriter(conn.getOutputStream());
                out.print(param);
                out.flush();
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送POST请求出现异常！参数:[]", e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 拼接请求参数
     * @param url 请求url
     * @param paramMap 需要传递的参数
     * @return 拼接之后的参数
     */
    private static Map<String, String> getParam(String url, Map<String, Object> paramMap) {
        logger.debug("开始拼接参数:[{}],[{}]",url,paramMap);
        Set<String> keySet = paramMap.keySet();
        String param = "";
        for (String string : keySet) {
            param = param + string + "=" + paramMap.get(string) + "&";
        }
        param = param.substring(0, param.length() - 1);
        String allUrl = url + "?" + param;
        Map<String, String> par = new HashMap<String, String>();
        par.put("url", url);
        par.put("param", param);
        par.put("allUrl", allUrl);
        logger.debug("参数拼接完成:[{}],",par);
        return par;
    }

    /**
     * 发送POST请求
     * @param url url
     * @param map 参数
     * @return 返回结果
     */
    public static String postRequest(String url, Map map) {
        Map<String, String> param = getParam(url, map);
        logger.debug("发送请求,url:[{}],参数:[{}]", url,param);
        String result = sendRequest(param.get("url"), param.get("param"),"POST");
        logger.debug("请求结束,返回值:[{}]", result);
        return result;
    }
 /**
     * 发送POST请求
     * @param url url
     * @return 返回结果
     */
    public static String postRequest(String url) {
        logger.debug("发送请求,url:[{}]", url);
        String result = sendRequest(url, null, "POST");
        logger.debug("请求结束,返回值:[{}]", result);
        return result;
    }

    /**
     * 发送GET请求
     * @param url url
     * @param map 参数
     * @return 返回结果
     */
    public static String getRequest(String url, Map map) {
        Map<String, String> param = getParam(url, map);
        logger.debug("发送请求,url:[{}],参数:[{}]", url,param);
        String result = sendRequest(param.get("url"), param.get("param"),"GET");
        logger.debug("请求结束,返回值:[{}]", result);
        return result;
    }
    /**
     * 发送GET请求
     * @param url url
     * @return 返回结果
     */
    public static String getRequest(String url) {
        logger.debug("发送请求,url:[{}]", url);
        String result = sendRequest(url, null, "GET");
        logger.debug("请求结束,返回值:[{}]", result);
        return result;
    }
}
