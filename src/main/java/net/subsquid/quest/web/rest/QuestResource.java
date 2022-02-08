package net.subsquid.quest.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.subsquid.quest.repository.QuestRepository;
import net.subsquid.quest.service.QuestQueryService;
import net.subsquid.quest.service.QuestService;
import net.subsquid.quest.service.criteria.QuestCriteria;
import net.subsquid.quest.service.dto.QuestDTO;
import net.subsquid.quest.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link net.subsquid.quest.domain.Quest}.
 */
@RestController
@RequestMapping("/api")
public class QuestResource {

    private final Logger log = LoggerFactory.getLogger(QuestResource.class);

    private static final String ENTITY_NAME = "quest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestService questService;

    private final QuestRepository questRepository;

    private final QuestQueryService questQueryService;

    public QuestResource(QuestService questService, QuestRepository questRepository, QuestQueryService questQueryService) {
        this.questService = questService;
        this.questRepository = questRepository;
        this.questQueryService = questQueryService;
    }

    /**
     * {@code POST  /quests} : Create a new quest.
     *
     * @param questDTO the questDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questDTO, or with status {@code 400 (Bad Request)} if the quest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quests")
    public ResponseEntity<QuestDTO> createQuest(@Valid @RequestBody QuestDTO questDTO) throws URISyntaxException {
        log.debug("REST request to save Quest : {}", questDTO);
        if (questDTO.getId() != null) {
            throw new BadRequestAlertException("A new quest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestDTO result = questService.save(questDTO);
        return ResponseEntity
            .created(new URI("/api/quests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quests/:id} : Updates an existing quest.
     *
     * @param id the id of the questDTO to save.
     * @param questDTO the questDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questDTO,
     * or with status {@code 400 (Bad Request)} if the questDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quests/{id}")
    public ResponseEntity<QuestDTO> updateQuest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuestDTO questDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Quest : {}, {}", id, questDTO);
        if (questDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestDTO result = questService.save(questDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quests/:id} : Partial updates given fields of an existing quest, field will ignore if it is null
     *
     * @param id the id of the questDTO to save.
     * @param questDTO the questDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questDTO,
     * or with status {@code 400 (Bad Request)} if the questDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestDTO> partialUpdateQuest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuestDTO questDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quest partially : {}, {}", id, questDTO);
        if (questDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestDTO> result = questService.partialUpdate(questDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quests} : get all the quests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quests in body.
     */
    @GetMapping("/quests")
    public ResponseEntity<List<QuestDTO>> getAllQuests(
        QuestCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Quests by criteria: {}", criteria);
        Page<QuestDTO> page = questQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quests/count} : count all the quests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/quests/count")
    public ResponseEntity<Long> countQuests(QuestCriteria criteria) {
        log.debug("REST request to count Quests by criteria: {}", criteria);
        return ResponseEntity.ok().body(questQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quests/:id} : get the "id" quest.
     *
     * @param id the id of the questDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quests/{id}")
    public ResponseEntity<QuestDTO> getQuest(@PathVariable Long id) {
        log.debug("REST request to get Quest : {}", id);
        Optional<QuestDTO> questDTO = questService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questDTO);
    }

    /**
     * {@code DELETE  /quests/:id} : delete the "id" quest.
     *
     * @param id the id of the questDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quests/{id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable Long id) {
        log.debug("REST request to delete Quest : {}", id);
        questService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
