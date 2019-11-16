package cn.crabapples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.List;

/**
 * TODO Web静态资源配置
 *
 * @author Mr.He
 * @date 2019/11/14 21:20
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport{
	// 这个方法是用来配置静态资源的，比如html，js，css，等等
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		 registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
		 super.addResourceHandlers(registry);
	}
	/**
	 * 异步乱码解决方式
	 * @return
	 */
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
	    StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
	    return converter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	    super.configureMessageConverters(converters);
	    converters.add(responseBodyConverter());
	}
}