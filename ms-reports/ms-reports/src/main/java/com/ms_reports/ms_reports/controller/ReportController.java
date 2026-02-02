package com.ms_reports.ms_reports.controller;

import com.ms_reports.ms_reports.entity.RiskLevel;
import com.ms_reports.ms_reports.service.IReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/patient/{id}")
    public String getReportByPatientId(@PathVariable Integer id) {
        RiskLevel risk = reportService.calculateRisk(id);
        return risk.getLabel();
    }}
