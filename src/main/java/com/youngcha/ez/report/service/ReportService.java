package com.youngcha.ez.report.service;

import com.youngcha.ez.report.dto.*;
import com.youngcha.ez.report.repository.ContextRepository;
import com.youngcha.ez.report.repository.ReportRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ContextRepository contextRepository;

    public ReportService(ReportRepository reportRepository, ContextRepository contextRepository) {
        this.reportRepository = reportRepository;
        this.contextRepository = contextRepository;
    }

    public ReportDto findReportById(Long id) {
        Optional<Report> findReport = reportRepository.findById(id);
        System.out.println("findReport = " + findReport.isPresent());

        if(findReport.isPresent()){
            Report report = findReport.get();
            ReportDto reportDto = ReportConverter.reportToReportDto(report);
            List<ContextDto> contextDtoList = ReportConverter.contextListToContextDtoList(report.getContextList());
            reportDto.updateContextList(contextDtoList);
            return reportDto;
        }
        else{
            return new ReportDto();
        }
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public List<ReportDto> findReportBySearchWord(Pageable pageable, String name){
        // 검색어 없을 때 null 처리
        if(name == null){
            name = "";
        }
        return ReportConverter.reportListToReportDtoList(reportRepository.findAll(pageable).toList());
    }

    public void save(Report report){
        reportRepository.save(report);
        for(Context context : report.getContextList()){
            contextRepository.save(context);
        }
    }


}
