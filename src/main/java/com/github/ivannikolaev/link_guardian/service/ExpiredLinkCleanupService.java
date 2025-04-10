package com.github.ivannikolaev.link_guardian.service;

import com.github.ivannikolaev.link_guardian.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpiredLinkCleanupService {
    private final LinkRepository linkRepository;

    @Value("${app.cleanup.retain-inactive-days:30}")
    private int retainInactiveDays;

    @Scheduled(cron = "${app.cleanup.cron: 0 0 3 * * ?}")
    @Transactional
    public void cleanupExpiredLinks() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retainInactiveDays);

        int deactivatedCount = linkRepository.deactivateExpiredLinks();
        int deletedCount = linkRepository.deleteInactiveLinksOlderThan(cutoffDate);

        log.info("Expired links cleanup: deactivated {} links, deleted {} inactive links", deactivatedCount, deletedCount);
    }

}
