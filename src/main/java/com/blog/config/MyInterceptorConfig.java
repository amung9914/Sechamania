package com.blog.config;

import com.blog.util.RootPathInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@RequiredArgsConstructor
public class MyInterceptorConfig implements WebMvcConfigurer {

    //referer 인터셉터
    private final RootPathInterceptor rootPathInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rootPathInterceptor)
                .addPathPatterns("/");
    }
}
