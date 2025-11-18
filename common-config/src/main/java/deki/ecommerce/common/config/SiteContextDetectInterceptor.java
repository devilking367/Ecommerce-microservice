package deki.ecommerce.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SiteContextDetectInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteContextDetectInterceptor.class);

    public static final String DEFAULT_HEADER_NAME = "L10N-Mode-Enabled";
    public static final String LANGUAGE_PARAMETER = "_lang";
    public static final String CURRENCY_HEADER_NAME = "X-Currency";
    public static final String SESSION_ID_HEADER_NAME = "X-Session-Id";
    public static final String REAL_IP_HEADER_NAME = "X-Real-IP";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOGGER.info("Incoming Request: [{}] {}", request.getMethod(), request.getRequestURI());
        return true; // tiếp tục xử lý
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        LOGGER.info("Completed Request: [{}] {} - Status: {}", request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}
