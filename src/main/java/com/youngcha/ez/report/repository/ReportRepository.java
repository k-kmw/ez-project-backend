package com.youngcha.ez.report.repository;

import com.youngcha.ez.report.dto.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {

    List<Report> findByNameContaining(Pageable pageable,String name);

}
