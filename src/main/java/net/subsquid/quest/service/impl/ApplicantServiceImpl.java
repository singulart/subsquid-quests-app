package net.subsquid.quest.service.impl;

import java.util.Optional;
import net.subsquid.quest.domain.Applicant;
import net.subsquid.quest.repository.ApplicantRepository;
import net.subsquid.quest.service.ApplicantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Applicant}.
 */
@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {

    private final Logger log = LoggerFactory.getLogger(ApplicantServiceImpl.class);

    private final ApplicantRepository applicantRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Override
    public Applicant save(Applicant applicant) {
        log.debug("Request to save Applicant : {}", applicant);
        return applicantRepository.save(applicant);
    }

    @Override
    public Optional<Applicant> partialUpdate(Applicant applicant) {
        log.debug("Request to partially update Applicant : {}", applicant);

        return applicantRepository
            .findById(applicant.getId())
            .map(existingApplicant -> {
                if (applicant.getDiscordHandle() != null) {
                    existingApplicant.setDiscordHandle(applicant.getDiscordHandle());
                }

                return existingApplicant;
            })
            .map(applicantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Applicant> findAll(Pageable pageable) {
        log.debug("Request to get all Applicants");
        return applicantRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Applicant> findOne(Long id) {
        log.debug("Request to get Applicant : {}", id);
        return applicantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Applicant : {}", id);
        applicantRepository.deleteById(id);
    }
}
