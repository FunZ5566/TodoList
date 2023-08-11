package com.sam.todolist2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .paths(PathSelectors.regex("/error.*")) // 排除指定路徑
//                .build()
//                .apiInfo(apiInfo());
//    }
@Bean
public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())//設定Swagger資訊
            .select()
            /**
             * apis():指定掃描的介面
             *  RequestHandlerSelectors:設定要掃描介面的方式
             *       basePackage:指定要掃描的包
             *       any:掃面全部
             *       none:不掃描
             *       withClassAnnotation:掃描類上的註解(引數是類上註解的class物件)
             *       withMethodAnnotation:掃描方法上的註解(引數是方法上的註解的class物件)
             */
            .apis(RequestHandlerSelectors.basePackage("com.sam.todolist2.controller"))
            /**
             * paths():過濾路徑
             *  PathSelectors:設定過濾的路徑
             *      any:過濾全部路徑
             *      none:不過濾路徑
             *      ant:過濾指定路徑:按照按照Spring的AntPathMatcher提供的match方法進行匹配
             *      regex:過濾指定路徑:按照String的matches方法進行匹配
             */
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
}

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "To Do List",
                "To Do List API",
                "v1.0",
                "",
                new Contact("SamChang", "",
                        ""),
                "", "", Collections.emptyList());
    }
}
