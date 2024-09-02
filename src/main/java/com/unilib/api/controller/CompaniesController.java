package com.unilib.api.controller;

import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.company.CompanyRequestDTO;
import com.unilib.api.service.CompaniesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
@RequestMapping("companies")
public class CompaniesController {
    @Autowired
    private CompaniesService companiesService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Company> create(@RequestParam("name") String name,
                                          @RequestParam("description") String description,
                                          @RequestParam("image") MultipartFile image){

        CompanyRequestDTO request = new CompanyRequestDTO(name, description, image);
        Company company = this.companiesService.create(request);

        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.companiesService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
