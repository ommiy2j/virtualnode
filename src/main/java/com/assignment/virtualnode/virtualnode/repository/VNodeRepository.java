package com.assignment.virtualnode.virtualnode.repository;

import com.assignment.virtualnode.virtualnode.models.VNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VNodeRepository extends JpaRepository<VNode, Long> {
    Optional<VNode> findByNodeName(String name);
    List<VNode> findByNodeNameIn(List<String> names);
}
