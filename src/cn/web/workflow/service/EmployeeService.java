package cn.web.workflow.service;

import java.util.List;

import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;

public interface EmployeeService {

	public Employee findUserByUsername(String username); // 帐号是唯一约束

	// 查找当前用户的上级id
	public Employee findEmployeeByManagerId(Long managerId);

	// 显示用户列表
	public List<EmployeeCustom> findUserAndRoleList();

	// 下一级别
	public List<Employee> findEmployeeByLevel(int level);

	// 修改权限
	public void updateEmployeeRoles(String roleId, String userId);

	// 添加角色和分配权限
	void addRoleAndPermissions(SysRole role, int[] permissionIds);

	// 新建权限
	public void addPermission(SysPermission sysPermission);

	// 【角色列表】 修改角色
	public void updateRoleAndPermissions(String rid,int[] permissionIds);
	
	// 【角色列表】删除角色
	public void deleteRoleById(String roleId);

}
