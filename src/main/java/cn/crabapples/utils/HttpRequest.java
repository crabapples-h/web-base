package cn.crabapples.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpRequest {
    private static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
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

    private static Map<String, String> getParam(String url, Map<String, String> paramMap) {
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
        return par;
    }

    public static String sendRequest(String txtUrl, Map map) {
        Map<String, String> param = getParam(txtUrl, map);
        System.out.println(param.get("allUrl"));
        String sr = HttpRequest.sendPost(param.get("url"), param.get("param"));
        return sr;
    }
}
