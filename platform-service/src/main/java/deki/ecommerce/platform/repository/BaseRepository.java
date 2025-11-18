package deki.ecommerce.platform.repository;

import deki.ecommerce.platform.model.data.AbstractAuditableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends AbstractAuditableEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    String REST_COLLECTION_RESOURCE_REL = "data";

}
