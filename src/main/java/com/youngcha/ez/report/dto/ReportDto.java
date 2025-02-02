package com.youngcha.ez.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto{
    private long reportId;
    private String name;
    private String title;
    private String opinion;
    private String targetPrice;
    private List<ContextDto> contextList;

    public void updateContextList(List<ContextDto> list){
        this.contextList = list;
    }
}
