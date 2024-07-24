package com.youngcha.ez.report.service;

import com.youngcha.ez.report.dto.Context;
import com.youngcha.ez.report.dto.Report;
import com.youngcha.ez.report.dto.ReportConverter;
import com.youngcha.ez.report.dto.ReportDto;
import com.youngcha.ez.report.repository.ContextRepository;
import com.youngcha.ez.report.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Report findReportById(Long id) {
        Optional<Report> report = reportRepository.findById(id);
        return report.get();
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
