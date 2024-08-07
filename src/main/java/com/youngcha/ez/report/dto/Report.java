package com.youngcha.ez.report.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "report")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Report {

    @Id @GeneratedValue
    private Long reportId;

    @Column
    private String name; // 기업이름

    @Column
    private String title; // 보고서 제목

    @Column
    private String opinion; // 투자 의견 매수/매도

    @Column
    private String targetPrice; // 목표 주가

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Context> contextList;// 문단 리스트

    public void addContext(Context context){
        this.contextList.add(context);
    }
}

