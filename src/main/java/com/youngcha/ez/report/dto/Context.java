package com.youngcha.ez.report.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "context")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Context {

    @Id
    @GeneratedValue
    private Long contextId;

    @Column
    private String title;

    @Column(length = 25565)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId")
    private Report report;


}


