package cn.web.workflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysRoleExample;
import cn.web.workflow.pojo.TreeMenu;
import cn.web.workflow.service.EmployeeService;
import cn.web.workflow.service.SysService;
import cn.web.workflow.utils.Constants;

/**
 * 用户功能实现
 * 
 * @author 滨仔
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SysPermissionCustomMapper sysPermissionCustomMapper;

	@Autowired
	private SysService sysService;

	// 主界面
	@RequestMapping("/main")
	public ModelAndView main(Model model, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		session.setAttribute(Constants.GLOBAL_SESSION_ID, activeUser);
		List<TreeMenu> menuTree = sysPermissionCustomMapper.findMenuList();
		mv.addObject("menuList", menuTree);
		mv.setViewName("index");
		return mv;
	}

	// 用户列表显示
	@RequestMapping("/findUserList")
	public ModelAndView findUserList(SysRoleExample sysRoleExample) {
		ModelAndView mv = new ModelAndView();
		List<EmployeeCustom> userList = employeeService.findUserAndRoleList();
		List<SysRole> allRoles = sysService.findAllRoleList();
		// for (SysRole sysRole : allRoles) {
		// System.out.println("=========="+sysRole.getId());
		// }
		mv.addObject("userList", userList);
		mv.addObject("allRoles", allRoles);
		mv.setViewName("userlist");
		return mv;
	}

	// 修改权限级别
	@RequestMapping("/assignRole")
	@ResponseBody
	public Map<String, String> assignRole(String roleId, String userId) {
		System.out.println(roleId);
		System.out.println(userId);
		Map<String, String> map = new HashMap<>();
		try {
			sysService.updateEmployeeRoles(roleId, userId);
			map.put("msg", "权限设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "权限设置失败");
		}
		return map;
	}

	// 【角色管理】 查看权限
	@RequestMapping("/viewPermissionByUser")
	@ResponseBody
	public SysRole viewPermissionByUser(String userName) {
		SysRole sysRole = sysPermissionCustomMapper.findRoleAndPermissionLIstByUserId(userName);
		// System.out.println("所选权限级别名称：" + sysRole.getName()+
		// "==============");
		return sysRole;
	}

	// 【角色管理】
	@RequestMapping("/toAddRole")
	public ModelAndView toAddRole() {
		ModelAndView mv = new ModelAndView();
		List<TreeMenu> allPermissions = sysPermissionCustomMapper.getAllMenuAndPermission();
		List<TreeMenu> saveSubmitPermission = sysPermissionCustomMapper.getSubMenuAndPermissions();
		mv.addObject("allPermissions", allPermissions);
		mv.addObject("saveSubmitPermission", saveSubmitPermission);
		mv.setViewName("rolelist");
		return mv;
	}

	// 保存角色和权限(角色添加内)
	@RequestMapping("/saveRoleAndPermissions")
	public String saveRoleAndPermissions(SysRole sysRole, int[] permissionIds) {
		// for (int i : permissionIds) {
		// System.out.println(i);
		// }
		// System.out.println(role.getName());

		// role主键，使用uuid
		String uuid = UUID.randomUUID().toString();
		sysRole.setId(uuid);
		sysRole.setAvailable("1"); // 可用
		employeeService.addRoleAndPermissions(sysRole, permissionIds);
		return "redirect:/findRoles";
	}

	// 新建权限
	@RequestMapping("/saveSubmitPermission")
	public String saveSubmitPermission(SysPermission sysPermission) {
		if (sysPermission.getAvailable() == null) {
			sysPermission.setAvailable("0");
		}
		employeeService.addPermission(sysPermission);
		return "redirect:/toAddRole";
	}

	// 【角色列表】
	@RequestMapping("/findRoles")
	public ModelAndView findRoles() {
		ModelAndView mv = new ModelAndView();
		List<SysRole> allRoles = sysPermissionCustomMapper.findAllRoleName();
		mv.addObject("allRoles", allRoles);
		mv.setViewName("permissionlist");
		return mv;
	}

	// 【角色列表】编辑
	@RequestMapping("/loadMyPermissions")
	@ResponseBody
	public List<SysPermission> loadMyPermissions(String roleId) {
		List<SysPermission> allMenuAndPermissions = sysService.findPermissionsByRoleId(roleId);
		for (SysPermission sysPermission : allMenuAndPermissions) {
			System.out.println(sysPermission.getId() + "," + sysPermission.getType() + "\n" + sysPermission.getName()
					+ "," + sysPermission.getUrl() + "," + sysPermission.getPercode());
		}
		return allMenuAndPermissions;
	}

	// 【角色列表】 修改
	@RequestMapping("/updateRoleAndPermission")
	public String updateRoleAndPermission(String roleId, int[] permissionIds) {
		employeeService.updateRoleAndPermissions(roleId, permissionIds);
		return "redirect:/findRoles";

	}

	// //【角色列表】 删除
	// @RequestMapping("/deleteRole")
	// public String deleteRole(String id){
	// System.out.println("删除角色================="+id);
	// employeeService.deleteRoleById(id);
	// return "redirect:/findRoles";
	// }

	// @RequestMapping("/login")
	// public String login(String username, String password, Model model,
	// HttpSession session) {
	// Employee loginEmployee = null;
	// try {
	// loginEmployee = employeeService.findUserByUsername(username);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if(loginEmployee != null){
	// //判断密码
	// if(loginEmployee.getPassword().equals(password)){
	// session.setAttribute(Constants.GLOBAL_SESSION_ID, loginEmployee);
	// List<TreeMenu> menuList = sysService.findMenuList();
	// for (TreeMenu treeMenu : menuList) {
	// System.out.println(treeMenu.getId() + "," + treeMenu.getName()); // 一级菜单
	//
	// for (SysPermission sysPermission : treeMenu.getChildren()) {
	// System.out.println("\t"+sysPermission.getName());
	// }
	//
	// }
	// model.addAttribute("menuList", menuList);
	// return "index";
	// }
	// model.addAttribute("errorMsg", "账号或密码错误");
	// return "login";
	// }else{
	// model.addAttribute("errorMsg", "账号或密码错误");
	// return "login";
	// }
	// }
	//
	// @RequestMapping("/logout")
	// public String logout(HttpSession session) {
	// session.invalidate();
	// return "login";
	// }
	@RequestMapping("/saveUser")
	public String saveUser(Employee user) {
		return null;
	}

	@RequestMapping("/findNextManager")
	@ResponseBody
	public List<Employee> findNextManager(int level) {
		level++; // 加一，表示下一个级别
		List<Employee> list = employeeService.findEmployeeByLevel(level);
		System.out.println(list);
		return list;

	}

}
