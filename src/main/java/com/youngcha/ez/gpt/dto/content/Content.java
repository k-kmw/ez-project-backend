package com.youngcha.ez.gpt.dto.content;

public class Content {

    public Content(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Content{" +
                "type='" + type + '\'' +
                '}';
    }
}
