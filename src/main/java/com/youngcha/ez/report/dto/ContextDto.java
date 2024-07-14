package com.youngcha.ez.report.dto;

public class ContextDto {
    private Long contextId;
    private String title;
    private String content;

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContextDto(Long contextId, String title, String content) {
        this.contextId = contextId;
        this.title = title;
        this.content = content;
    }
}
