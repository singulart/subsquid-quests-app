package net.subsquid.quest.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;
import net.subsquid.quest.domain.enumeration.QuestStatus;

/**
 * A DTO for the {@link net.subsquid.quest.domain.Quest} entity.
 */
public class QuestDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private String reward;

    @NotNull
    private LocalDate expiresOn;

    @NotNull
    private LocalDate reviewStartDate;

    @NotNull
    @Min(value = 0)
    private Integer maxApplicants;

    private String assignee;

    private QuestStatus status;

    private String privateNotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public LocalDate getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(LocalDate expiresOn) {
        this.expiresOn = expiresOn;
    }

    public LocalDate getReviewStartDate() {
        return reviewStartDate;
    }

    public void setReviewStartDate(LocalDate reviewStartDate) {
        this.reviewStartDate = reviewStartDate;
    }

    public Integer getMaxApplicants() {
        return maxApplicants;
    }

    public void setMaxApplicants(Integer maxApplicants) {
        this.maxApplicants = maxApplicants;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestDTO)) {
            return false;
        }

        QuestDTO questDTO = (QuestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", reward='" + getReward() + "'" +
            ", expiresOn='" + getExpiresOn() + "'" +
            ", reviewStartDate='" + getReviewStartDate() + "'" +
            ", maxApplicants=" + getMaxApplicants() +
            ", assignee='" + getAssignee() + "'" +
            ", status='" + getStatus() + "'" +
            ", privateNotes='" + getPrivateNotes() + "'" +
            "}";
    }
}
