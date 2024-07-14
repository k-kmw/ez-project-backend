package com.youngcha.ez.report.dto;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "report")
public class Report {

    @Id @GeneratedValue
    private long reportId;

    @Column
    private String name; // 기업이름

    @Column
    private String opinion; // 투자 의견 매수/매도

    @Column
    private String targetPrice; // 목표 주가

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private List<Context> contextList;// 문단 리스트

    public Report(String name, String opinion, String targetPrice, List<Context> contextList) {
        this.name = name;
        this.opinion = opinion;
        this.targetPrice = targetPrice;
        this.contextList = contextList;
    }

    public long getReportId() {
        return reportId;
    }

    public String getName() {
        return name;
    }

    public String getOpinion() {
        return opinion;
    }

    public String getTargetPrice() {
        return targetPrice;
    }

    public List<Context> getContextList() {
        return contextList;
    }

    public void addContext(Context context){
        this.contextList.add(context);
    }
}

