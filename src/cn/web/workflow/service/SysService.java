package cn.web.workflow.service;

import java.util.List;

import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;

public interface SysService {

	// 通过用户名进行用户认证
	public Employee findSysUserByUserCode(String username);

	public List<TreeMenu> loadMenuTree();

	// 根据用户id查询权限范围的菜单
	public List<SysPermission> findMenuListByUserId(String userid) throws Exception;

	// 根据用户id查询权限范围的url
	public List<SysPermission> findPermissionListByUserId(String userid) throws Exception;

}
