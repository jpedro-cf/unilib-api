package com.unilib.api.controller;

import com.unilib.api.domain.group.Group;
import com.unilib.api.domain.group.GroupRequestDTO;
import com.unilib.api.service.GroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("groups")
public class GroupsController {
    @Autowired
    private GroupsService groupsService;

    @PostMapping
    public ResponseEntity<Group> create(@RequestBody GroupRequestDTO data){
        Group group = this.groupsService.create(data);

        return ResponseEntity.ok(group);
    }

    @GetMapping("/company/{company_id}")
    public ResponseEntity<List<Group>> getAll(@PathVariable UUID company_id){
        List<Group> groups = this.groupsService.getAllByCompany(company_id);

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getById(@PathVariable UUID id){
        Group group = this.groupsService.getById(id);

        return ResponseEntity.ok(group);
    }
}
