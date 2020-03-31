package cn.crabapples.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * TODO RestTemplate配置类
 *
 * @author Mr.He
 * 2019/7/21 14:47
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
@Configuration
public class RestTemplateConfigure {
    /**
     * 开启负载均衡
     */
    @Bean
    @LoadBalanced
    @Primary
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 不开启负载均衡
     */
    @Bean(name = "restTemplateNotLoadBalanced")
    public RestTemplate getRestTemplateNotLoadBalanced() {
        return new RestTemplate();
    }
}
