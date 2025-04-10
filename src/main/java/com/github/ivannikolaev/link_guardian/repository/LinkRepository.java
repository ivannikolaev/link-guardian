package com.github.ivannikolaev.link_guardian.repository;

import com.github.ivannikolaev.link_guardian.model.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<ShortLink, Long> {
    Optional<ShortLink> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Modifying
    @Query("UPDATE ShortLink sl SET sl.active = false WHERE sl.expirationTime < CURRENT_TIMESTAMP AND sl.active = true")
    int deactivateExpiredLinks();

    @Modifying
    @Query("DELETE FROM ShortLink sl WHERE sl.active = false AND sl.updatedAt < :cutoffDate")
    int deleteInactiveLinksOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
}
