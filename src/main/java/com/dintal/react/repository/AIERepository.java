package com.dintal.react.repository;

import com.dintal.react.domain.AIE;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AIE entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AIERepository extends ReactiveCrudRepository<AIE, String>, AIERepositoryInternal {
    Flux<AIE> findAllBy(Pageable pageable);

    @Override
    <S extends AIE> Mono<S> save(S entity);

    @Override
    Flux<AIE> findAll();

    @Override
    Mono<AIE> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface AIERepositoryInternal {
    <S extends AIE> Mono<S> save(S entity);

    Flux<AIE> findAllBy(Pageable pageable);

    Flux<AIE> findAll();

    Mono<AIE> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AIE> findAllBy(Pageable pageable, Criteria criteria);
}
