package net.subsquid.quest.service.impl;

import java.util.Optional;
import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.repository.QuestRepository;
import net.subsquid.quest.service.QuestService;
import net.subsquid.quest.service.dto.QuestDTO;
import net.subsquid.quest.service.mapper.QuestMapper;
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

    private final QuestMapper questMapper;

    public QuestServiceImpl(QuestRepository questRepository, QuestMapper questMapper) {
        this.questRepository = questRepository;
        this.questMapper = questMapper;
    }

    @Override
    public QuestDTO save(QuestDTO questDTO) {
        log.debug("Request to save Quest : {}", questDTO);
        Quest quest = questMapper.toEntity(questDTO);
        quest = questRepository.save(quest);
        return questMapper.toDto(quest);
    }

    @Override
    public Optional<QuestDTO> partialUpdate(QuestDTO questDTO) {
        log.debug("Request to partially update Quest : {}", questDTO);

        return questRepository
            .findById(questDTO.getId())
            .map(existingQuest -> {
                questMapper.partialUpdate(existingQuest, questDTO);

                return existingQuest;
            })
            .map(questRepository::save)
            .map(questMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Quests");
        return questRepository.findAll(pageable).map(questMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestDTO> findOne(Long id) {
        log.debug("Request to get Quest : {}", id);
        return questRepository.findById(id).map(questMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Quest : {}", id);
        questRepository.deleteById(id);
    }
}
