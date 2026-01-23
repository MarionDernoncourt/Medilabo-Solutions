package com.medilabo.ms_notes.proxies;

import com.medilabo.ms_notes.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name= "MS-PATIENT")
public interface IPatientProxy {

    @GetMapping("/patient/{id}")
    PatientDTO getPatientById(@PathVariable("id") Integer id);

}
