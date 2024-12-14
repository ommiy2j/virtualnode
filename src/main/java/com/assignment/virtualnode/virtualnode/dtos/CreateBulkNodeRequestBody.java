package com.assignment.virtualnode.virtualnode.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateBulkNodeRequestBody {
    List<String> nodes;
}
