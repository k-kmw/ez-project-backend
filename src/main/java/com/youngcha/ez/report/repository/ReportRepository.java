package com.youngcha.ez.report.repository;

import com.youngcha.ez.report.dto.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query("SELECT r FROM Report r WHERE r.name LIKE %:name%")
    List<Report> findByName(Pageable pageable, @Param("name") String name);
}
