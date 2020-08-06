package cn.web.workflow.mapper;

import java.util.List;

import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
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

	// 根据职位查找名称和权限
	SysRole findRoleAndPermissionLIstByUserId(String name);
	
	// 得到角色权限的父菜单
	List<TreeMenu> getAllMenuAndPermission();
	
	// 得到角色权限的子菜单
	List<TreeMenu> getSubMenuAndPermissions();
	
	// 查询出所有角色的名称
	List<SysRole> findAllRoleName();
	
	// 根据Id查询角色的权限
	List<SysPermission> findPermissionsByRoleId(String roleId);
}