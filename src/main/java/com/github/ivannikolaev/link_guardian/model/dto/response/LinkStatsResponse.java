package com.github.ivannikolaev.link_guardian.model.dto.response;

import java.time.LocalDateTime;

public record LinkStatsResponse(String shortCode, int currentClicks, int maxClicks, LocalDateTime expiresAt, LocalDateTime createdAt) {
}
