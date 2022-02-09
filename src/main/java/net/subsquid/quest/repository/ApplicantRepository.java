package net.subsquid.quest.repository;

import java.util.List;
import java.util.Optional;
import net.subsquid.quest.domain.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Applicant entity.
 */
@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    @Query(
        value = "select distinct applicant from Applicant applicant left join fetch applicant.quests",
        countQuery = "select count(distinct applicant) from Applicant applicant"
    )
    Page<Applicant> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct applicant from Applicant applicant left join fetch applicant.quests")
    List<Applicant> findAllWithEagerRelationships();

    @Query("select applicant from Applicant applicant left join fetch applicant.quests where applicant.id =:id")
    Optional<Applicant> findOneWithEagerRelationships(@Param("id") Long id);
}
