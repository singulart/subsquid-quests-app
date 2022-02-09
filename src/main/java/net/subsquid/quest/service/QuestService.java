package net.subsquid.quest.service;

import java.util.Optional;
import net.subsquid.quest.domain.Quest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Quest}.
 */
public interface QuestService {
    /**
     * Save a quest.
     *
     * @param quest the entity to save.
     * @return the persisted entity.
     */
    Quest save(Quest quest);

    /**
     * Partially updates a quest.
     *
     * @param quest the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Quest> partialUpdate(Quest quest);

    /**
     * Get all the quests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Quest> findAll(Pageable pageable);

    /**
     * Get all the quests with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Quest> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" quest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Quest> findOne(Long id);

    /**
     * Delete the "id" quest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
