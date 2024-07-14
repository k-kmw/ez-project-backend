package com.youngcha.ez.report.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "context")
public class Context {

    @Id
    @GeneratedValue
    private Long contextId;

    @Column
    private String title;

    @Column(length = 25565)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reportId")
    private Report report;

    public Context(String title, String content, Report report) {
        this.title = title;
        this.content = content;
        this.report = report;
    }

    public Long getContextId() {
        return contextId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Report getReport() {
        return report;
    }
}


