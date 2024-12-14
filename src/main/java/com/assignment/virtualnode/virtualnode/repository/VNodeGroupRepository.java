package com.assignment.virtualnode.virtualnode.repository;

import com.assignment.virtualnode.virtualnode.models.VNodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VNodeGroupRepository extends JpaRepository<VNodeGroup, Long> {
    Optional<VNodeGroup> findByGroupName(String name);

    @Query("SELECT cg FROM VNodeGroup cg JOIN cg.nodeGroupMappings gn WHERE gn.vNode.nodeName = :nodeName")
    Optional<VNodeGroup> findVNodeGroupByNodeName(String nodeName);
}
