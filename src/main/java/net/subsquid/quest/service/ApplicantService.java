package net.subsquid.quest.service;

import java.util.Optional;
import net.subsquid.quest.domain.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Applicant}.
 */
public interface ApplicantService {
    /**
     * Save a applicant.
     *
     * @param applicant the entity to save.
     * @return the persisted entity.
     */
    Applicant save(Applicant applicant);

    /**
     * Partially updates a applicant.
     *
     * @param applicant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Applicant> partialUpdate(Applicant applicant);

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Applicant> findAll(Pageable pageable);

    /**
     * Get the "id" applicant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Applicant> findOne(Long id);

    /**
     * Delete the "id" applicant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
