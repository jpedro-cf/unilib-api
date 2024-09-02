package com.unilib.api.controller;

import com.unilib.api.domain.group.Group;
import com.unilib.api.domain.group.GroupRequestDTO;
import com.unilib.api.service.GroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
