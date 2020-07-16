package cn.web.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.web.workflow.mapper.EmployeeMapper;
import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeExample;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;
import cn.web.workflow.service.SysService;

@Service
public class SysServiceImpl implements SysService {

	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SysPermissionCustomMapper sysMapper;

	@Override
	public Employee findSysUserByUserCode(String name) {
		EmployeeExample employeeExample = new EmployeeExample();
		EmployeeExample.Criteria criteria = employeeExample.createCriteria();
		criteria.andNameEqualTo(name);

		List<Employee> list = employeeMapper.selectByExample(employeeExample);

		if (list != null && list.size() == 1) {
			return list.get(0);
		}

		return null;
	}

	@Override
	public List<TreeMenu> loadMenuTree() {
		List<TreeMenu> menus = sysMapper.findMenuList();
		return menus;
	}

	// 根据用户id查询权限范围的菜单
	@Override
	public List<SysPermission> findMenuListByUserId(String userid) throws Exception {
		return sysMapper.findMenuListByUserId(userid);
	}

	// 根据用户id查询权限范围的url
	@Override
	public List<SysPermission> findPermissionListByUserId(String userid) throws Exception {
		// TODO Auto-generated method stub
		return sysMapper.findPermissionListByUserId(userid);
	}

}
