package com.youngcha.ez.gpt.content;

public class textContent extends Content{
    private String text;

    public textContent(String type, String text) {
        super(type);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
