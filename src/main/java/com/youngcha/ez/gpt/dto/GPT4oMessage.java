package com.youngcha.ez.gpt.dto;


import com.youngcha.ez.gpt.dto.content.Content;

import java.util.List;

public class GPT4oMessage {

    private String role;
    private List<Content> content;

    @Override
    public String toString() {
        return "GPT4oMessage{" +
                "role='" + role + '\'' +
                ", content=" + content +
                '}';
    }

    public GPT4oMessage() {}

    public GPT4oMessage(String role, List<Content> content) {
        super();
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }
}