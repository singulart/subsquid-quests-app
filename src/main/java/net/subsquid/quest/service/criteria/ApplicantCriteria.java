package net.subsquid.quest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link net.subsquid.quest.domain.Applicant} entity. This class is used
 * in {@link net.subsquid.quest.web.rest.ApplicantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /applicants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter discordHandle;

    private LongFilter questId;

    private Boolean distinct;

    public ApplicantCriteria() {}

    public ApplicantCriteria(ApplicantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.discordHandle = other.discordHandle == null ? null : other.discordHandle.copy();
        this.questId = other.questId == null ? null : other.questId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ApplicantCriteria copy() {
        return new ApplicantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDiscordHandle() {
        return discordHandle;
    }

    public StringFilter discordHandle() {
        if (discordHandle == null) {
            discordHandle = new StringFilter();
        }
        return discordHandle;
    }

    public void setDiscordHandle(StringFilter discordHandle) {
        this.discordHandle = discordHandle;
    }

    public LongFilter getQuestId() {
        return questId;
    }

    public LongFilter questId() {
        if (questId == null) {
            questId = new LongFilter();
        }
        return questId;
    }

    public void setQuestId(LongFilter questId) {
        this.questId = questId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicantCriteria that = (ApplicantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(discordHandle, that.discordHandle) &&
            Objects.equals(questId, that.questId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discordHandle, questId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicantCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (discordHandle != null ? "discordHandle=" + discordHandle + ", " : "") +
            (questId != null ? "questId=" + questId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
