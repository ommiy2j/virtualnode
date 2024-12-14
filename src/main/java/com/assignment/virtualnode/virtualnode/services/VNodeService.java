package com.assignment.virtualnode.virtualnode.services;

import com.assignment.virtualnode.virtualnode.dtos.CreateBulkNodeRequestBody;
import com.assignment.virtualnode.virtualnode.exceptions.ResourceAlreadyExistsException;
import com.assignment.virtualnode.virtualnode.models.VNode;
import com.assignment.virtualnode.virtualnode.repository.VNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VNodeService {

    private final VNodeRepository vNodeRepository;

    @Autowired
    public VNodeService(VNodeRepository vNodeRepository) {
        this.vNodeRepository = vNodeRepository;
    }

    public VNode createVNode(String name) throws ResourceAlreadyExistsException {
        Optional<VNode> node = vNodeRepository.findByNodeName(name);
        if (node.isPresent()) {
            throw new ResourceAlreadyExistsException("VNode with name " + name + " already exists");
        }
        VNode vNode = new VNode();
        vNode.setNodeName(name);
        return vNodeRepository.save(vNode);
    }

    public List<VNode> createBulkVNode(CreateBulkNodeRequestBody body) {
        List<String> nodeNames = body.getNodes();
        List<VNode> existingNodes = vNodeRepository.findByNodeNameIn(nodeNames);
        Set<String> existingNodeNames = existingNodes.stream()
                .map(VNode::getNodeName)
                .collect(Collectors.toSet());

        List<VNode> newNodes = nodeNames.stream()
                .filter(name -> !existingNodeNames.contains(name))
                .map(name -> {
                    VNode vNode = new VNode();
                    vNode.setNodeName(name);
                    return vNode;
                })
                .collect(Collectors.toList());


        if (!newNodes.isEmpty()) {
            vNodeRepository.saveAll(newNodes);
        }

        return newNodes;
    }


    public VNode getVNode(String name) {
        Optional<VNode> node = vNodeRepository.findByNodeName(name);
        if (node.isPresent()) {
            return node.get();
        }
        throw new IllegalArgumentException("VNode with name " + name + " does not exist");
    }

}
