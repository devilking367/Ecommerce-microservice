package deki.ecommerce.platform.service.impl;

import deki.ecommerce.common.exception.BaseException;
import deki.ecommerce.common.util.ServicesUtils;
import deki.ecommerce.platform.model.data.AbstractAuditableEntity;
import deki.ecommerce.platform.repository.BaseRepository;
import deki.ecommerce.platform.service.EntityService;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EntityServiceImpl implements EntityService {

    @PersistenceContext
    private EntityManager entityManager;
    private final Map<String, BaseRepository<? extends AbstractAuditableEntity>> repositoryMap = new HashMap<>();
    private final Lazy<Repositories> repositories;
    private final PersistentEntities entities;

    private final Map<String, JpaEntityInformation<AbstractAuditableEntity, ?>> jpaEntityInformationMap = new HashMap<>();

    public EntityServiceImpl(Lazy<Repositories> repositories, PersistentEntities entities) {
        this.repositories = repositories;
        this.entities = entities;
    }

    // region SAVE / UPDATE
    @Transactional
    @Override
    public <T extends AbstractAuditableEntity> void save(final T entity) {
        ServicesUtils.validateParameterNotNullStandardMessage("entity", entity);
        if (isNew(entity)) {
            entityManager.persist(entity);
        } else {
            final T mergedEntity = entityManager.merge(entity);
            mergeProperties(mergedEntity, entity);
        }
    }

    @Transactional
    @Override
    public <T extends AbstractAuditableEntity> void saveAndFlush(final T entity) {
        save(entity);
        flush();
    }

    @Transactional
    @Override
    public <T extends AbstractAuditableEntity> void saveAll(final Collection<T> entities) {
        ServicesUtils.validateParameterNotEmptyStandardMessage("entities", entities);
        entities.forEach(this::save);
    }

    @Transactional
    @Override
    public <T extends AbstractAuditableEntity> void saveAllAndFlush(final Collection<T> entities) {
        saveAll(entities);
        flush();
    }

    private void flush() {
        entityManager.flush();
    }
    // endregion

    // region SEARCH
    @Transactional(readOnly = true)
    @Override
    public <T> T searchUnique(final String queryStr) {
        ServicesUtils.validateParameterNotEmptyStandardMessage("queryStr", queryStr);
        final Query query = entityManager.createQuery(queryStr);
        return (T) query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public <T> T searchUnique(final String queryStr, final Map<String, Object> params) {
        ServicesUtils.validateParameterNotEmptyStandardMessage("queryStr", queryStr);
        ServicesUtils.validateParameterNotEmptyStandardMessage("params", params);
        final Query query = entityManager.createQuery(queryStr);
        params.forEach(query::setParameter);
        return (T) query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public <T extends AbstractAuditableEntity> T searchUnique(final Class<T> entityClass, final Specification<T> spec) {
        ServicesUtils.validateParameterNotNullStandardMessage("entityClass", entityClass);
        ServicesUtils.validateParameterNotNullStandardMessage("spec", spec);
        return getRepository(entityClass).findOne(spec).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public <T extends AbstractAuditableEntity> List<T> search(final Class<T> entityClass, final Specification<T> spec) {
        ServicesUtils.validateParameterNotNullStandardMessage("entityClass", entityClass);
        ServicesUtils.validateParameterNotNullStandardMessage("spec", spec);
        return getRepository(entityClass).findAll(spec);
    }

    @Transactional(readOnly = true)
    @Override
    public <T extends AbstractAuditableEntity> List<T> search(final Class<T> entityClass, final Specification<T> spec, final Sort sort) {
        ServicesUtils.validateParameterNotNullStandardMessage("entityClass", entityClass);
        ServicesUtils.validateParameterNotNullStandardMessage("spec", spec);
        ServicesUtils.validateParameterNotNullStandardMessage("sort", sort);
        return getRepository(entityClass).findAll(spec, sort);
    }
    // endregion

    // region DELETE
    @Transactional
    @Override
    public <T> void delete(final T entity) {
        ServicesUtils.validateParameterNotNullStandardMessage("entity", entity);
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Transactional
    @Override
    public <T> void deleteAll(final Collection<T> entities) {
        ServicesUtils.validateParameterNotEmptyStandardMessage("entities", entities);
        entities.forEach(this::delete);
    }
    // endregion

    // region GET
    @Transactional(readOnly = true)
    @Override
    public <T> T getById(final Class<T> entityClass, final Object id) {
        ServicesUtils.validateParameterNotNullStandardMessage("entityClass", entityClass);
        ServicesUtils.validateParameterNotNullStandardMessage("id", id);
        return entityManager.find(entityClass, id);
    }
    // endregion

    // region MERGE
    @Transactional
    @Override
    public <T extends AbstractAuditableEntity> List<T> merge(
            final Collection<T> fetchedEntities, final Function<T, String> keyExtractor,
            final Function<Collection<String>, Collection<T>> existingEntitiesFunction, final BiConsumer<T, T> mergeConsumer) {

        final Set<String> entityKeys = fetchedEntities.stream().map(keyExtractor).collect(Collectors.toSet());
        final List<T> mergedEntities = mergeEntities(fetchedEntities, existingEntitiesFunction.apply(entityKeys),
                keyExtractor, mergeConsumer);

        if (CollectionUtils.isEmpty(mergedEntities)) {
            return Collections.emptyList();
        }

        saveAll(mergedEntities);
        return mergedEntities;
    }

    private <T extends AbstractAuditableEntity> List<T> mergeEntities(
            final Collection<T> fetchedEntities, final Collection<T> existingEntities,
            final Function<T, String> keyExtractor, final BiConsumer<T, T> mergeConsumer) {

        final Map<String, T> existingMap = existingEntities.stream()
                .collect(Collectors.toMap(keyExtractor, Function.identity()));

        final Set<T> mergedEntities = new HashSet<>();
        final Map<Boolean, List<T>> partitioned = fetchedEntities.stream()
                .collect(Collectors.partitioningBy(e -> existingMap.containsKey(keyExtractor.apply(e))));

        partitioned.get(Boolean.TRUE).forEach(source -> {
            final T existing = existingMap.get(keyExtractor.apply(source));
            mergeConsumer.accept(source, existing);
            mergedEntities.add(existing);
        });

        mergedEntities.addAll(partitioned.get(Boolean.FALSE));
        return new ArrayList<>(mergedEntities);
    }
    // endregion

    // region UTIL
    @Override
    public boolean isNew(final AbstractAuditableEntity entity) {
        String className = entity.getClass().getName();
        JpaEntityInformation<AbstractAuditableEntity, ?> info = jpaEntityInformationMap.get(className);
        if (Objects.isNull(info)) {
            info = JpaEntityInformationSupport.getEntityInformation(
                    (Class<AbstractAuditableEntity>) entity.getClass(), entityManager);
            jpaEntityInformationMap.put(className, info);
        }
        return info.isNew(entity);
    }

    private <T extends AbstractAuditableEntity> BaseRepository<T> getRepository(Class<T> entityClass) {
        String className = entityClass.getName();
        BaseRepository<T> repository = (BaseRepository<T>) repositoryMap.get(className);
        if (Objects.isNull(repository)) {
            repository = repositories.get().getRepositoryFor(entityClass).map(BaseRepository.class::cast)
                    .orElseThrow(() -> new IllegalStateException("No repository found for type " + entityClass + "!"));
            repositoryMap.put(className, repository);
        }
        return repository;
    }

    private <T> void mergeProperties(T target, T source) {
        final PersistentEntity<?, ?> entity = entities.getRequiredPersistentEntity(target.getClass());
        final MergingPropertyHandler propertyHandler = new MergingPropertyHandler(source, target, entity);
        entity.doWithAll(propertyHandler);
    }

    private class MergingPropertyHandler implements PropertyHandler { // NOSONAR
        private final PersistentPropertyAccessor<?> sourceAccessor;
        private final PersistentPropertyAccessor<?> targetAccessor;

        private MergingPropertyHandler(final Object source, final Object target, final PersistentEntity<?, ?> entity) {
            this.sourceAccessor = entity.getPropertyAccessor(source);
            this.targetAccessor = entity.getPropertyAccessor(target);
        }

        @Override
        public void doWithPersistentProperty(final PersistentProperty property) {
            if (property.isIdProperty()) {
                return;
            }

            final Optional<Object> sourceValue = Optional.ofNullable(sourceAccessor.getProperty(property));
            targetAccessor.setProperty(property, sourceValue.orElse(null));

            if (!Objects.equals(sourceAccessor.getProperty(property), targetAccessor.getProperty(property))) {
                throw new BaseException(String.format("Source [%s=%s] and target [%s=%s] is not identical!", property.getName(),
                        sourceAccessor.getProperty(property), property.getName(), targetAccessor.getProperty(property)));
            }
        }
    }
}
