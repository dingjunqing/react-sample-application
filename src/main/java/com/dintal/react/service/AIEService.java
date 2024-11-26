package com.dintal.react.service;

import com.dintal.react.domain.AIE;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.dintal.react.domain.AIE}.
 */
public interface AIEService {
    /**
     * Save a aIE.
     *
     * @param aIE the entity to save.
     * @return the persisted entity.
     */
    Mono<AIE> save(AIE aIE);

    /**
     * Updates a aIE.
     *
     * @param aIE the entity to update.
     * @return the persisted entity.
     */
    Mono<AIE> update(AIE aIE);

    /**
     * Partially updates a aIE.
     *
     * @param aIE the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AIE> partialUpdate(AIE aIE);

    /**
     * Get all the aIES.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AIE> findAll(Pageable pageable);

    /**
     * Returns the number of aIES available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" aIE.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AIE> findOne(String id);

    /**
     * Delete the "id" aIE.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
