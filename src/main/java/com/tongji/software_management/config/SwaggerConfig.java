package com.tongji.software_management.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(value = {"knife4j.enable"}, matchIfMissing = true)
public class SwaggerConfig {
//    添加api文档分组
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 选择swagger2版本
                .groupName("后端所有的接口")
                .apiInfo(apiInfo())
                .select()
                // 指定生成api文档的包
                .apis(RequestHandlerSelectors.basePackage("com.tongji.software_management.controller"))
                .paths(PathSelectors.any())
                .build();
    }

//    api文档相关展示信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("软管项目restful风格接口文档")
                .description("供项目小组成员观看")
                .contact(new Contact("HJK, LZA","",""))
                .version("1.0")
                .termsOfServiceUrl("http://localhost:8443/doc.html")     //网站地址
                .build();
    }
}