package cn.web.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.web.workflow.mapper.EmployeeMapper;
import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.mapper.SysRoleMapper;
import cn.web.workflow.mapper.SysUserRoleMapper;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.EmployeeExample;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysRoleExample;
import cn.web.workflow.pojo.SysUserRole;
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
	@Override
	public Employee findUserByUsername(String username) {
		
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		
		criteria.andNameEqualTo(username);
		List<Employee> list = employeeMapper.selectByExample(example);
		if(list != null && list.size()>0){
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

	// 查找下一个级别
	@Override
	public List<Employee> findEmployeeByLevel(int level) {
		return null;
	}

	// 修改权限
	@Override
	public SysUserRole updateEmployeeRoles(String roleId, String userId) {
		return sysPermissionCustomMapper.updateEmployeeRoles(roleId, userId);
	}

	// 【下拉表】显示权限
	@Override
	public List<SysRole> findAllRoleList(SysRoleExample sysRoleExample) {
		return sysRoleMapper.selectByExample(sysRoleExample);
	}

}
