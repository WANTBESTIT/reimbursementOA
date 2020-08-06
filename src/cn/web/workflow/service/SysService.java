package cn.web.workflow.service;

import java.util.List;

import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.TreeMenu;

public interface SysService {
	
	// 根据用户的身份和密码 进行认证，如果认证通过，返回用户身份信息
	public ActiveUser authenticat(String userCode, String password) throws Exception;

	// 通过用户名进行用户认证
	public Employee findSysUserByUserCode(String username);

	// 加载一级菜单
	public List<TreeMenu> loadMenuTree();
	
	// 加载二级菜单
	public List<SysPermission> loadSubMenu();
	
	List<SysPermission> findPermissionListByUserId(String userid) throws Exception;

	public void updateEmployeeRoles(String roleId, String userId);

	// 根据角色Id查找权限
	public List<SysPermission> findPermissionsByRoleId(String roleId);

    // 显示下拉框权限名称
	public List<SysRole> findAllRoleList();

}
