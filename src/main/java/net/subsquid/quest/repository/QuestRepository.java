package net.subsquid.quest.repository;

import net.subsquid.quest.domain.Quest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Quest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestRepository extends JpaRepository<Quest, Long>, JpaSpecificationExecutor<Quest> {}
