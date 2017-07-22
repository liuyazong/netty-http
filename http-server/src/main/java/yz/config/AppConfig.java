package yz.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * author: liuyazong
 * datetime: 2017/7/8 下午1:42
 */
@ComponentScan(basePackages = {"yz.server", "yz.aop"})
@Configuration
@PropertySources({@PropertySource(value = {"classpath:application.properties"})})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
