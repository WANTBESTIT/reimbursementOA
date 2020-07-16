package cn.web.workflow.mapper;

import java.util.List;

import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysUserRole;
import cn.web.workflow.pojo.TreeMenu;

public interface SysPermissionCustomMapper {

	// 查询一级导航
	List<TreeMenu> findMenuList();

	// 查询二级导航
	List<SysPermission> getSubMenu();

	// 根据用户id查询菜单
	public List<SysPermission> findMenuListByUserId(String userid) throws Exception;

	// 根据用户id查询权限url
	public List<SysPermission> findPermissionListByUserId(String userid) throws Exception;

	// 用户列表
	List<EmployeeCustom> findUserAndRoleList();

	SysRole findRoleAndPermissionLIstByUserId(String name);

	// 修改角色权限
	SysUserRole updateEmployeeRoles(String roleId, String userId);
}