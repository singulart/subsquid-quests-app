package net.subsquid.quest.service.mapper;

import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.service.dto.QuestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quest} and its DTO {@link QuestDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestMapper extends EntityMapper<QuestDTO, Quest> {}
