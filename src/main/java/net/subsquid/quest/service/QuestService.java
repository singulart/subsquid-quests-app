package net.subsquid.quest.service;

import java.util.Optional;
import net.subsquid.quest.service.dto.QuestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link net.subsquid.quest.domain.Quest}.
 */
public interface QuestService {
    /**
     * Save a quest.
     *
     * @param questDTO the entity to save.
     * @return the persisted entity.
     */
    QuestDTO save(QuestDTO questDTO);

    /**
     * Partially updates a quest.
     *
     * @param questDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestDTO> partialUpdate(QuestDTO questDTO);

    /**
     * Get all the quests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" quest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestDTO> findOne(Long id);

    /**
     * Delete the "id" quest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
