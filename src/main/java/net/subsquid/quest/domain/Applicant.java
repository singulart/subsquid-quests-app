package net.subsquid.quest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Applicant.
 */
@Entity
@Table(name = "applicant")
public class Applicant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "discord_handle", nullable = false)
    private String discordHandle;

    @ManyToMany
    @JoinTable(
        name = "rel_quest__applicant",
        joinColumns = @JoinColumn(name = "applicant_id"),
        inverseJoinColumns = @JoinColumn(name = "quest_id")
    )
    @JsonIgnoreProperties(value = { "applicants" }, allowSetters = true)
    private Set<Quest> quests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Applicant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiscordHandle() {
        return this.discordHandle;
    }

    public Applicant discordHandle(String discordHandle) {
        this.setDiscordHandle(discordHandle);
        return this;
    }

    public void setDiscordHandle(String discordHandle) {
        this.discordHandle = discordHandle;
    }

    public Set<Quest> getQuests() {
        return this.quests;
    }

    public void setQuests(Set<Quest> quests) {
        this.quests = quests;
    }

    public Applicant quests(Set<Quest> quests) {
        this.setQuests(quests);
        return this;
    }

    public Applicant addQuest(Quest quest) {
        this.quests.add(quest);
        quest.getApplicants().add(this);
        return this;
    }

    public Applicant removeQuest(Quest quest) {
        this.quests.remove(quest);
        quest.getApplicants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Applicant)) {
            return false;
        }
        return id != null && id.equals(((Applicant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Applicant{" +
            "id=" + getId() +
            ", discordHandle='" + getDiscordHandle() + "'" +
            "}";
    }
}
