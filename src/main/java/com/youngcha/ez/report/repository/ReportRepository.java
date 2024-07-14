package com.youngcha.ez.report.repository;

import com.youngcha.ez.report.dto.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
