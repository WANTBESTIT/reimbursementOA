package cn.web.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.web.workflow.exception.CustomException;
import cn.web.workflow.mapper.EmployeeMapper;
import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.mapper.SysRoleMapper;
import cn.web.workflow.mapper.SysUserRoleMapper;
import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeExample;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysUserRole;
import cn.web.workflow.pojo.SysUserRoleExample;
import cn.web.workflow.pojo.TreeMenu;
import cn.web.workflow.service.SysService;
import cn.web.workflow.utils.MD5;

@Service
public class SysServiceImpl implements SysService {

	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SysPermissionCustomMapper sysMapper;
	@Autowired
	private SysRoleMapper roleMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	@Override
	public ActiveUser authenticat(String userCode, String password) throws Exception {
		/**
		 * 认证过程： 根据用户身份（账号）查询数据库，如果查询不到用户不存在 对输入的密码 和数据库密码 进行比对，如果一致，认证通过
		 */
		// 根据用户账号查询数据库
		Employee sysUser = this.findSysUserByUserCode(userCode);

		if (sysUser == null) {
			// 抛出异常
			throw new CustomException("用户账号不存在");
		}

		// 数据库密码 (md5密码 )
		String password_db = sysUser.getPassword();

		// 对输入的密码 和数据库密码 进行比对，如果一致，认证通过
		// 对页面输入的密码 进行md5加密
		String password_input_md5 = new MD5().getMD5ofStr(password);
		if (!password_input_md5.equalsIgnoreCase(password_db)) {
			// 抛出异常
			throw new CustomException("用户名或密码 错误");
		}
		// 得到用户id
		Long userid = sysUser.getId();
		// 根据用户id查询菜单
		List<TreeMenu> menuTree = sysMapper.findMenuList();

		// 根据用户id查询权限url
		List<SysPermission> children = sysMapper.getSubMenu();

		// 认证通过，返回用户身份信息
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUserid(sysUser.getId());
		activeUser.setUsercode(userCode);
		activeUser.setUsername(sysUser.getName());// 用户名称

		// 放入权限范围的菜单和url
		activeUser.setMenuTree(menuTree);
		activeUser.setChildren(children);

		return activeUser;
	}

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

	@Override
	public List<SysPermission> loadSubMenu() {
		return sysMapper.getSubMenu();
	}

	@Override
	public List<SysPermission> findPermissionListByUserId(String userid) throws Exception {
		return sysMapper.findPermissionListByUserId(userid);
	}

	// 根据下拉框修改权限
	@Override
	public void updateEmployeeRoles(String roleId, String userId) {
		SysUserRoleExample example = new SysUserRoleExample();
		SysUserRoleExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(userId);
		SysUserRole sysUserRole = sysUserRoleMapper.selectByExample(example).get(0);
		// System.out.println(sysUserRole.getSysUserId());
		sysUserRole.setSysRoleId(roleId);
		sysUserRoleMapper.updateByPrimaryKey(sysUserRole);
	}

	@Override
	public List<SysPermission> findPermissionsByRoleId(String roleId) {
		return sysMapper.findPermissionsByRoleId(roleId);
	}

	// 显示下拉框权限名称
	@Override
	public List<SysRole> findAllRoleList() {
		return roleMapper.selectByExample(null);
	}

}
