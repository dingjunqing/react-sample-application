package com.dintal.react.web.rest;

import com.dintal.react.domain.AIE;
import com.dintal.react.repository.AIERepository;
import com.dintal.react.service.AIEService;
import com.dintal.react.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.dintal.react.domain.AIE}.
 */
@RestController
@RequestMapping("/api/aies")
public class AIEResource {

    private static final Logger LOG = LoggerFactory.getLogger(AIEResource.class);

    private static final String ENTITY_NAME = "aIE";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AIEService aIEService;

    private final AIERepository aIERepository;

    public AIEResource(AIEService aIEService, AIERepository aIERepository) {
        this.aIEService = aIEService;
        this.aIERepository = aIERepository;
    }

    /**
     * {@code POST  /aies} : Create a new aIE.
     *
     * @param aIE the aIE to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aIE, or with status {@code 400 (Bad Request)} if the aIE has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AIE>> createAIE(@Valid @RequestBody AIE aIE) throws URISyntaxException {
        LOG.debug("REST request to save AIE : {}", aIE);
        if (aIE.getId() != null) {
            throw new BadRequestAlertException("A new aIE cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aIE.setId(UUID.randomUUID());
        return aIEService
            .save(aIE)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/aies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /aies/:id} : Updates an existing aIE.
     *
     * @param id the id of the aIE to save.
     * @param aIE the aIE to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIE,
     * or with status {@code 400 (Bad Request)} if the aIE is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aIE couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AIE>> updateAIE(@PathVariable(value = "id", required = false) final String id, @Valid @RequestBody AIE aIE)
        throws URISyntaxException {
        LOG.debug("REST request to update AIE : {}, {}", id, aIE);
        if (aIE.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIE.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aIERepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return aIEService
                    .update(aIE)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /aies/:id} : Partial updates given fields of an existing aIE, field will ignore if it is null
     *
     * @param id the id of the aIE to save.
     * @param aIE the aIE to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIE,
     * or with status {@code 400 (Bad Request)} if the aIE is not valid,
     * or with status {@code 404 (Not Found)} if the aIE is not found,
     * or with status {@code 500 (Internal Server Error)} if the aIE couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AIE>> partialUpdateAIE(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AIE aIE
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AIE partially : {}, {}", id, aIE);
        if (aIE.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIE.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aIERepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AIE> result = aIEService.partialUpdate(aIE);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /aies} : get all the aIES.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aIES in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AIE>>> getAllAIES(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of AIES");
        return aIEService
            .countAll()
            .zipWith(aIEService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /aies/:id} : get the "id" aIE.
     *
     * @param id the id of the aIE to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aIE, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AIE>> getAIE(@PathVariable("id") String id) {
        LOG.debug("REST request to get AIE : {}", id);
        Mono<AIE> aIE = aIEService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aIE);
    }

    /**
     * {@code DELETE  /aies/:id} : delete the "id" aIE.
     *
     * @param id the id of the aIE to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAIE(@PathVariable("id") String id) {
        LOG.debug("REST request to delete AIE : {}", id);
        return aIEService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
