package com.github.ivannikolaev.link_guardian.repository;

import com.github.ivannikolaev.link_guardian.model.entity.ShortLink;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    @Test
    void shouldSaveAndRetrieveLink() {
        //Given
        ShortLink link = ShortLink.builder()
                .originalUrl("https://www.google.com")
                .shortCode("google")
                .expirationTime(LocalDateTime.now().plusHours(1))
                .maxClicks(10)
                .build();

        //When
        linkRepository.save(link);
        Optional<ShortLink> foundLink = linkRepository.findByShortCode("google");

        //Then
        assertThat(foundLink)
                .isPresent()
                .hasValueSatisfying(l -> {
                    assertThat(l.getOriginalUrl()).isEqualTo("https://www.google.com");
                    assertThat(l.getMaxClicks()).isEqualTo(10);
                });
    }

    @Test
    @Sql(scripts = "/test-data.sql")
    void shouldFindExistingLinksFromScript() {
        //When
        Optional<ShortLink> foundLink = linkRepository.findByShortCode("google");

        //Then
        assertThat(foundLink).hasValueSatisfying(l -> {
            assertThat(l.getOriginalUrl()).isEqualTo("https://www.google.com");
            assertThat(l.isActive()).isTrue();
        });
    }

    @Test
    void shouldReturnEmptyForNonExistentCode() {
        //When
        Optional<ShortLink> foundLink = linkRepository.findByShortCode("non-existent");

        //Then
        assertThat(foundLink).isEmpty();
    }
}