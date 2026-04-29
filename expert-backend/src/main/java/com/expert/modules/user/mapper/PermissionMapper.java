package com.expert.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.modules.user.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<SysPermission> {

    @Select("SELECT p.* FROM sys_permission p " +
            "INNER JOIN sys_role_permission rp ON p.id = rp.permission_id " +
            "INNER JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<SysPermission> findPermissionsByUserId(Long userId);

    @Select("SELECT p.* FROM sys_permission p " +
            "INNER JOIN sys_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<SysPermission> findPermissionsByRoleId(Long roleId);

    @Select("SELECT * FROM sys_permission WHERE parent_id = #{parentId} ORDER BY sort_order")
    List<SysPermission> findByParentId(Long parentId);
}