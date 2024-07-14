package com.youngcha.ez.report.dto;

import java.util.List;

public class ReportDto{
    private long reportId;
    private String name;
    private String opinion;
    private String targetPrice;
    private List<ContextDto> contextList;

    public ReportDto(long reportId, String name, String opinion, String targetPrice, List<ContextDto> contextList) {
        this.reportId = reportId;
        this.name = name;
        this.opinion = opinion;
        this.targetPrice = targetPrice;
        this.contextList = contextList;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(String targetPrice) {
        this.targetPrice = targetPrice;
    }

    public List<ContextDto> getContextList() {
        return contextList;
    }

    public void setContextList(List<ContextDto> contextList) {
        this.contextList = contextList;
    }


}
