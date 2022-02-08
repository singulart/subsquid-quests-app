package net.subsquid.quest.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link net.subsquid.quest.domain.Applicant} entity.
 */
public class ApplicantDTO implements Serializable {

    private Long id;

    @NotNull
    private String discordHandle;

    private Set<QuestDTO> quests = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiscordHandle() {
        return discordHandle;
    }

    public void setDiscordHandle(String discordHandle) {
        this.discordHandle = discordHandle;
    }

    public Set<QuestDTO> getQuests() {
        return quests;
    }

    public void setQuests(Set<QuestDTO> quests) {
        this.quests = quests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicantDTO)) {
            return false;
        }

        ApplicantDTO applicantDTO = (ApplicantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicantDTO{" +
            "id=" + getId() +
            ", discordHandle='" + getDiscordHandle() + "'" +
            ", quests=" + getQuests() +
            "}";
    }
}
