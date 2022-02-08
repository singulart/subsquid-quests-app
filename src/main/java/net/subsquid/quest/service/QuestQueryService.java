package net.subsquid.quest.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import net.subsquid.quest.domain.*; // for static metamodels
import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.repository.QuestRepository;
import net.subsquid.quest.service.criteria.QuestCriteria;
import net.subsquid.quest.service.dto.QuestDTO;
import net.subsquid.quest.service.mapper.QuestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Quest} entities in the database.
 * The main input is a {@link QuestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestDTO} or a {@link Page} of {@link QuestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestQueryService extends QueryService<Quest> {

    private final Logger log = LoggerFactory.getLogger(QuestQueryService.class);

    private final QuestRepository questRepository;

    private final QuestMapper questMapper;

    public QuestQueryService(QuestRepository questRepository, QuestMapper questMapper) {
        this.questRepository = questRepository;
        this.questMapper = questMapper;
    }

    /**
     * Return a {@link List} of {@link QuestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestDTO> findByCriteria(QuestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Quest> specification = createSpecification(criteria);
        return questMapper.toDto(questRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestDTO> findByCriteria(QuestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quest> specification = createSpecification(criteria);
        return questRepository.findAll(specification, page).map(questMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Quest> specification = createSpecification(criteria);
        return questRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quest> createSpecification(QuestCriteria criteria) {
        Specification<Quest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Quest_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Quest_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Quest_.description));
            }
            if (criteria.getReward() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReward(), Quest_.reward));
            }
            if (criteria.getExpiresOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiresOn(), Quest_.expiresOn));
            }
            if (criteria.getReviewStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewStartDate(), Quest_.reviewStartDate));
            }
            if (criteria.getMaxApplicants() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxApplicants(), Quest_.maxApplicants));
            }
            if (criteria.getAssignee() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssignee(), Quest_.assignee));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Quest_.status));
            }
            if (criteria.getPrivateNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrivateNotes(), Quest_.privateNotes));
            }
        }
        return specification;
    }
}
