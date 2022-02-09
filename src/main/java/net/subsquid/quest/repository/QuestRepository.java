package net.subsquid.quest.repository;

import java.util.List;
import java.util.Optional;
import net.subsquid.quest.domain.Quest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Quest entity.
 */
@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
    @Query(
        value = "select distinct quest from Quest quest left join fetch quest.applicants",
        countQuery = "select count(distinct quest) from Quest quest"
    )
    Page<Quest> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct quest from Quest quest left join fetch quest.applicants")
    List<Quest> findAllWithEagerRelationships();

    @Query("select quest from Quest quest left join fetch quest.applicants where quest.id =:id")
    Optional<Quest> findOneWithEagerRelationships(@Param("id") Long id);
}
