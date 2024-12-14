package com.assignment.virtualnode.virtualnode.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class VNodeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable=false)
    private String groupName;


    @OneToMany(mappedBy = "vNodeGroup", cascade = CascadeType.ALL)
    private List<VNodeGroupMapping> nodeGroupMappings = new ArrayList<>();

}
