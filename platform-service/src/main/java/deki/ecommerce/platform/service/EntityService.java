package deki.ecommerce.platform.service;

import deki.ecommerce.platform.model.data.AbstractAuditableEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface EntityService {

    <T extends AbstractAuditableEntity> void save(T entity);
    <T extends AbstractAuditableEntity> void saveAndFlush(T entity);
    <T extends AbstractAuditableEntity> void saveAll(Collection<T> entities);
    <T extends AbstractAuditableEntity> void saveAllAndFlush(Collection<T> entities);

    <T> void delete(T entity);
    <T> void deleteAll(Collection<T> entities);

    <T> T getById(Class<T> entityClass, Object id);

    <T> T searchUnique(String queryStr);
    <T> T searchUnique(String queryStr, Map<String, Object> params);
    <T extends AbstractAuditableEntity> T searchUnique(Class<T> entityClass, Specification<T> spec);

    <T extends AbstractAuditableEntity> List<T> search(Class<T> entityClass, Specification<T> spec);
    <T extends AbstractAuditableEntity> List<T> search(Class<T> entityClass, Specification<T> spec, Sort sort);

    <T extends AbstractAuditableEntity> boolean isNew(T entity);

    <T extends AbstractAuditableEntity> List<T> merge(
            Collection<T> fetchedEntities,
            Function<T, String> keyExtractor,
            Function<Collection<String>, Collection<T>> existingEntitiesFunction,
            BiConsumer<T, T> mergeConsumer);
}
