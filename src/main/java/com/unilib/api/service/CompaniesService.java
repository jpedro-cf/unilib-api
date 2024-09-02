package com.unilib.api.service;

import com.unilib.api.domain.book.Book;
import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.company.CompanyRequestDTO;
import com.unilib.api.domain.permission.Permission;
import com.unilib.api.domain.permission.Permissions;
import com.unilib.api.domain.user.User;
import com.unilib.api.domain.user.UserRole;
import com.unilib.api.repositories.BooksRepository;
import com.unilib.api.repositories.CompaniesRepository;
import com.unilib.api.repositories.PermissionsRepository;
import com.unilib.api.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompaniesService {
    @Autowired
    private AuthService authService;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BooksRepository booksRepository;

    public Company create(CompanyRequestDTO request){
        Optional<User> user = this.authService.getCurrentUser();

        if(user.isEmpty()){
            throw new UsernameNotFoundException("Current user not found");
        }

        Company company = new Company();

        company.setName(request.name());
        company.setDescription(request.description());

        // Criar serviço para handle de arquivos futuramente, pois tbm teremos upload de arquivo nos books e users
        company.setImage("https://image.png");
        company.setCreatedAt(new Date());

        this.companiesRepository.save(company);

        Permission permission = new Permission();
        Permissions adminPermissions = new Permissions();

        adminPermissions.setAdmin(true);
        adminPermissions.setManager(true);
        adminPermissions.setEditor(true);

        permission.setCompany(company);
        permission.setUser(user.get());
        permission.setPermissions(adminPermissions);

        user.get().setRoles(Set.of(UserRole.ADMIN.name()));

        this.permissionsRepository.save(permission);

        this.usersRepository.save(user.get());

        return company;
    }

    public Company getByID(UUID id){
        return this.companiesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }

    public void delete(UUID id){
        if(!this.userHasPermission(id, "admin")){
            throw new IllegalArgumentException("You don't have permission to delete this company.");
        }

        Optional<Company> company = this.companiesRepository.findById(id);

        if(company.isEmpty()){
            throw new IllegalArgumentException("Company not found.");
        }

        List<Book> books = this.booksRepository.findAllByCompany(company.get());
        List<Permission> permissions = this.permissionsRepository.findByCompanyId(company.get().getId());

        this.permissionsRepository.deleteAll(permissions);
        this.booksRepository.deleteAll(books);
        this.companiesRepository.delete(company.get());

    }

    public Boolean userHasPermission(UUID company_id, String role) {
        Optional<User> user = this.authService.getCurrentUser();

        if(user.isEmpty()){
            return false;
        }

        Optional<Permission> permission = this.permissionsRepository.findByCompanyIdAndUserId(company_id, user.get().getId());

        if(permission.isEmpty()){
            return false;
        }

        Permission perm = permission.get();
        Permissions permissions = perm.getPermissions();

        switch (role.toLowerCase()) {
            case "admin":
                return Boolean.TRUE.equals(permissions.getAdmin());
            case "manager":
                return Boolean.TRUE.equals(permissions.getManager());
            case "editor":
                return Boolean.TRUE.equals(permissions.getEditor());
            default:
                throw new IllegalArgumentException("Invalid permission type: " + permission);
        }

    }
}
