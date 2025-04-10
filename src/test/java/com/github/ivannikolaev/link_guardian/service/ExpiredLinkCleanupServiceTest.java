package com.github.ivannikolaev.link_guardian.service;

import com.github.ivannikolaev.link_guardian.model.entity.ShortLink;
import com.github.ivannikolaev.link_guardian.repository.LinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ExpiredLinkCleanupService.class)
class ExpiredLinkCleanupServiceTest {
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ExpiredLinkCleanupService expiredLinkCleanupService;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldDeactivateExpiredLinks() {
        //given
        ShortLink activeExpired = ShortLink.builder()
                .originalUrl("https://expired.com")
                .shortCode("expired")
                .expirationTime(LocalDateTime.now().minusDays(1))
                .active(true)
                .build();

        linkRepository.save(activeExpired);

        //when
        expiredLinkCleanupService.cleanupExpiredLinks();

        //then
        ShortLink updatedExpired = linkRepository.findByShortCode("expired").orElseThrow();
        assertThat(updatedExpired.isActive()).isFalse();

    }

}