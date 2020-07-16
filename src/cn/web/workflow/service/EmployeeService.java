package cn.web.workflow.service;

import java.util.List;

import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysRoleExample;
import cn.web.workflow.pojo.SysUserRole;

public interface EmployeeService {

	public Employee findUserByUsername(String username); // 帐号是唯一约束

	// 查找当前用户的上级id
	public Employee findEmployeeByManagerId(Long managerId);

	// 显示用户列表
	public List<EmployeeCustom> findUserAndRoleList();

	// 下一级别
	public List<Employee> findEmployeeByLevel(int level);

	// 修改权限
	public SysUserRole updateEmployeeRoles(String roleId, String userId);

	public List<SysRole> findAllRoleList(SysRoleExample sysRoleExample);

}
