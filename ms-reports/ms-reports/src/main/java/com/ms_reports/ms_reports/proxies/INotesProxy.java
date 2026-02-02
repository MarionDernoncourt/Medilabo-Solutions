package com.ms_reports.ms_reports.proxies;

import com.ms_reports.ms_reports.entity.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MS-NOTES")
public interface INotesProxy {

    @GetMapping("/notes/patient/{id}")
    List<NoteDTO> getNotesByPatientId(@PathVariable("id") Integer patientId);
}
