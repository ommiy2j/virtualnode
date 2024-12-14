package com.assignment.virtualnode.virtualnode.controller;

import com.assignment.virtualnode.virtualnode.dtos.CreateBulkNodeRequestBody;
import com.assignment.virtualnode.virtualnode.dtos.CreateGroupRequestBody;
import com.assignment.virtualnode.virtualnode.exceptions.ResourceAlreadyExistsException;
import com.assignment.virtualnode.virtualnode.models.VNode;
import com.assignment.virtualnode.virtualnode.models.VNodeGroup;
import com.assignment.virtualnode.virtualnode.services.VNodeGroupService;
import com.assignment.virtualnode.virtualnode.services.VNodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VirtualNodeControllerImpl {

    private final VNodeGroupService vNodeGroupService;
    private final VNodeService vNodeService;

    public VirtualNodeControllerImpl(VNodeGroupService vNodeGroupService, VNodeService vNodeService) {
        this.vNodeGroupService = vNodeGroupService;
        this.vNodeService = vNodeService;
    }

    @PostMapping("/group/create/")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequestBody createGroupRequestBody) {
        try {
            VNodeGroup vNodeGroup = vNodeGroupService.createVNodeGroup(createGroupRequestBody.getName(), createGroupRequestBody.getNodes());
            return new ResponseEntity<>(vNodeGroup, HttpStatus.CREATED);
        } catch (ResourceAlreadyExistsException alreadyExistsException) {
            return new ResponseEntity<>(alreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }


    @GetMapping("/group/groupName")
    public ResponseEntity<VNodeGroup> getVNodeGroup(@RequestParam String name) {
        return vNodeGroupService.getVNodeGroup(name).map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping("/node/create")
    public ResponseEntity<?> createNode(@RequestParam String name) {
        try {
            VNode vNode = vNodeService.createVNode(name);
            return new ResponseEntity<>(vNode, HttpStatus.CREATED);
        } catch (ResourceAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.CONFLICT);        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/node/create/all")
    public ResponseEntity<List<VNode>> createBulkNode(@RequestBody CreateBulkNodeRequestBody body) {
        try {
            List<VNode> vNode = vNodeService.createBulkVNode(body);
            return new ResponseEntity<>(vNode, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/node/get")
    public ResponseEntity<VNode> getVNode(@RequestParam String name) {
        return new ResponseEntity<>(vNodeService.getVNode(name), HttpStatus.OK);
    }


    @GetMapping("/group/nodename")
    public ResponseEntity<VNodeGroup> findGroupByNodeName(@RequestParam String nodeName) {
        VNodeGroup vNodeGroupByNode = vNodeGroupService.getVNodeGroupByNode(nodeName);
        if (vNodeGroupByNode != null) {
            return new ResponseEntity<>(vNodeGroupByNode, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
