package com.unilib.api.companies.http;

import com.unilib.api.companies.Group;
import com.unilib.api.companies.dto.GroupRequestDTO;
import com.unilib.api.companies.services.GroupsService;
import com.unilib.api.config.security.TokenAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("groups")
public class GroupsController {
    private final GroupsService groupsService;

    public GroupsController(GroupsService groupsService){
        this.groupsService = groupsService;
    }

    @PostMapping
    public ResponseEntity<Group> create(@RequestBody GroupRequestDTO data,
                                        TokenAuthentication authentication){
        Group group = this.groupsService.create(data, authentication.getUser());

        return ResponseEntity.ok(group);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<List<Group>> getAll(@PathVariable("id") UUID companyId,
                                              TokenAuthentication authentication){
        List<Group> groups = this.groupsService
                .getAllByCompany(companyId, authentication.getUser());

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getById(@PathVariable UUID id,
                                         TokenAuthentication authentication){
        Group group = this.groupsService.getById(id, authentication.getUser());

        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, TokenAuthentication authentication){
        this.groupsService.delete(id, authentication.getUser());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Group> addMember(@PathVariable("id") UUID groupId,
                                           @RequestBody List<UUID> members,
                                           TokenAuthentication authentication){
        Group group = this.groupsService
                .addMembers(groupId, members, authentication.getUser());

        return ResponseEntity.ok(group);
    }
}
