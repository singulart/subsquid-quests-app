package net.subsquid.quest.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.repository.QuestRepository;
import net.subsquid.quest.service.QuestService;
import net.subsquid.quest.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    public QuestResource(QuestService questService, QuestRepository questRepository) {
        this.questService = questService;
        this.questRepository = questRepository;
    }

    /**
     * {@code POST  /quests} : Create a new quest.
     *
     * @param quest the quest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quest, or with status {@code 400 (Bad Request)} if the quest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quests")
    public ResponseEntity<Quest> createQuest(@Valid @RequestBody Quest quest) throws URISyntaxException {
        log.debug("REST request to save Quest : {}", quest);
        if (quest.getId() != null) {
            throw new BadRequestAlertException("A new quest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Quest result = questService.save(quest);
        return ResponseEntity
            .created(new URI("/api/quests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quests/:id} : Updates an existing quest.
     *
     * @param id the id of the quest to save.
     * @param quest the quest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quest,
     * or with status {@code 400 (Bad Request)} if the quest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quests/{id}")
    public ResponseEntity<Quest> updateQuest(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Quest quest)
        throws URISyntaxException {
        log.debug("REST request to update Quest : {}, {}", id, quest);
        if (quest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Quest result = questService.save(quest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quests/:id} : Partial updates given fields of an existing quest, field will ignore if it is null
     *
     * @param id the id of the quest to save.
     * @param quest the quest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quest,
     * or with status {@code 400 (Bad Request)} if the quest is not valid,
     * or with status {@code 404 (Not Found)} if the quest is not found,
     * or with status {@code 500 (Internal Server Error)} if the quest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Quest> partialUpdateQuest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Quest quest
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quest partially : {}, {}", id, quest);
        if (quest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Quest> result = questService.partialUpdate(quest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quest.getId().toString())
        );
    }

    /**
     * {@code GET  /quests} : get all the quests.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quests in body.
     */
    @GetMapping("/quests")
    public ResponseEntity<List<Quest>> getAllQuests(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Quests");
        Page<Quest> page;
        if (eagerload) {
            page = questService.findAllWithEagerRelationships(pageable);
        } else {
            page = questService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quests/:id} : get the "id" quest.
     *
     * @param id the id of the quest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quests/{id}")
    public ResponseEntity<Quest> getQuest(@PathVariable Long id) {
        log.debug("REST request to get Quest : {}", id);
        Optional<Quest> quest = questService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quest);
    }

    /**
     * {@code DELETE  /quests/:id} : delete the "id" quest.
     *
     * @param id the id of the quest to delete.
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
