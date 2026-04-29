package com.expert.modules.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class PermissionTreeNode {
    private Long id;
    private String permCode;
    private String permName;
    private Integer permType;
    private Long parentId;
    private String path;
    private String icon;
    private Integer sort;
    private List<PermissionTreeNode> children;
}