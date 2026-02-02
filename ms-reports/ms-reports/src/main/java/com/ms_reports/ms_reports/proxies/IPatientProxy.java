package com.ms_reports.ms_reports.proxies;

import com.ms_reports.ms_reports.entity.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="MS-PATIENT")
public interface IPatientProxy {

    @GetMapping("/patient/{id}")
    PatientDTO getPatientById(@PathVariable("id") Integer patientId);
}
