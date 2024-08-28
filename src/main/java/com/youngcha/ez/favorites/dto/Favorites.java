package com.youngcha.ez.favorites.dto;

import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.report.dto.Report;
import com.youngcha.ez.report.dto.ReportConverter;
import com.youngcha.ez.report.dto.ReportDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Favorites {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoritesDto {
        private ReportDto report;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reportId")
    private Report report;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private Member member;
}
