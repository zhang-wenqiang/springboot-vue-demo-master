package com.example.demo.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//映射static路径的请求到static目录下
// 静态资源访问路径和存放路径配置
//registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
// swagger访问配置
//registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");



@Configuration
public class WebConf extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        //通过image访问本地的图片
        //
        registry.addResourceHandler("/image/**").addResourceLocations("file:E:/idea-2021/springboot-vue-demo-master/springboot-vue-demo-master/springboot/imgs/");
    }

}
