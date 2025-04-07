package com.github.ivannikolaev.link_guardian.model.dto.response;

import java.time.LocalDateTime;

public record LinkResponse(String shortCode, String originalUrl, LocalDateTime expiresAt, int maxClicks) {
}
