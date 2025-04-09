package com.github.ivannikolaev.link_guardian.exception;

public class LinkExpiredException extends RuntimeException {
    public LinkExpiredException(String message) {
        super(message);
    }
}
