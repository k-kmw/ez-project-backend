package com.youngcha.ez.report.service;

import com.youngcha.ez.gpt.service.GptService;
import com.youngcha.ez.report.dto.Context;
import com.youngcha.ez.report.dto.Report;
import com.youngcha.ez.util.FileUtil;
import com.youngcha.ez.util.ImageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyzerService {

    private final GptService gptService;
    private final ImageConverter imageConverter;
    private final ReportService reportService;
    private final FileUtil fileUtil;


    public Report analyzePdf(String pdfName){
        // 1. pdf -> 이미지 변환
        imageConverter.convertJpg(pdfName);

        // 2-1. 목표 주가 및 의견 추출
        String targetPrice = gptService.getTargetPrice();
        targetPrice = targetPrice.replace("[","");
        targetPrice = targetPrice.replace("]","");


        String opinion;

        int idx = targetPrice.indexOf(",");
        if(idx == -1){
            opinion = "없음";
            targetPrice = "없음";
        }
        else{
            opinion = targetPrice.substring(0,idx);
            targetPrice = targetPrice.substring(idx+1,targetPrice.length());
        }

        // 보고서 제목 추출
        String reportTitle = gptService.getTitle();
        reportTitle = reportTitle.replace("[","");
        reportTitle = reportTitle.replace("]","");
        //파싱

        // 3. 기업 이름 추출
        String name = gptService.getName();
        name = name.replace("[","");
        name = name.replace("]","");

        // 4. 콘텐츠 요약 parse 필요
        String summary = gptService.getResponseByGPT4();
        String[] st = summary.split("\n");

        // 콘텐츠 parse 필요
        List<Context> contexts = new ArrayList<>();

        Report report = Report.builder()
                .name(name)
                .title(reportTitle)
                .opinion(opinion)
                .targetPrice(targetPrice)
                .contextList(contexts)
                .build();


        for(String string : st){
            if(string == null) continue;
            string = string.trim();
            if(string.length() == 0)continue;

            string = string.replace("[", "");
            string = string.replace("]", "");

            int i = string.indexOf(",");
            if(i == -1){
                log.debug("can't find {}",string);
                continue;
            }
            String title = string.substring(0,i);
            String content = string.substring(i+1,string.length());

            Context context = Context.builder()
                                    .title(title)
                                    .content(content)
                                    .report(report)
                                    .build();
            report.addContext(context);
        }

        return report;
    }

    public List<Report> analyzeAllPdf(){

        List<Report> reports = new ArrayList<>();
        List<String> allPdfNames = fileUtil.getAllPdfNames();
        for (String pdfName : allPdfNames){
            if(pdfName.contains("DS_Store"))continue;
            pdfName = pdfName.split("\\.")[0];
            Report report = analyzePdf(pdfName);
            reports.add(report);
            reportService.save(report);
        }

        return reports;
    }
}