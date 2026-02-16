package deki.ecommerce.platform.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Base configuration cho Spring Data REST
 * Các service khác có thể extend class này hoặc sử dụng trực tiếp
 */
public abstract class BaseRepositoryRestConfiguration implements RepositoryRestConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepositoryRestConfiguration.class);
    private final EntityManagerFactory entityManagerFactory;

    public BaseRepositoryRestConfiguration(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config,
            final CorsRegistry cors) {
        // Disable ALPS (Application-Level Profile Semantics)
        config.getMetadataConfiguration().setAlpsEnabled(false);

        // Customize parameter names
        config.setPageParamName("_currentPage");
        config.setLimitParamName("_pageSize");
        config.setSortParamName("_sort");

        // Set default media type
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);

        // Pagination defaults
        config.setDefaultPageSize(20);

        // Return body on create/update
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);

        // Expose IDs for all managed entities
        final Class<?>[] managedEntityTypes = entityManagerFactory.getMetamodel()
                .getEntities().stream()
                .map(EntityType::getJavaType)
                .distinct()
                .toArray(Class[]::new);
        config.exposeIdsFor(managedEntityTypes);

        // Exposure configuration
        config.getExposureConfiguration()
                .disablePutForCreation()
                .withAssociationExposure((metadata, httpMethods) -> httpMethods
                        .disable(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
                        .enable(HttpMethod.GET));

        // Projection parameter name
        config.getProjectionConfiguration().setParameterName("_expand");

        // Enable enum translation
        config.setEnableEnumTranslation(true);

        // Configure CORS
        configureCors(cors);
        LOGGER.info("======================== Config RepositoryRestConfiguration");
    }

    /**
     * Configure CORS settings
     * Override method này trong subclass nếu cần customize CORS
     */
    protected void configureCors(CorsRegistry cors) {
        cors.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
