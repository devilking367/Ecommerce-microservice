package deki.ecommerce.product.repository;

import deki.ecommerce.platform.repository.BaseRepository;
import deki.ecommerce.product.domain.product.Product;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository cho Product entity
 * Tự động expose REST API endpoints qua Spring Data REST
 */
@Repository
@RepositoryRestResource(collectionResourceRel = BaseRepository.REST_COLLECTION_RESOURCE_REL, path = "products")
public interface ProductRepository extends BaseRepository<Product> {
}
