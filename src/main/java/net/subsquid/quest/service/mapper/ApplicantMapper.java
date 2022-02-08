package net.subsquid.quest.service.mapper;

import net.subsquid.quest.domain.Applicant;
import net.subsquid.quest.service.dto.ApplicantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Applicant} and its DTO {@link ApplicantDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestMapper.class })
public interface ApplicantMapper extends EntityMapper<ApplicantDTO, Applicant> {
    @Mapping(target = "quests", source = "quests", qualifiedByName = "titleSet")
    ApplicantDTO toDto(Applicant s);

    @Mapping(target = "removeQuest", ignore = true)
    Applicant toEntity(ApplicantDTO applicantDTO);
}
