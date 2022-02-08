package net.subsquid.quest.service.mapper;

import java.util.Set;
import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.service.dto.QuestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quest} and its DTO {@link QuestDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestMapper extends EntityMapper<QuestDTO, Quest> {
    @Named("titleSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    Set<QuestDTO> toDtoTitleSet(Set<Quest> quest);
}
