package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.LogoDTO;
import com.builderbackend.app.services.LogoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("logo")
public class LogoController {

    private final LogoService logoService;

    @Autowired
    public LogoController(LogoService logoService) {
        this.logoService = logoService;
    }

    @GetMapping("/get-logos")
    public ResponseEntity<List<LogoDTO>> getLogosForBusiness(@RequestParam("businessId") String businessId) {
        List<LogoDTO> logoDTOs;
        logoDTOs = logoService.getLogosForBusinessId(businessId);
        return ResponseEntity.ofNullable(logoDTOs);
    }



    @PostMapping("/upload")
    public ResponseEntity<LogoDTO> uploadLogo(@RequestPart("logoDTO") String logoDTOJson, 
                                              @RequestPart(name = "file", required = false) MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        LogoDTO logoDTO;
        try {
            logoDTO = objectMapper.readValue(logoDTOJson, LogoDTO.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build(); 
        }
        LogoDTO savedLogos = logoService.saveLogos(logoDTO, file);
        return ResponseEntity.ok(savedLogos);
    }


    @DeleteMapping("/delete/{logoId}")
    public ResponseEntity<Void> deleteLogo(@PathVariable String logoId) {
        try {
            logoService.deleteLogo(logoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
