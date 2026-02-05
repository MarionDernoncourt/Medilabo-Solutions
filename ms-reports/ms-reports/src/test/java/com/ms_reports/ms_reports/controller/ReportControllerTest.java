package com.ms_reports.ms_reports.controller;

import com.ms_reports.ms_reports.entity.NoteDTO;
import com.ms_reports.ms_reports.entity.RiskLevel;
import com.ms_reports.ms_reports.exceptions.NoteNotFoundException;
import com.ms_reports.ms_reports.exceptions.NoteServiceException;
import com.ms_reports.ms_reports.exceptions.PatientNotFoundException;
import com.ms_reports.ms_reports.exceptions.PatientServiceException;
import com.ms_reports.ms_reports.service.IReportService;
import com.ms_reports.ms_reports.service.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private ReportController reportController;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private IReportService reportService;



    @Test
    public void getReportByPatientIdTest_WhenReturn_NONE() throws Exception {
                when(reportService.calculateRisk(any(Integer.class))).thenReturn(RiskLevel.NONE);

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("None"));
            }

    @Test
    public void getReportByPatientIdTest_WhenReturn_NOTAPPLICABLE() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenReturn(RiskLevel.NOT_APPLICABLE);

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Not defined"));
    }
    @Test
    public void getReportByPatientIdTest_WhenReturn_BORDERLINE() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenReturn(RiskLevel.BORDERLINE);

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Borderline"));
    }
    @Test
    public void getReportByPatientIdTest_WhenReturn_IN_DANGER() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenReturn(RiskLevel.IN_DANGER);

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("In Danger"));
    }
    @Test
    public void getReportByPatientIdTest_WhenReturn_EARLY_ONSET() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenReturn(RiskLevel.EARLY_ONSET);

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Early onset"));
    }

    @Test
    public void getReportByPatientIdTest_WhenReturn_PatientNotFound() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenThrow(new PatientNotFoundException("Aucun patient trouvé"));

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getReportByPatientIdTest_WhenReturn_PatientServiceException() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenThrow(new PatientServiceException("Erreur lors de la communication avec le service patient"));

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void getReportByPatientIdTest_WhenReturn_NoteNotFound() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenThrow(new NoteNotFoundException("Aucune note trouvée"));

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getReportByPatientIdTest_WhenReturn_NoteServiceException() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenThrow(new NoteServiceException("Erreur lors de la communication avec le service notes"));

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isServiceUnavailable());
    }
    @Test
    public void  getReportByPatientIdTest_WhenReturn_InternalError() throws Exception {
        when(reportService.calculateRisk(any(Integer.class))).thenThrow(new RuntimeException());

        mockMvc.perform(get("/report/patient/1"))
                .andExpect(status().isInternalServerError());
    }


}
