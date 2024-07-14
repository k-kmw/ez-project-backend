package com.youngcha.ez.report.controller;

import com.youngcha.ez.report.dto.Context;
import com.youngcha.ez.report.dto.ContextDto;
import com.youngcha.ez.report.dto.Report;
import com.youngcha.ez.report.dto.ReportDto;
import com.youngcha.ez.report.service.Analyzer;
import com.youngcha.ez.report.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final Analyzer analyzer;

    public ReportController(ReportService reportService, Analyzer analyzer) {
        this.reportService = reportService;
        this.analyzer = analyzer;
    }

    @GetMapping("/")
    public List<Report> findAllReport(){
        return reportService.findAll();
    }

    @GetMapping("/{id}")
    public Report findReportById(@PathVariable(name = "id") Long id){
        return reportService.findReportById(id);
    }

    @GetMapping("/analyze")
    public ReportDto analyzeReport(){
        List<Report> reports = analyzer.analyzeAllPdf();
        Report report = reports.get(0);

        List<ContextDto> contextDtoList = new ArrayList<ContextDto>();
        for (Context context : report.getContextList()){
            contextDtoList.add(new ContextDto(context.getContextId(),context.getTitle(),context.getContent()));
        }

        ReportDto reportDto = new ReportDto(report.getReportId(),report.getName(),report.getOpinion(),report.getTargetPrice(),contextDtoList);
        return reportDto;
    }

    @GetMapping("/temp")
    public void temp(){
        analyzer.temp();
    }

    @GetMapping("/image")
    public void image(){
        analyzer.imageTran();
    }

}
