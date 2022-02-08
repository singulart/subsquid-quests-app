package net.subsquid.quest.service;

import java.util.Optional;
import net.subsquid.quest.service.dto.ApplicantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link net.subsquid.quest.domain.Applicant}.
 */
public interface ApplicantService {
    /**
     * Save a applicant.
     *
     * @param applicantDTO the entity to save.
     * @return the persisted entity.
     */
    ApplicantDTO save(ApplicantDTO applicantDTO);

    /**
     * Partially updates a applicant.
     *
     * @param applicantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ApplicantDTO> partialUpdate(ApplicantDTO applicantDTO);

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApplicantDTO> findAll(Pageable pageable);

    /**
     * Get all the applicants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApplicantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" applicant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApplicantDTO> findOne(Long id);

    /**
     * Delete the "id" applicant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
