package org.zerock.apiserver.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zerock.apiserver.formatter.LocalDateFormatter;

@Configuration
@Log4j2
//web과 관련 설정
public class CustomServletConfig implements WebMvcConfigurer {
    //WebMvcConfigurer사용시 오버라이드 할 수 있는것들이 많아진다.

    @Override
    public void addFormatters(FormatterRegistry registry){
        log.info("------------------");
        log.info("addFormatters");

        registry.addFormatter(new LocalDateFormatter());
    }

    //cros설정
    //pre-flight => ajax보낼때 미리 찔러보기
//    @Override
//    public void addCorsMappings(CorsRegistry registry){
//        registry.addMapping("/**")
//                .maxAge(500)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
//                .allowedOrigins("*");
//
//    }
    //->security로 빼주어야 한다

}