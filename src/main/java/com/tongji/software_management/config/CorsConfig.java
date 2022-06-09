package com.tongji.software_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 1允许的域名
        config.addAllowedOriginPattern("*");
        // 2允许用户操作和处理cookie
        config.setAllowCredentials(true);
        // 3允许任何方法（post、get等）
        config.addAllowedMethod("*");
        // 4允许任何请求头
        config.addAllowedHeader("*");
        /*
            5可以让用户拿到的字段，有几个字段无论设置与否都可以拿到的，
            包括：Cache-Control、Content-Language、Content-Type、
            Expires、Last-Modified、Pragma
        */
        config.addExposedHeader("*");
        // 6一次跨域请求能够生效的期限（设置为1hour内不用再发跨域请求 ）
        config.setMaxAge(60*60L);

        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configSource);
    }
}

