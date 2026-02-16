package deki.ecommerce.common.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            //TODO: implement after security module is done
           /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("system");
            }
            return Optional.ofNullable(authentication.getName());*/
            LOGGER.info("get Current Auditor: System");

            return Optional.of("system");
        } catch (Exception e) {
            LOGGER.info("get Current Auditor: System catching");
            return Optional.of("system catching");
        }
    }
}