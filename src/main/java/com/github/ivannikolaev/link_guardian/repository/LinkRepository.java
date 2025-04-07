package com.github.ivannikolaev.link_guardian.repository;

import com.github.ivannikolaev.link_guardian.model.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<ShortLink, Long> {
    Optional<ShortLink> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Transactional
    @Modifying
    @Query("UPDATE ShortLink sl SET sl.active = false WHERE sl.expirationTime < CURRENT_TIMESTAMP AND sl.active = true")
    void deactivateExpiredLinks();
}
