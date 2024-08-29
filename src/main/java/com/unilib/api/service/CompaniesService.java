package com.unilib.api.service;

import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.company.CompanyRequestDTO;
import com.unilib.api.domain.permission.Permission;
import com.unilib.api.domain.permission.Permissions;
import com.unilib.api.domain.user.User;
import com.unilib.api.repositories.CompaniesRepository;
import com.unilib.api.repositories.PermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CompaniesService {
    @Autowired
    private AuthService authService;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    public Company create(CompanyRequestDTO request){
        Optional<User> user = this.authService.getCurrentUser();

        if(user.isEmpty()){
            throw new IllegalArgumentException("Current user not found");
        }

        Company company = new Company();

        company.setName(request.name());
        company.setDescription(request.description());
        // Criar serviço para handle de arquivos futuramente, tbm tem nos books e users
        company.setImage("https://image.png");
        company.setCreatedAt(new Date());

        this.companiesRepository.save(company);

        Permission permission = new Permission();
        Permissions adminPermissions = new Permissions();

        adminPermissions.setAdmin(true);

        permission.setCompany(company);
        permission.setUser(user.get());
        permission.setPermissions(adminPermissions);

        this.permissionsRepository.save(permission);

        return company;
    }
}
