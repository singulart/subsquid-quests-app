package net.subsquid.quest.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import net.subsquid.quest.domain.enumeration.QuestStatus;

/**
 * A Quest.
 */
@Entity
@Table(name = "quest")
public class Quest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "reward", nullable = false)
    private String reward;

    @NotNull
    @Column(name = "expires_on", nullable = false)
    private LocalDate expiresOn;

    @NotNull
    @Column(name = "review_start_date", nullable = false)
    private LocalDate reviewStartDate;

    @NotNull
    @Min(value = 1)
    @Column(name = "max_applicants", nullable = false)
    private Integer maxApplicants;

    @Column(name = "assignee")
    private String assignee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuestStatus status;

    @Column(name = "private_notes")
    private String privateNotes;

    @ManyToMany
    @JoinTable(
        name = "rel_quest__applicant",
        joinColumns = @JoinColumn(name = "quest_id"),
        inverseJoinColumns = @JoinColumn(name = "applicant_id")
    )
    private Set<Applicant> applicants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Quest title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Quest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReward() {
        return this.reward;
    }

    public Quest reward(String reward) {
        this.setReward(reward);
        return this;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public LocalDate getExpiresOn() {
        return this.expiresOn;
    }

    public Quest expiresOn(LocalDate expiresOn) {
        this.setExpiresOn(expiresOn);
        return this;
    }

    public void setExpiresOn(LocalDate expiresOn) {
        this.expiresOn = expiresOn;
    }

    public LocalDate getReviewStartDate() {
        return this.reviewStartDate;
    }

    public Quest reviewStartDate(LocalDate reviewStartDate) {
        this.setReviewStartDate(reviewStartDate);
        return this;
    }

    public void setReviewStartDate(LocalDate reviewStartDate) {
        this.reviewStartDate = reviewStartDate;
    }

    public Integer getMaxApplicants() {
        return this.maxApplicants;
    }

    public Quest maxApplicants(Integer maxApplicants) {
        this.setMaxApplicants(maxApplicants);
        return this;
    }

    public void setMaxApplicants(Integer maxApplicants) {
        this.maxApplicants = maxApplicants;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public Quest assignee(String assignee) {
        this.setAssignee(assignee);
        return this;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public QuestStatus getStatus() {
        return this.status;
    }

    public Quest status(QuestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
    }

    public String getPrivateNotes() {
        return this.privateNotes;
    }

    public Quest privateNotes(String privateNotes) {
        this.setPrivateNotes(privateNotes);
        return this;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    public Set<Applicant> getApplicants() {
        return this.applicants;
    }

    public void setApplicants(Set<Applicant> applicants) {
        this.applicants = applicants;
    }

    public Quest applicants(Set<Applicant> applicants) {
        this.setApplicants(applicants);
        return this;
    }

    public Quest addApplicant(Applicant applicant) {
        this.applicants.add(applicant);
        return this;
    }

    public Quest removeApplicant(Applicant applicant) {
        this.applicants.remove(applicant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quest)) {
            return false;
        }
        return id != null && id.equals(((Quest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quest{" +
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
