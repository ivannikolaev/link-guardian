package com.github.ivannikolaev.link_guardian.model.dto.request;

public record CreateLinkRequest(String originalUrl, int expirationHours, int maxClicks) {
}
