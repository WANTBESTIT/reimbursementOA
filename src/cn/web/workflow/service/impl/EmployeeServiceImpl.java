package cn.web.workflow.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.web.workflow.mapper.EmployeeMapper;
import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.mapper.SysPermissionMapper;
import cn.web.workflow.mapper.SysRoleMapper;
import cn.web.workflow.mapper.SysRolePermissionMapper;
import cn.web.workflow.mapper.SysUserRoleMapper;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.EmployeeExample;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysRoleExample;
import cn.web.workflow.pojo.SysRolePermission;
import cn.web.workflow.pojo.SysRolePermissionExample;
import cn.web.workflow.pojo.SysUserRole;
import cn.web.workflow.pojo.SysUserRoleExample;
import cn.web.workflow.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysPermissionCustomMapper sysPermissionCustomMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SysRolePermissionMapper rolePermissionMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	@Override
	public Employee findUserByUsername(String username) {

		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();

		criteria.andNameEqualTo(username);
		List<Employee> list = employeeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Employee findEmployeeByManagerId(Long managerId) {
		return employeeMapper.selectByPrimaryKey(managerId);
	}

	@Override
	public List<EmployeeCustom> findUserAndRoleList() {
		return sysPermissionCustomMapper.findUserAndRoleList();
	}

	// 根据员工级别查找员工信息
	@Override
	public List<Employee> findEmployeeByLevel(int level) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andRoleEqualTo(level);
		List<Employee> list = employeeMapper.selectByExample(example);
		
		return list;
	}

	// 修改权限
	@Override
	public void updateEmployeeRoles(String roleId, String userId) {
		SysUserRoleExample example = new SysUserRoleExample();
		SysUserRoleExample.Criteria criteria = example.createCriteria();
		criteria.andSysRoleIdEqualTo(userId);
		System.out.println("userId:=======" + userId);
		SysUserRole sysUserRole = sysUserRoleMapper.selectByExample(example).get(0);
		// System.out.println(sysUserRole.getSysUserId());
		sysUserRole.setSysRoleId(roleId);
		sysUserRoleMapper.updateByPrimaryKey(sysUserRole);

	}

	@Override
	public void addRoleAndPermissions(SysRole role, int[] permissionIds) {
		// 添加角色
		sysRoleMapper.insert(role);
		// 添加角色和权限关系表
		for (int i = 0; i < permissionIds.length; i++) {
			SysRolePermission rolePermission = new SysRolePermission();
			// 16进制随机码
			String uuid = UUID.randomUUID().toString();
			rolePermission.setId(uuid);
			rolePermission.setSysRoleId(role.getId());
			rolePermission.setSysPermissionId(permissionIds[i] + "");
			rolePermissionMapper.insert(rolePermission);
		}
	}

	// 新建权限
	@Override
	public void addPermission(SysPermission sysPermission) {
		sysPermissionMapper.insert(sysPermission);
	}

	// 删除角色
	@Override
	public void deleteRoleById(String roleId) {
		SysRoleExample example = new SysRoleExample();
		SysRoleExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(roleId);
		SysRole sysRole = sysRoleMapper.selectByExample(example).get(0);
		sysRole.setAvailable("0");
		sysRoleMapper.updateByPrimaryKey(sysRole);
	}

	// 修改角色
	// 修改的是角色与权限的中间表数据
	@Override
	public void updateRoleAndPermissions(String rid, int[] permissionIds) {
		System.out.println(rid+"=======44544444444444444");
		//先删除角色权限关系表中角色的权限关系
		SysRolePermissionExample example = new SysRolePermissionExample();
		SysRolePermissionExample.Criteria criteria = example.createCriteria();
		criteria.andSysRoleIdEqualTo(rid);
		rolePermissionMapper.deleteByExample(example);
		//重新创建角色权限关系
		for (Integer pid : permissionIds) {
			System.out.println("pid======="+pid);
			SysRolePermission rolePermission = new SysRolePermission();
			String uuid = UUID.randomUUID().toString();
			rolePermission.setId(uuid);
			rolePermission.setSysRoleId(rid);
			rolePermission.setSysPermissionId(pid.toString());
			rolePermissionMapper.insert(rolePermission);
		}
	}
}
