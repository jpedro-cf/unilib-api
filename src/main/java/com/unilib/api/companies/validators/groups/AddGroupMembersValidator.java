package com.unilib.api.companies.validators.groups;

import com.unilib.api.companies.Group;
import com.unilib.api.companies.repositories.GroupsRepository;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.dto.GroupMembersValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import com.unilib.api.users.validators.UserExist;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddGroupMembersValidator implements Validator<GroupMembersValidation, List<User>> {
    private final GroupsRepository groupsRepository;
    private final CompanyMemberExist memberExist;
    private final UserExist userExist;

    public AddGroupMembersValidator(GroupsRepository groupsRepository,
                                    ValidatorsFactory factory){
        this.groupsRepository = groupsRepository;
        this.memberExist = factory.getValidator(CompanyMemberExist.class);
        this.userExist = factory.getValidator(UserExist.class);
    }

    @Override
    public List<User> validate(GroupMembersValidation request) {
        Group group = groupsRepository.findById(request.groupId())
                .orElseThrow(() -> new NotFoundException("Group not found."));

        memberExist.validate(new CompanyMemberValidation(
                group.getCompany().getId(), request.userId()));

        List<User> users = request.memberIds()
                .stream()
                .map(userExist::validate)
                .toList();

        return users;
    }
}
