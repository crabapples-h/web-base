package cn.crabapples.utils;
//package cn.alittlelove.eurekaclientwechat;
//
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.TreeMap;
//
///**
// * HeQuan
// * PC-name 29404
// * @author Wishfor_you@foxmail.com
// * 2019年3月6日 下午5:52:21
// *
// */
//public class UploadFile {
//
//	private static final String media = "https://api.weixin.qq.com/cgi-bin/material/add_material?";
//	public static void main(String[] args) {
//		String token = AccessToken.getInstance();
//		TreeMap<String,String> paramStr = new TreeMap<String,String>();
//		paramStr.put("access_token", token);
//		paramStr.put("type", "image");
//		String url = media+HttpUtil.readlyParams(paramStr);
//		HashMap paramFile = new HashMap<String,Object>();
//		paramFile.put("media", new File("d:/1.png"));
//		String result = HttpUtil2.SendHttpRequest(url, RequestMethod.POST, PRINT.PRINT, paramFile);
//		System.out.println(result);
////		Map<String,Object> resultMap = new Gson().fromJson(result, Map.class);
////		String access_token = (String) resultMap.get("access_token");
////		System.out.println(access_token);
//		System.out.println(token);
//	}
//}
