package com.youngcha.ez.report.controller;

import com.youngcha.ez.report.dto.*;
import com.youngcha.ez.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping()
    public ResponseEntity<List<ReportDto>> findReportUsingPaging(@PageableDefault(size = 8) Pageable pageable, @RequestParam(value = "searchKeyword", required = false)String searchWord){
        if(searchWord == null || searchWord.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(reportService.findAll(pageable));
        }
        else{
            return ResponseEntity.status(HttpStatus.OK) .body(reportService.findByName(pageable, searchWord));
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDto> findReportDetail(@PathVariable(name = "reportId")Long reportId){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.findReportDtoById(reportId));
    }

}
