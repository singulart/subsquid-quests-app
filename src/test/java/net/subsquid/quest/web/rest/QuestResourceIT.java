package net.subsquid.quest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import net.subsquid.quest.IntegrationTest;
import net.subsquid.quest.domain.Quest;
import net.subsquid.quest.domain.enumeration.QuestStatus;
import net.subsquid.quest.repository.QuestRepository;
import net.subsquid.quest.service.criteria.QuestCriteria;
import net.subsquid.quest.service.dto.QuestDTO;
import net.subsquid.quest.service.mapper.QuestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestResource} REST controller.
 */
@IntegrationTest
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
    private static final LocalDate SMALLER_EXPIRES_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_REVIEW_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REVIEW_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REVIEW_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_MAX_APPLICANTS = 0;
    private static final Integer UPDATED_MAX_APPLICANTS = 1;
    private static final Integer SMALLER_MAX_APPLICANTS = 0 - 1;

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

    @Autowired
    private QuestMapper questMapper;

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
        QuestDTO questDTO = questMapper.toDto(quest);
        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
        QuestDTO questDTO = questMapper.toDto(quest);

        int databaseSizeBeforeCreate = questRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
        QuestDTO questDTO = questMapper.toDto(quest);

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
        QuestDTO questDTO = questMapper.toDto(quest);

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
        QuestDTO questDTO = questMapper.toDto(quest);

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
        QuestDTO questDTO = questMapper.toDto(quest);

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
        QuestDTO questDTO = questMapper.toDto(quest);

        restQuestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
    void getQuestsByIdFiltering() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        Long id = quest.getId();

        defaultQuestShouldBeFound("id.equals=" + id);
        defaultQuestShouldNotBeFound("id.notEquals=" + id);

        defaultQuestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title equals to DEFAULT_TITLE
        defaultQuestShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the questList where title equals to UPDATED_TITLE
        defaultQuestShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title not equals to DEFAULT_TITLE
        defaultQuestShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the questList where title not equals to UPDATED_TITLE
        defaultQuestShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultQuestShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the questList where title equals to UPDATED_TITLE
        defaultQuestShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title is not null
        defaultQuestShouldBeFound("title.specified=true");

        // Get all the questList where title is null
        defaultQuestShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByTitleContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title contains DEFAULT_TITLE
        defaultQuestShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the questList where title contains UPDATED_TITLE
        defaultQuestShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title does not contain DEFAULT_TITLE
        defaultQuestShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the questList where title does not contain UPDATED_TITLE
        defaultQuestShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where description equals to DEFAULT_DESCRIPTION
        defaultQuestShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the questList where description equals to UPDATED_DESCRIPTION
        defaultQuestShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where description not equals to DEFAULT_DESCRIPTION
        defaultQuestShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the questList where description not equals to UPDATED_DESCRIPTION
        defaultQuestShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultQuestShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the questList where description equals to UPDATED_DESCRIPTION
        defaultQuestShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where description is not null
        defaultQuestShouldBeFound("description.specified=true");

        // Get all the questList where description is null
        defaultQuestShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where description contains DEFAULT_DESCRIPTION
        defaultQuestShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the questList where description contains UPDATED_DESCRIPTION
        defaultQuestShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where description does not contain DEFAULT_DESCRIPTION
        defaultQuestShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the questList where description does not contain UPDATED_DESCRIPTION
        defaultQuestShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestsByRewardIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reward equals to DEFAULT_REWARD
        defaultQuestShouldBeFound("reward.equals=" + DEFAULT_REWARD);

        // Get all the questList where reward equals to UPDATED_REWARD
        defaultQuestShouldNotBeFound("reward.equals=" + UPDATED_REWARD);
    }

    @Test
    @Transactional
    void getAllQuestsByRewardIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reward not equals to DEFAULT_REWARD
        defaultQuestShouldNotBeFound("reward.notEquals=" + DEFAULT_REWARD);

        // Get all the questList where reward not equals to UPDATED_REWARD
        defaultQuestShouldBeFound("reward.notEquals=" + UPDATED_REWARD);
    }

    @Test
    @Transactional
    void getAllQuestsByRewardIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reward in DEFAULT_REWARD or UPDATED_REWARD
        defaultQuestShouldBeFound("reward.in=" + DEFAULT_REWARD + "," + UPDATED_REWARD);

        // Get all the questList where reward equals to UPDATED_REWARD
        defaultQuestShouldNotBeFound("reward.in=" + UPDATED_REWARD);
    }

    @Test
    @Transactional
    void getAllQuestsByRewardIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reward is not null
        defaultQuestShouldBeFound("reward.specified=true");

        // Get all the questList where reward is null
        defaultQuestShouldNotBeFound("reward.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByRewardContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reward contains DEFAULT_REWARD
        defaultQuestShouldBeFound("reward.contains=" + DEFAULT_REWARD);

        // Get all the questList where reward contains UPDATED_REWARD
        defaultQuestShouldNotBeFound("reward.contains=" + UPDATED_REWARD);
    }

    @Test
    @Transactional
    void getAllQuestsByRewardNotContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reward does not contain DEFAULT_REWARD
        defaultQuestShouldNotBeFound("reward.doesNotContain=" + DEFAULT_REWARD);

        // Get all the questList where reward does not contain UPDATED_REWARD
        defaultQuestShouldBeFound("reward.doesNotContain=" + UPDATED_REWARD);
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn equals to DEFAULT_EXPIRES_ON
        defaultQuestShouldBeFound("expiresOn.equals=" + DEFAULT_EXPIRES_ON);

        // Get all the questList where expiresOn equals to UPDATED_EXPIRES_ON
        defaultQuestShouldNotBeFound("expiresOn.equals=" + UPDATED_EXPIRES_ON);
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn not equals to DEFAULT_EXPIRES_ON
        defaultQuestShouldNotBeFound("expiresOn.notEquals=" + DEFAULT_EXPIRES_ON);

        // Get all the questList where expiresOn not equals to UPDATED_EXPIRES_ON
        defaultQuestShouldBeFound("expiresOn.notEquals=" + UPDATED_EXPIRES_ON);
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn in DEFAULT_EXPIRES_ON or UPDATED_EXPIRES_ON
        defaultQuestShouldBeFound("expiresOn.in=" + DEFAULT_EXPIRES_ON + "," + UPDATED_EXPIRES_ON);

        // Get all the questList where expiresOn equals to UPDATED_EXPIRES_ON
        defaultQuestShouldNotBeFound("expiresOn.in=" + UPDATED_EXPIRES_ON);
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn is not null
        defaultQuestShouldBeFound("expiresOn.specified=true");

        // Get all the questList where expiresOn is null
        defaultQuestShouldNotBeFound("expiresOn.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn is greater than or equal to DEFAULT_EXPIRES_ON
        defaultQuestShouldBeFound("expiresOn.greaterThanOrEqual=" + DEFAULT_EXPIRES_ON);

        // Get all the questList where expiresOn is greater than or equal to UPDATED_EXPIRES_ON
        defaultQuestShouldNotBeFound("expiresOn.greaterThanOrEqual=" + UPDATED_EXPIRES_ON);
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn is less than or equal to DEFAULT_EXPIRES_ON
        defaultQuestShouldBeFound("expiresOn.lessThanOrEqual=" + DEFAULT_EXPIRES_ON);

        // Get all the questList where expiresOn is less than or equal to SMALLER_EXPIRES_ON
        defaultQuestShouldNotBeFound("expiresOn.lessThanOrEqual=" + SMALLER_EXPIRES_ON);
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsLessThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn is less than DEFAULT_EXPIRES_ON
        defaultQuestShouldNotBeFound("expiresOn.lessThan=" + DEFAULT_EXPIRES_ON);

        // Get all the questList where expiresOn is less than UPDATED_EXPIRES_ON
        defaultQuestShouldBeFound("expiresOn.lessThan=" + UPDATED_EXPIRES_ON);
    }

    @Test
    @Transactional
    void getAllQuestsByExpiresOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where expiresOn is greater than DEFAULT_EXPIRES_ON
        defaultQuestShouldNotBeFound("expiresOn.greaterThan=" + DEFAULT_EXPIRES_ON);

        // Get all the questList where expiresOn is greater than SMALLER_EXPIRES_ON
        defaultQuestShouldBeFound("expiresOn.greaterThan=" + SMALLER_EXPIRES_ON);
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate equals to DEFAULT_REVIEW_START_DATE
        defaultQuestShouldBeFound("reviewStartDate.equals=" + DEFAULT_REVIEW_START_DATE);

        // Get all the questList where reviewStartDate equals to UPDATED_REVIEW_START_DATE
        defaultQuestShouldNotBeFound("reviewStartDate.equals=" + UPDATED_REVIEW_START_DATE);
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate not equals to DEFAULT_REVIEW_START_DATE
        defaultQuestShouldNotBeFound("reviewStartDate.notEquals=" + DEFAULT_REVIEW_START_DATE);

        // Get all the questList where reviewStartDate not equals to UPDATED_REVIEW_START_DATE
        defaultQuestShouldBeFound("reviewStartDate.notEquals=" + UPDATED_REVIEW_START_DATE);
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate in DEFAULT_REVIEW_START_DATE or UPDATED_REVIEW_START_DATE
        defaultQuestShouldBeFound("reviewStartDate.in=" + DEFAULT_REVIEW_START_DATE + "," + UPDATED_REVIEW_START_DATE);

        // Get all the questList where reviewStartDate equals to UPDATED_REVIEW_START_DATE
        defaultQuestShouldNotBeFound("reviewStartDate.in=" + UPDATED_REVIEW_START_DATE);
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate is not null
        defaultQuestShouldBeFound("reviewStartDate.specified=true");

        // Get all the questList where reviewStartDate is null
        defaultQuestShouldNotBeFound("reviewStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate is greater than or equal to DEFAULT_REVIEW_START_DATE
        defaultQuestShouldBeFound("reviewStartDate.greaterThanOrEqual=" + DEFAULT_REVIEW_START_DATE);

        // Get all the questList where reviewStartDate is greater than or equal to UPDATED_REVIEW_START_DATE
        defaultQuestShouldNotBeFound("reviewStartDate.greaterThanOrEqual=" + UPDATED_REVIEW_START_DATE);
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate is less than or equal to DEFAULT_REVIEW_START_DATE
        defaultQuestShouldBeFound("reviewStartDate.lessThanOrEqual=" + DEFAULT_REVIEW_START_DATE);

        // Get all the questList where reviewStartDate is less than or equal to SMALLER_REVIEW_START_DATE
        defaultQuestShouldNotBeFound("reviewStartDate.lessThanOrEqual=" + SMALLER_REVIEW_START_DATE);
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate is less than DEFAULT_REVIEW_START_DATE
        defaultQuestShouldNotBeFound("reviewStartDate.lessThan=" + DEFAULT_REVIEW_START_DATE);

        // Get all the questList where reviewStartDate is less than UPDATED_REVIEW_START_DATE
        defaultQuestShouldBeFound("reviewStartDate.lessThan=" + UPDATED_REVIEW_START_DATE);
    }

    @Test
    @Transactional
    void getAllQuestsByReviewStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where reviewStartDate is greater than DEFAULT_REVIEW_START_DATE
        defaultQuestShouldNotBeFound("reviewStartDate.greaterThan=" + DEFAULT_REVIEW_START_DATE);

        // Get all the questList where reviewStartDate is greater than SMALLER_REVIEW_START_DATE
        defaultQuestShouldBeFound("reviewStartDate.greaterThan=" + SMALLER_REVIEW_START_DATE);
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants equals to DEFAULT_MAX_APPLICANTS
        defaultQuestShouldBeFound("maxApplicants.equals=" + DEFAULT_MAX_APPLICANTS);

        // Get all the questList where maxApplicants equals to UPDATED_MAX_APPLICANTS
        defaultQuestShouldNotBeFound("maxApplicants.equals=" + UPDATED_MAX_APPLICANTS);
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants not equals to DEFAULT_MAX_APPLICANTS
        defaultQuestShouldNotBeFound("maxApplicants.notEquals=" + DEFAULT_MAX_APPLICANTS);

        // Get all the questList where maxApplicants not equals to UPDATED_MAX_APPLICANTS
        defaultQuestShouldBeFound("maxApplicants.notEquals=" + UPDATED_MAX_APPLICANTS);
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants in DEFAULT_MAX_APPLICANTS or UPDATED_MAX_APPLICANTS
        defaultQuestShouldBeFound("maxApplicants.in=" + DEFAULT_MAX_APPLICANTS + "," + UPDATED_MAX_APPLICANTS);

        // Get all the questList where maxApplicants equals to UPDATED_MAX_APPLICANTS
        defaultQuestShouldNotBeFound("maxApplicants.in=" + UPDATED_MAX_APPLICANTS);
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants is not null
        defaultQuestShouldBeFound("maxApplicants.specified=true");

        // Get all the questList where maxApplicants is null
        defaultQuestShouldNotBeFound("maxApplicants.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants is greater than or equal to DEFAULT_MAX_APPLICANTS
        defaultQuestShouldBeFound("maxApplicants.greaterThanOrEqual=" + DEFAULT_MAX_APPLICANTS);

        // Get all the questList where maxApplicants is greater than or equal to UPDATED_MAX_APPLICANTS
        defaultQuestShouldNotBeFound("maxApplicants.greaterThanOrEqual=" + UPDATED_MAX_APPLICANTS);
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants is less than or equal to DEFAULT_MAX_APPLICANTS
        defaultQuestShouldBeFound("maxApplicants.lessThanOrEqual=" + DEFAULT_MAX_APPLICANTS);

        // Get all the questList where maxApplicants is less than or equal to SMALLER_MAX_APPLICANTS
        defaultQuestShouldNotBeFound("maxApplicants.lessThanOrEqual=" + SMALLER_MAX_APPLICANTS);
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsLessThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants is less than DEFAULT_MAX_APPLICANTS
        defaultQuestShouldNotBeFound("maxApplicants.lessThan=" + DEFAULT_MAX_APPLICANTS);

        // Get all the questList where maxApplicants is less than UPDATED_MAX_APPLICANTS
        defaultQuestShouldBeFound("maxApplicants.lessThan=" + UPDATED_MAX_APPLICANTS);
    }

    @Test
    @Transactional
    void getAllQuestsByMaxApplicantsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where maxApplicants is greater than DEFAULT_MAX_APPLICANTS
        defaultQuestShouldNotBeFound("maxApplicants.greaterThan=" + DEFAULT_MAX_APPLICANTS);

        // Get all the questList where maxApplicants is greater than SMALLER_MAX_APPLICANTS
        defaultQuestShouldBeFound("maxApplicants.greaterThan=" + SMALLER_MAX_APPLICANTS);
    }

    @Test
    @Transactional
    void getAllQuestsByAssigneeIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where assignee equals to DEFAULT_ASSIGNEE
        defaultQuestShouldBeFound("assignee.equals=" + DEFAULT_ASSIGNEE);

        // Get all the questList where assignee equals to UPDATED_ASSIGNEE
        defaultQuestShouldNotBeFound("assignee.equals=" + UPDATED_ASSIGNEE);
    }

    @Test
    @Transactional
    void getAllQuestsByAssigneeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where assignee not equals to DEFAULT_ASSIGNEE
        defaultQuestShouldNotBeFound("assignee.notEquals=" + DEFAULT_ASSIGNEE);

        // Get all the questList where assignee not equals to UPDATED_ASSIGNEE
        defaultQuestShouldBeFound("assignee.notEquals=" + UPDATED_ASSIGNEE);
    }

    @Test
    @Transactional
    void getAllQuestsByAssigneeIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where assignee in DEFAULT_ASSIGNEE or UPDATED_ASSIGNEE
        defaultQuestShouldBeFound("assignee.in=" + DEFAULT_ASSIGNEE + "," + UPDATED_ASSIGNEE);

        // Get all the questList where assignee equals to UPDATED_ASSIGNEE
        defaultQuestShouldNotBeFound("assignee.in=" + UPDATED_ASSIGNEE);
    }

    @Test
    @Transactional
    void getAllQuestsByAssigneeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where assignee is not null
        defaultQuestShouldBeFound("assignee.specified=true");

        // Get all the questList where assignee is null
        defaultQuestShouldNotBeFound("assignee.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByAssigneeContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where assignee contains DEFAULT_ASSIGNEE
        defaultQuestShouldBeFound("assignee.contains=" + DEFAULT_ASSIGNEE);

        // Get all the questList where assignee contains UPDATED_ASSIGNEE
        defaultQuestShouldNotBeFound("assignee.contains=" + UPDATED_ASSIGNEE);
    }

    @Test
    @Transactional
    void getAllQuestsByAssigneeNotContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where assignee does not contain DEFAULT_ASSIGNEE
        defaultQuestShouldNotBeFound("assignee.doesNotContain=" + DEFAULT_ASSIGNEE);

        // Get all the questList where assignee does not contain UPDATED_ASSIGNEE
        defaultQuestShouldBeFound("assignee.doesNotContain=" + UPDATED_ASSIGNEE);
    }

    @Test
    @Transactional
    void getAllQuestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where status equals to DEFAULT_STATUS
        defaultQuestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the questList where status equals to UPDATED_STATUS
        defaultQuestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where status not equals to DEFAULT_STATUS
        defaultQuestShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the questList where status not equals to UPDATED_STATUS
        defaultQuestShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultQuestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the questList where status equals to UPDATED_STATUS
        defaultQuestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where status is not null
        defaultQuestShouldBeFound("status.specified=true");

        // Get all the questList where status is null
        defaultQuestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByPrivateNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where privateNotes equals to DEFAULT_PRIVATE_NOTES
        defaultQuestShouldBeFound("privateNotes.equals=" + DEFAULT_PRIVATE_NOTES);

        // Get all the questList where privateNotes equals to UPDATED_PRIVATE_NOTES
        defaultQuestShouldNotBeFound("privateNotes.equals=" + UPDATED_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestsByPrivateNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where privateNotes not equals to DEFAULT_PRIVATE_NOTES
        defaultQuestShouldNotBeFound("privateNotes.notEquals=" + DEFAULT_PRIVATE_NOTES);

        // Get all the questList where privateNotes not equals to UPDATED_PRIVATE_NOTES
        defaultQuestShouldBeFound("privateNotes.notEquals=" + UPDATED_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestsByPrivateNotesIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where privateNotes in DEFAULT_PRIVATE_NOTES or UPDATED_PRIVATE_NOTES
        defaultQuestShouldBeFound("privateNotes.in=" + DEFAULT_PRIVATE_NOTES + "," + UPDATED_PRIVATE_NOTES);

        // Get all the questList where privateNotes equals to UPDATED_PRIVATE_NOTES
        defaultQuestShouldNotBeFound("privateNotes.in=" + UPDATED_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestsByPrivateNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where privateNotes is not null
        defaultQuestShouldBeFound("privateNotes.specified=true");

        // Get all the questList where privateNotes is null
        defaultQuestShouldNotBeFound("privateNotes.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestsByPrivateNotesContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where privateNotes contains DEFAULT_PRIVATE_NOTES
        defaultQuestShouldBeFound("privateNotes.contains=" + DEFAULT_PRIVATE_NOTES);

        // Get all the questList where privateNotes contains UPDATED_PRIVATE_NOTES
        defaultQuestShouldNotBeFound("privateNotes.contains=" + UPDATED_PRIVATE_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestsByPrivateNotesNotContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where privateNotes does not contain DEFAULT_PRIVATE_NOTES
        defaultQuestShouldNotBeFound("privateNotes.doesNotContain=" + DEFAULT_PRIVATE_NOTES);

        // Get all the questList where privateNotes does not contain UPDATED_PRIVATE_NOTES
        defaultQuestShouldBeFound("privateNotes.doesNotContain=" + UPDATED_PRIVATE_NOTES);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestShouldBeFound(String filter) throws Exception {
        restQuestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restQuestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestShouldNotBeFound(String filter) throws Exception {
        restQuestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        QuestDTO questDTO = questMapper.toDto(updatedQuest);

        restQuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questDTO))
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

        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questDTO))
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

        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questDTO))
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

        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questDTO)))
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

        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questDTO))
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

        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questDTO))
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

        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questDTO)))
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
