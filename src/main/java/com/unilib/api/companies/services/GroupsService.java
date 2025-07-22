package com.unilib.api.companies.services;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.companies.Group;
import com.unilib.api.companies.dto.GroupRequestDTO;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.dto.GroupMembersValidation;
import com.unilib.api.companies.validators.dto.GroupValidation;
import com.unilib.api.companies.validators.groups.AddGroupMembersValidator;
import com.unilib.api.companies.validators.groups.CreateGroupValidator;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import com.unilib.api.companies.repositories.GroupsRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final ValidatorsFactory validatorsFactory;

    public GroupsService(GroupsRepository groupsRepository,
                         ValidatorsFactory validatorsFactory){
        this.groupsRepository = groupsRepository;
        this.validatorsFactory = validatorsFactory;
    }

    public Group create(GroupRequestDTO request, User user){
        CreateGroupValidator validator = validatorsFactory
                .getValidator(CreateGroupValidator.class);

        Company company = validator
                .validate(new GroupValidation(request.companyId(), user.getId()));

        Group group = Group.builder()
                .title(request.title())
                .company(company)
                .build();

        return groupsRepository.save(group);

    }

    public List<Group> getAllByCompany(UUID companyId, User user) {
        CompanyMemberExist validator = validatorsFactory.getValidator(CompanyMemberExist.class);
        validator.validate(new CompanyMemberValidation(companyId, user.getId()));

        return this.groupsRepository.findByCompanyId(companyId);
    }

    public Group getById(UUID groupId, User user){
        CompanyMemberExist validator = validatorsFactory.getValidator(CompanyMemberExist.class);
        validator.validate(new CompanyMemberValidation(groupId, user.getId()));

        return groupsRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found."));
    }

    public void delete(UUID groupId, User user){
        CompanyMemberExist validator = validatorsFactory.getValidator(CompanyMemberExist.class);
        CompanyMember member = validator
                .validate(new CompanyMemberValidation(groupId, user.getId()));

        if(member.getRole().getLevel() < CompanyRole.ADMIN.getLevel()){
            throw new ForbiddenException("You're not allowed to delete this group.");
        }

        this.groupsRepository.deleteById(groupId);
    }

    public Group addMembers(UUID groupId, List<UUID> members, User user){
        Group group = this.groupsRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found."));

        AddGroupMembersValidator validator = validatorsFactory
                .getValidator(AddGroupMembersValidator.class);

        List<User> newMembers = validator.validate(
                new GroupMembersValidation(groupId, user.getId(), members));

        group.getMembers().addAll(newMembers);

        return this.groupsRepository.save(group);

    }
}
