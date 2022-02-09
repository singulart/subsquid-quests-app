package net.subsquid.quest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import net.subsquid.quest.IntegrationTest;
import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.domain.enumeration.QuestStatus;
import net.subsquid.quest.repository.QuestRepository;
import net.subsquid.quest.service.QuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REWARD = "AAAAAAAAAA";
    private static final String UPDATED_REWARD = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRES_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRES_ON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_REVIEW_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REVIEW_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_MAX_APPLICANTS = 1;
    private static final Integer UPDATED_MAX_APPLICANTS = 2;

    private static final String DEFAULT_ASSIGNEE = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE = "BBBBBBBBBB";

    private static final QuestStatus DEFAULT_STATUS = QuestStatus.OPEN;
    private static final QuestStatus UPDATED_STATUS = QuestStatus.CLAIMED;

    private static final String DEFAULT_PRIVATE_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_PRIVATE_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestRepository questRepository;

    @Mock
    private QuestRepository questRepositoryMock;

    @Mock
    private QuestService questServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestMockMvc;

    private Quest quest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quest createEntity(EntityManager em) {
        Quest quest = new Quest()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .reward(DEFAULT_REWARD)
            .expiresOn(DEFAULT_EXPIRES_ON)
            .reviewStartDate(DEFAULT_REVIEW_START_DATE)
            .maxApplicants(DEFAULT_MAX_APPLICANTS)
            .assignee(DEFAULT_ASSIGNEE)
            .status(DEFAULT_STATUS)
            .privateNotes(DEFAULT_PRIVATE_NOTES);
        return quest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quest createUpdatedEntity(EntityManager em) {
        Quest quest = new Quest()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .reward(UPDATED_REWARD)
            .expiresOn(UPDATED_EXPIRES_ON)
            .reviewStartDate(UPDATED_REVIEW_START_DATE)
            .maxApplicants(UPDATED_MAX_APPLICANTS)
            .assignee(UPDATED_ASSIGNEE)
            .status(UPDATED_STATUS)
            .privateNotes(UPDATED_PRIVATE_NOTES);
        return quest;
    }

    @BeforeEach
    public void initTest() {
        quest = createEntity(em);
    }

    @Test
    @Transactional
    void createQuest() throws Exception {
        int databaseSizeBeforeCreate = questRepository.findAll().size();
        // Create the Quest
        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isCreated());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeCreate + 1);
        Quest testQuest = questList.get(questList.size() - 1);
        assertThat(testQuest.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuest.getReward()).isEqualTo(DEFAULT_REWARD);
        assertThat(testQuest.getExpiresOn()).isEqualTo(DEFAULT_EXPIRES_ON);
        assertThat(testQuest.getReviewStartDate()).isEqualTo(DEFAULT_REVIEW_START_DATE);
        assertThat(testQuest.getMaxApplicants()).isEqualTo(DEFAULT_MAX_APPLICANTS);
        assertThat(testQuest.getAssignee()).isEqualTo(DEFAULT_ASSIGNEE);
        assertThat(testQuest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuest.getPrivateNotes()).isEqualTo(DEFAULT_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void createQuestWithExistingId() throws Exception {
        // Create the Quest with an existing ID
        quest.setId(1L);

        int databaseSizeBeforeCreate = questRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isBadRequest());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setTitle(null);

        // Create the Quest, which fails.

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRewardIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setReward(null);

        // Create the Quest, which fails.

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiresOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setExpiresOn(null);

        // Create the Quest, which fails.

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setReviewStartDate(null);

        // Create the Quest, which fails.

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxApplicantsIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setMaxApplicants(null);

        // Create the Quest, which fails.

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setStatus(null);

        // Create the Quest, which fails.

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuests() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList
        restQuestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quest.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reward").value(hasItem(DEFAULT_REWARD)))
            .andExpect(jsonPath("$.[*].expiresOn").value(hasItem(DEFAULT_EXPIRES_ON.toString())))
            .andExpect(jsonPath("$.[*].reviewStartDate").value(hasItem(DEFAULT_REVIEW_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxApplicants").value(hasItem(DEFAULT_MAX_APPLICANTS)))
            .andExpect(jsonPath("$.[*].assignee").value(hasItem(DEFAULT_ASSIGNEE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].privateNotes").value(hasItem(DEFAULT_PRIVATE_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(questServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getQuest() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get the quest
        restQuestMockMvc
            .perform(get(ENTITY_API_URL_ID, quest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quest.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.reward").value(DEFAULT_REWARD))
            .andExpect(jsonPath("$.expiresOn").value(DEFAULT_EXPIRES_ON.toString()))
            .andExpect(jsonPath("$.reviewStartDate").value(DEFAULT_REVIEW_START_DATE.toString()))
            .andExpect(jsonPath("$.maxApplicants").value(DEFAULT_MAX_APPLICANTS))
            .andExpect(jsonPath("$.assignee").value(DEFAULT_ASSIGNEE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.privateNotes").value(DEFAULT_PRIVATE_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingQuest() throws Exception {
        // Get the quest
        restQuestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuest() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        int databaseSizeBeforeUpdate = questRepository.findAll().size();

        // Update the quest
        Quest updatedQuest = questRepository.findById(quest.getId()).get();
        // Disconnect from session so that the updates on updatedQuest are not directly saved in db
        em.detach(updatedQuest);
        updatedQuest
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .reward(UPDATED_REWARD)
            .expiresOn(UPDATED_EXPIRES_ON)
            .reviewStartDate(UPDATED_REVIEW_START_DATE)
            .maxApplicants(UPDATED_MAX_APPLICANTS)
            .assignee(UPDATED_ASSIGNEE)
            .status(UPDATED_STATUS)
            .privateNotes(UPDATED_PRIVATE_NOTES);

        restQuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuest))
            )
            .andExpect(status().isOk());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
        Quest testQuest = questList.get(questList.size() - 1);
        assertThat(testQuest.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuest.getReward()).isEqualTo(UPDATED_REWARD);
        assertThat(testQuest.getExpiresOn()).isEqualTo(UPDATED_EXPIRES_ON);
        assertThat(testQuest.getReviewStartDate()).isEqualTo(UPDATED_REVIEW_START_DATE);
        assertThat(testQuest.getMaxApplicants()).isEqualTo(UPDATED_MAX_APPLICANTS);
        assertThat(testQuest.getAssignee()).isEqualTo(UPDATED_ASSIGNEE);
        assertThat(testQuest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuest.getPrivateNotes()).isEqualTo(UPDATED_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingQuest() throws Exception {
        int databaseSizeBeforeUpdate = questRepository.findAll().size();
        quest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuest() throws Exception {
        int databaseSizeBeforeUpdate = questRepository.findAll().size();
        quest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuest() throws Exception {
        int databaseSizeBeforeUpdate = questRepository.findAll().size();
        quest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestWithPatch() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        int databaseSizeBeforeUpdate = questRepository.findAll().size();

        // Update the quest using partial update
        Quest partialUpdatedQuest = new Quest();
        partialUpdatedQuest.setId(quest.getId());

        partialUpdatedQuest
            .title(UPDATED_TITLE)
            .reward(UPDATED_REWARD)
            .expiresOn(UPDATED_EXPIRES_ON)
            .reviewStartDate(UPDATED_REVIEW_START_DATE)
            .maxApplicants(UPDATED_MAX_APPLICANTS)
            .status(UPDATED_STATUS);

        restQuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuest))
            )
            .andExpect(status().isOk());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
        Quest testQuest = questList.get(questList.size() - 1);
        assertThat(testQuest.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuest.getReward()).isEqualTo(UPDATED_REWARD);
        assertThat(testQuest.getExpiresOn()).isEqualTo(UPDATED_EXPIRES_ON);
        assertThat(testQuest.getReviewStartDate()).isEqualTo(UPDATED_REVIEW_START_DATE);
        assertThat(testQuest.getMaxApplicants()).isEqualTo(UPDATED_MAX_APPLICANTS);
        assertThat(testQuest.getAssignee()).isEqualTo(DEFAULT_ASSIGNEE);
        assertThat(testQuest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuest.getPrivateNotes()).isEqualTo(DEFAULT_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateQuestWithPatch() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        int databaseSizeBeforeUpdate = questRepository.findAll().size();

        // Update the quest using partial update
        Quest partialUpdatedQuest = new Quest();
        partialUpdatedQuest.setId(quest.getId());

        partialUpdatedQuest
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .reward(UPDATED_REWARD)
            .expiresOn(UPDATED_EXPIRES_ON)
            .reviewStartDate(UPDATED_REVIEW_START_DATE)
            .maxApplicants(UPDATED_MAX_APPLICANTS)
            .assignee(UPDATED_ASSIGNEE)
            .status(UPDATED_STATUS)
            .privateNotes(UPDATED_PRIVATE_NOTES);

        restQuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuest))
            )
            .andExpect(status().isOk());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
        Quest testQuest = questList.get(questList.size() - 1);
        assertThat(testQuest.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuest.getReward()).isEqualTo(UPDATED_REWARD);
        assertThat(testQuest.getExpiresOn()).isEqualTo(UPDATED_EXPIRES_ON);
        assertThat(testQuest.getReviewStartDate()).isEqualTo(UPDATED_REVIEW_START_DATE);
        assertThat(testQuest.getMaxApplicants()).isEqualTo(UPDATED_MAX_APPLICANTS);
        assertThat(testQuest.getAssignee()).isEqualTo(UPDATED_ASSIGNEE);
        assertThat(testQuest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuest.getPrivateNotes()).isEqualTo(UPDATED_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingQuest() throws Exception {
        int databaseSizeBeforeUpdate = questRepository.findAll().size();
        quest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuest() throws Exception {
        int databaseSizeBeforeUpdate = questRepository.findAll().size();
        quest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuest() throws Exception {
        int databaseSizeBeforeUpdate = questRepository.findAll().size();
        quest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuest() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        int databaseSizeBeforeDelete = questRepository.findAll().size();

        // Delete the quest
        restQuestMockMvc
            .perform(delete(ENTITY_API_URL_ID, quest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
