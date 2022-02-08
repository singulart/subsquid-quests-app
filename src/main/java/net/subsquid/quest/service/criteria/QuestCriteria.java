package net.subsquid.quest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import net.subsquid.quest.domain.enumeration.QuestStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link net.subsquid.quest.domain.Quest} entity. This class is used
 * in {@link net.subsquid.quest.web.rest.QuestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QuestStatus
     */
    public static class QuestStatusFilter extends Filter<QuestStatus> {

        public QuestStatusFilter() {}

        public QuestStatusFilter(QuestStatusFilter filter) {
            super(filter);
        }

        @Override
        public QuestStatusFilter copy() {
            return new QuestStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter reward;

    private LocalDateFilter expiresOn;

    private LocalDateFilter reviewStartDate;

    private IntegerFilter maxApplicants;

    private StringFilter assignee;

    private QuestStatusFilter status;

    private StringFilter privateNotes;

    private Boolean distinct;

    public QuestCriteria() {}

    public QuestCriteria(QuestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.reward = other.reward == null ? null : other.reward.copy();
        this.expiresOn = other.expiresOn == null ? null : other.expiresOn.copy();
        this.reviewStartDate = other.reviewStartDate == null ? null : other.reviewStartDate.copy();
        this.maxApplicants = other.maxApplicants == null ? null : other.maxApplicants.copy();
        this.assignee = other.assignee == null ? null : other.assignee.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.privateNotes = other.privateNotes == null ? null : other.privateNotes.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestCriteria copy() {
        return new QuestCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getReward() {
        return reward;
    }

    public StringFilter reward() {
        if (reward == null) {
            reward = new StringFilter();
        }
        return reward;
    }

    public void setReward(StringFilter reward) {
        this.reward = reward;
    }

    public LocalDateFilter getExpiresOn() {
        return expiresOn;
    }

    public LocalDateFilter expiresOn() {
        if (expiresOn == null) {
            expiresOn = new LocalDateFilter();
        }
        return expiresOn;
    }

    public void setExpiresOn(LocalDateFilter expiresOn) {
        this.expiresOn = expiresOn;
    }

    public LocalDateFilter getReviewStartDate() {
        return reviewStartDate;
    }

    public LocalDateFilter reviewStartDate() {
        if (reviewStartDate == null) {
            reviewStartDate = new LocalDateFilter();
        }
        return reviewStartDate;
    }

    public void setReviewStartDate(LocalDateFilter reviewStartDate) {
        this.reviewStartDate = reviewStartDate;
    }

    public IntegerFilter getMaxApplicants() {
        return maxApplicants;
    }

    public IntegerFilter maxApplicants() {
        if (maxApplicants == null) {
            maxApplicants = new IntegerFilter();
        }
        return maxApplicants;
    }

    public void setMaxApplicants(IntegerFilter maxApplicants) {
        this.maxApplicants = maxApplicants;
    }

    public StringFilter getAssignee() {
        return assignee;
    }

    public StringFilter assignee() {
        if (assignee == null) {
            assignee = new StringFilter();
        }
        return assignee;
    }

    public void setAssignee(StringFilter assignee) {
        this.assignee = assignee;
    }

    public QuestStatusFilter getStatus() {
        return status;
    }

    public QuestStatusFilter status() {
        if (status == null) {
            status = new QuestStatusFilter();
        }
        return status;
    }

    public void setStatus(QuestStatusFilter status) {
        this.status = status;
    }

    public StringFilter getPrivateNotes() {
        return privateNotes;
    }

    public StringFilter privateNotes() {
        if (privateNotes == null) {
            privateNotes = new StringFilter();
        }
        return privateNotes;
    }

    public void setPrivateNotes(StringFilter privateNotes) {
        this.privateNotes = privateNotes;
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
        final QuestCriteria that = (QuestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(reward, that.reward) &&
            Objects.equals(expiresOn, that.expiresOn) &&
            Objects.equals(reviewStartDate, that.reviewStartDate) &&
            Objects.equals(maxApplicants, that.maxApplicants) &&
            Objects.equals(assignee, that.assignee) &&
            Objects.equals(status, that.status) &&
            Objects.equals(privateNotes, that.privateNotes) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            reward,
            expiresOn,
            reviewStartDate,
            maxApplicants,
            assignee,
            status,
            privateNotes,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (reward != null ? "reward=" + reward + ", " : "") +
            (expiresOn != null ? "expiresOn=" + expiresOn + ", " : "") +
            (reviewStartDate != null ? "reviewStartDate=" + reviewStartDate + ", " : "") +
            (maxApplicants != null ? "maxApplicants=" + maxApplicants + ", " : "") +
            (assignee != null ? "assignee=" + assignee + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (privateNotes != null ? "privateNotes=" + privateNotes + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
