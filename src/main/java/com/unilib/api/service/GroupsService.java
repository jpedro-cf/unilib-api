package com.unilib.api.service;

import com.unilib.api.domain.category.Category;
import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.group.Group;
import com.unilib.api.domain.group.GroupRequestDTO;
import com.unilib.api.domain.user.User;
import com.unilib.api.repositories.GroupsRepository;
import com.unilib.api.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupsService {
    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private CompaniesService companiesService;

    @Autowired
    private UsersRepository usersRepository;

    public Group create(GroupRequestDTO data){
        if(!this.companiesService.userHasPermission(data.company_id(), "editor")){
            throw new IllegalArgumentException("You don't have permission to create groups");
        }

        List<User> users = this.usersRepository.findAllById(data.members());

        Company company = this.companiesService.getByID(data.company_id());

        Group group = new Group();

        group.setName(data.name());

        Set<User> members = new HashSet<>(users);
        group.setMembers(members);

        group.setCompany(company);
        group.setCreatedAt(new Date());

        this.groupsRepository.save(group);

        return group;

    }

    public List<Group> getAllByCompany(UUID companyId) {
        if(!this.companiesService.userHasPermission(companyId, "editor")){
            throw new IllegalArgumentException("You don't have permission to see groups");
        }

        return this.groupsRepository.findByCompanyId(companyId);
    }

    public Group getById(UUID id){
        Optional<Group> group = this.groupsRepository.findById(id);

        if(group.isEmpty()){
            throw new IllegalArgumentException("Group not found.");
        }

        if(!this.companiesService.userHasPermission(group.get().getCompany().getId(), "editor")){
            throw new IllegalArgumentException("You don't have permission to see this group");
        }

        return group.get();
    }

    public Void delete(UUID id){
        Optional<Group> group = this.groupsRepository.findById(id);

        if(group.isEmpty()){
            throw new IllegalArgumentException("Group not found.");
        }

        if(!this.companiesService.userHasPermission(group.get().getCompany().getId(), "admin")){
            throw new IllegalArgumentException("You don't have permission to see this group");
        }

        this.groupsRepository.delete(group.get());

        return null;
    }

    public Group addMembers(UUID id, List<UUID> members){
        Optional<Group> groupOpt = this.groupsRepository.findById(id);

        if(groupOpt.isEmpty()){
            throw new IllegalArgumentException("Group not found.");
        }

        Group group = groupOpt.get();

        if(!this.companiesService.userHasPermission(group.getCompany().getId(), "editor")){
            throw new IllegalArgumentException("You don't have permission to add members in this group.");
        }

        List<User> users = this.usersRepository.findAllById(members);

        if (users.isEmpty()) {
            throw new IllegalArgumentException("No users found with the provided IDs.");
        }

        Set<User> currentMembers = group.getMembers();
        currentMembers.addAll(users);
        group.setMembers(currentMembers);

        this.groupsRepository.save(group);

        return group;

    }

    public Group removeMembers(UUID id, List<UUID> members){
        Optional<Group> groupOpt = this.groupsRepository.findById(id);

        if(groupOpt.isEmpty()){
            throw new IllegalArgumentException("Group not found.");
        }

        Group group = groupOpt.get();

        if(!this.companiesService.userHasPermission(group.getCompany().getId(), "editor")){
            throw new IllegalArgumentException("You don't have permission to remove members in this group.");
        }

        List<User> users = this.usersRepository.findAllById(members);

        users.forEach(user -> group.getMembers().removeIf(existingMember -> existingMember.getId().equals(user.getId())));

        this.groupsRepository.save(group);

        return group;

    }
}
