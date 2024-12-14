package com.assignment.virtualnode.virtualnode.services;

import com.assignment.virtualnode.virtualnode.exceptions.ResourceAlreadyExistsException;
import com.assignment.virtualnode.virtualnode.models.VNode;
import com.assignment.virtualnode.virtualnode.models.VNodeGroup;
import com.assignment.virtualnode.virtualnode.models.VNodeGroupMapping;
import com.assignment.virtualnode.virtualnode.repository.VNodeGroupRepository;
import com.assignment.virtualnode.virtualnode.repository.VNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VNodeGroupService {

    private final VNodeGroupRepository vNodeGroupRepository;
    private final VNodeRepository vNodeRepository;

    @Autowired
    public VNodeGroupService(VNodeGroupRepository vNodeGroupRepository, VNodeRepository vNodeRepository) {
        this.vNodeGroupRepository = vNodeGroupRepository;
        this.vNodeRepository = vNodeRepository;
    }

    public Optional<VNodeGroup> getVNodeGroup(String name) {
        return vNodeGroupRepository.findByGroupName(name);
    }

    public Optional<VNode> getVNode(String name) {
        return vNodeRepository.findByNodeName(name);
    }

    public VNodeGroup createVNodeGroup(String name, List<String> nodes) throws ResourceAlreadyExistsException {
        Optional<VNodeGroup> vNodeGroup = vNodeGroupRepository.findByGroupName(name);
        if (vNodeGroup.isPresent()) {
            throw new ResourceAlreadyExistsException("Group Already Exists ");
        }

        List<VNode> vNodes = new ArrayList<>();
        for (String node : nodes) {
            VNode vNode = vNodeRepository.findByNodeName(node)
                    .orElseThrow(() -> new RuntimeException("Node Not Found, First Create Node"));

            Optional<VNodeGroup> vNodeGroup1 = vNodeGroupRepository.findVNodeGroupByNodeName(node);
            if (vNodeGroup1.isPresent()) {
                throw new ResourceAlreadyExistsException("Node Already exist in a Group ");
            }
            vNodes.add(vNode);
        }

        VNodeGroup newVNodeGroup = new VNodeGroup();
        newVNodeGroup.setGroupName(name);
        List<VNodeGroupMapping> vNodeGroupMappings = new ArrayList<>();
        vNodes.forEach(node ->{
            VNodeGroupMapping groupMapping = new VNodeGroupMapping();
            groupMapping.setVNodeGroup(newVNodeGroup);
            groupMapping.setVNode(node);
            vNodeGroupMappings.add(groupMapping);
        });
        newVNodeGroup.setNodeGroupMappings(vNodeGroupMappings);
        vNodeGroupRepository.save(newVNodeGroup);
        return newVNodeGroup;
    }


    public VNodeGroup getVNodeGroupByNode(String name) {
        return vNodeGroupRepository.findVNodeGroupByNodeName(name).isPresent() ? vNodeGroupRepository.findVNodeGroupByNodeName(name).get() : null;
    }
}
