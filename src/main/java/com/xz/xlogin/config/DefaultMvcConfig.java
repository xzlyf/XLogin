package com.xz.xlogin.config;

import com.xz.xlogin.interceptor.SignInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DefaultMvcConfig implements WebMvcConfigurer {

    @Resource
    private SignInterceptor signInterceptor;

    /**
     * 添加Interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInterceptor)
                //.addPathPatterns("/**")//所有接口
                .addPathPatterns("/user/*")//拦截接口
                //.excludePathPatterns("/user/now");//排除部分开放接口
                .excludePathPatterns("/html/*", "/js/*");//排除html/js目录
    }

}