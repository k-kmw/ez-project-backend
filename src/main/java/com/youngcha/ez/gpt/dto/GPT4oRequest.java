package com.youngcha.ez.gpt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.youngcha.ez.gpt.dto.content.Content;
import com.youngcha.ez.gpt.dto.content.ImageContent;
import com.youngcha.ez.gpt.dto.content.ImageUrl;
import com.youngcha.ez.gpt.dto.content.textContent;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GPT4oRequest {

    // gpt 모델을 지정한다.
    private String model;
    private List<GPT4oMessage> messages;
//    // 전송할 이미지들 ex pdf 캡처 이미지
//    private List<String> imageUrlList;
    // 파라미터
    private double temperature;

    // 입력 토큰과 출력 토큰 개수의 상한 (너무 짧거나 길지 않게 적절한 토큰 조정 필요)
    private int maxTokens;
    private double topP;
    private int frequencyPenalty;
    private int presencePenalty;



    public GPT4oRequest(String model, String prompt, List<String> imageUrlList, double temperature, int maxTokens, double topP, int frequencyPenalty, int presencePenalty) {

        this.model = model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP=topP;
        this.frequencyPenalty=frequencyPenalty;
        this.presencePenalty = presencePenalty;


        this.messages = new ArrayList<>();

        List<Content> contents  = new ArrayList<>();
        // 이미지를 넣는다.
        for(String url : imageUrlList){
            Content c = new ImageContent("image_url",new ImageUrl(url));
            contents.add(c);
        }
        // prompt를 넣는다.
        Content c = new textContent("text",prompt);
        contents.add(c);

        this.messages.add(new GPT4oMessage("user",contents));
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public List<GPT4oMessage> getMessages() {
        return messages;
    }
    public void setMessages(List<GPT4oMessage> messages) {
        this.messages = messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTopP() {
        return topP;
    }

    public void setTopP(double topP) {
        this.topP = topP;
    }

    public int getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(int frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public int getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(int presencePenalty) {
        this.presencePenalty = presencePenalty;
    }
}
