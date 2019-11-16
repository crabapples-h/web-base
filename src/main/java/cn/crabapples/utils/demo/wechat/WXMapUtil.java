package cn.crabapples.utils.demo.wechat;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * TODO 微信订单MAP工具类
 *
 * @author Mr.He
 * @date 2019/11/16 14:59
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class WXMapUtil {
	private static final Logger logger = LoggerFactory.getLogger(WXMapUtil.class);

	/**
	 * 将回调返回的XML字符串转换为Map(仅可在微信订单返回数据时使用)
	 * @param str 微信返回的XML格式字符串
	 * @return XML转换的map
	 */
	public static TreeMap<String,String> XMLStringToMap(String str) {
		logger.info("开始解析返回的xml:{}",str);
		Document document;
		try {
			document = DocumentHelper.parseText(str);
			@SuppressWarnings("unchecked")
			List<Element> elements = document.getRootElement().elements();
			TreeMap<String,String> resultMap = new TreeMap<String,String>();
			for (Element element : elements) {
					if("result_code".equals(element.getName())) {
						if((element.getText().indexOf("FAIL")!=-1)) {
							for (Element err : elements) {
								if("err_code".equals(err.getName())) {
									logger.error("节点返回错误:名称[{}],值[{}]",element.getName(),element.getText());
								}
							}
						}
					}
					if("err_code_des".equals(element.getName())) {
						throw new RuntimeException(element.getText());
					}
				logger.debug("节点信息:名称[{}],值[{}]",element.getName(),element.getText());
				resultMap.put(element.getName(), element.getText());
			}
			logger.info("解析完成:{}",resultMap);
			return resultMap;
		} catch (DocumentException e) {
			logger.error("解析xml时出现错误[{}]",e);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 将map转换为微信所需的XML格式字符串(仅可在微信订单下单时使用)
	 * @param map 需包含要提交的各项参数
	 * @return	返回值为微信需要的xml格式
	 */
	public static String MapToXMLString(Map<String, String> map) {
		logger.info("开始转换数据:{}",map);
		Set<String> keySet = map.keySet();
		StringBuffer xml = new StringBuffer("<xml>");
		String sign = "";
		for (String key : keySet) {
			logger.info("当前执行转换的key:[{}],当前执行转换的value:[{}]",key,map.get(key));
			StringBuffer element = new StringBuffer();
			if("sign".equals(key)) {
				sign = "<"+key+">"+map.get(key)+"</"+key+">";
			} else {
				element.append("<").append(key).append(">");
				element.append("<![CDATA[").append(map.get(key)).append("]]>");
				element.append("</").append(key).append(">");
			}
			xml.append(element);
		}
		xml.append(sign);
		xml.append("</xml>");
		logger.info("转换完成:[{}]",xml);
		return xml.toString();
	}

	// todo 下面两个方法暂时没什么用，以后优化
	/**
	 * 将实体类转换为map
	 * @param model
	 * @return
	 * @throws ModelToMapFormatException
	 */
	public static <T> Map<String, String> ModelToMap(T model) throws ModelToMapFormatException {
		try {
			Map<String,String> map = new HashMap<String,String>();
				Method[] methods = model.getClass().getMethods();	//获取所有自定义的方法
				for (Method method : methods) {	//遍历所有自定义方法
					if(method.getName().startsWith("get")) {	//判断当前方法是否为get方法
						Object result = method.invoke(model);	//执行当前get方法并获取返回值
						if(null==result)
							continue;
						map.put(method.getName().replace("get", ""), result.toString());
					}
				}
			return map;
		}catch(Exception e){
			throw new ModelToMapFormatException();
		}
	}
	/**
	 * 实体类转换map异常
	 * 2019年2月14日 下午2:25:47
	 * @author H
	 * TODO
	 * Admin
	 */
	private static class ModelToMapFormatException extends Exception{
		private static final long serialVersionUID = 1L;
	}
}