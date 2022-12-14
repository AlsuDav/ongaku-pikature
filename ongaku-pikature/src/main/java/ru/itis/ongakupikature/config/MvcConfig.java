//package ru.itis.ongakupikature.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@EnableWebMvc
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//
//    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[]{"classpath:/resources/", "classpath:/assets/", "classpath:/templates/"};
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        if (!registry.hasMappingForPattern("/**")) {
//            registry.addResourceHandler("/**").addResourceLocations(
//                    "classpath:/resources/");
//        }
//        if (!registry.hasMappingForPattern("/**")) {
//            registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
//        }
//    }
//
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        WebMvcConfigurer.super.configurePathMatch(configurer);
//
//        configurer.setUseSuffixPatternMatch(false);
//    }
//}