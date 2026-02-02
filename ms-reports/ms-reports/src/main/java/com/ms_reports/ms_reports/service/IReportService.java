package com.ms_reports.ms_reports.service;

import com.ms_reports.ms_reports.entity.RiskLevel;
import org.springframework.stereotype.Service;

@Service
public interface IReportService {

   RiskLevel calculateRisk(Integer patientId);
}
