package com.hanamja.moa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://moafrontend-env.eba-qtcfatkw.ap-northeast-2.elasticbeanstalk.com");
        config.addAllowedOrigin("http://moa-frontend.s3-website.ap-northeast-2.amazonaws.com/");
        config.addAllowedOrigin("http://dev.moa-univ.com");
        config.addAllowedOrigin("https://dev.moa-univ.com");
        config.addAllowedOrigin("http://moa-univ.com");
        config.addAllowedOrigin("https://moa-univ.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
