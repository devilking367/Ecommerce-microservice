package deki.ecommerce.product.config;

import deki.ecommerce.common.config.SiteContextDetectInterceptor;
import deki.ecommerce.platform.config.BaseRepositoryRestConfiguration;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Configuration cho Spring Data REST trong Product Service
 * Extend BaseRepositoryRestConfiguration để kế thừa các settings chung
 */
@Configuration
public class RestConfiguration extends BaseRepositoryRestConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestConfiguration.class);

    public RestConfiguration(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Apply base configuration
        super.configureRepositoryRestConfiguration(config, cors);
        LOGGER.info("======================== Config RepositoryRestConfiguration");
        // Set base path cho tất cả REST endpoints
//        config.setBasePath("/api");
    }
}
