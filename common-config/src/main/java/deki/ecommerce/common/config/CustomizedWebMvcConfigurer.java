package deki.ecommerce.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CustomizedWebMvcConfigurer implements WebMvcConfigurer {

    private final SiteContextDetectInterceptor siteContextDetectInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(siteContextDetectInterceptor)
                .addPathPatterns("/**") // intercept tất cả request
                .excludePathPatterns("/health", "/actuator/**"); // trừ endpoint hệ thống
    }
}
