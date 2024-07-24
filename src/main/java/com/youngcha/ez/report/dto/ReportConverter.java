package com.youngcha.ez.report.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportConverter {

    public static ReportDto reportToReportDto(Report report){

        return ReportDto.builder()
                .reportId(report.getReportId())
                .name(report.getName())
                .opinion(report.getOpinion())
                .targetPrice(report.getTargetPrice())
                .contextList(new ArrayList<ContextDto>())
                .build();
    }

    public static List<ReportDto> reportListToReportDtoList(List<Report> list){
        List<ReportDto> result = new ArrayList<>();
        for (Report report : list) {
            result.add(reportToReportDto(report));
        }
        return result;
    }

    public static ContextDto contextToContextDto(Context context){
        return ContextDto.builder()
                .contextId(context.getContextId())
                .title(context.getTitle())
                .content(context.getContent())
                .build();
    }

    public static List<ContextDto> contextListToContextDtoList(List<Context> list){
        List<ContextDto> result = new ArrayList<>();
        for(Context context : list){
            result.add(contextToContextDto(context));
        }
        return result;
    }

}
