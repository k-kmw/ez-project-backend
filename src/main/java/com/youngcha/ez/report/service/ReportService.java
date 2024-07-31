package com.youngcha.ez.report.service;

import com.youngcha.ez.report.dto.*;
import com.youngcha.ez.report.repository.ContextRepository;
import com.youngcha.ez.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportDto findReportById(Long id) {
        Optional<Report> findReport = reportRepository.findById(id);

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

    public List<ReportDto> findAll(Pageable pageable) {
        List<Report> reports = reportRepository.findAll(pageable).toList();
        return ReportConverter.reportListToReportDtoList(reports);
    }

    public List<ReportDto> findByName(Pageable pageable, String name){
        return ReportConverter.reportListToReportDtoList(reportRepository.findByName(pageable,name));
    }

    public ReportDto save(Report report){
        Report save = reportRepository.save(report);
        return ReportConverter.reportToReportDto(save);
    }


}
