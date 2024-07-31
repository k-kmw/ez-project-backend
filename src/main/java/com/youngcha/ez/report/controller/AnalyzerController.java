package com.youngcha.ez.report.controller;

import com.youngcha.ez.report.service.AnalyzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyze")
@RequiredArgsConstructor
public class AnalyzerController {
    private final AnalyzerService analyzerService;

    @GetMapping()
    public ResponseEntity<?> analyze(){
        analyzerService.analyzeAllPdf();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
