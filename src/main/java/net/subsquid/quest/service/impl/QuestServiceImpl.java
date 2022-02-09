package net.subsquid.quest.service.impl;

import java.util.Optional;
import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.repository.QuestRepository;
import net.subsquid.quest.service.QuestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Quest}.
 */
@Service
@Transactional
public class QuestServiceImpl implements QuestService {

    private final Logger log = LoggerFactory.getLogger(QuestServiceImpl.class);

    private final QuestRepository questRepository;

    public QuestServiceImpl(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    @Override
    public Quest save(Quest quest) {
        log.debug("Request to save Quest : {}", quest);
        return questRepository.save(quest);
    }

    @Override
    public Optional<Quest> partialUpdate(Quest quest) {
        log.debug("Request to partially update Quest : {}", quest);

        return questRepository
            .findById(quest.getId())
            .map(existingQuest -> {
                if (quest.getTitle() != null) {
                    existingQuest.setTitle(quest.getTitle());
                }
                if (quest.getDescription() != null) {
                    existingQuest.setDescription(quest.getDescription());
                }
                if (quest.getReward() != null) {
                    existingQuest.setReward(quest.getReward());
                }
                if (quest.getExpiresOn() != null) {
                    existingQuest.setExpiresOn(quest.getExpiresOn());
                }
                if (quest.getReviewStartDate() != null) {
                    existingQuest.setReviewStartDate(quest.getReviewStartDate());
                }
                if (quest.getMaxApplicants() != null) {
                    existingQuest.setMaxApplicants(quest.getMaxApplicants());
                }
                if (quest.getAssignee() != null) {
                    existingQuest.setAssignee(quest.getAssignee());
                }
                if (quest.getStatus() != null) {
                    existingQuest.setStatus(quest.getStatus());
                }
                if (quest.getPrivateNotes() != null) {
                    existingQuest.setPrivateNotes(quest.getPrivateNotes());
                }

                return existingQuest;
            })
            .map(questRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Quest> findAll(Pageable pageable) {
        log.debug("Request to get all Quests");
        return questRepository.findAll(pageable);
    }

    public Page<Quest> findAllWithEagerRelationships(Pageable pageable) {
        return questRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quest> findOne(Long id) {
        log.debug("Request to get Quest : {}", id);
        return questRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Quest : {}", id);
        questRepository.deleteById(id);
    }
}
