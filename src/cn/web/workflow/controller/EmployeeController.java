package cn.web.workflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.web.workflow.exception.CustomException;
import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysRoleExample;
import cn.web.workflow.service.EmployeeService;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SysPermissionCustomMapper sysService;

	@RequestMapping("/main")
	public String main(ModelMap model) {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("activeUser", activeUser);
		return "index";
	}

	// 用户列表显示
	@RequestMapping("/findUserList")
	public ModelAndView findUserList(SysRoleExample sysRoleExample) {
		ModelAndView mv = new ModelAndView();
		List<EmployeeCustom> userList = employeeService.findUserAndRoleList();
		List<SysRole> allRoles = employeeService.findAllRoleList(sysRoleExample);
		mv.addObject("userList", userList);
		mv.addObject("allRoles", allRoles);
		mv.setViewName("userlist");
		return mv;
	}

	// 修改权限级别
	@RequestMapping("/assignRole")
	@ResponseBody
	public Map<String, String> assignRole(String roleId, String userId) {
		Map<String, String> map = new HashMap<>();
		try{
			employeeService.updateEmployeeRoles(roleId, userId);
			map.put("msg", "权限设置成功");
		}catch(Exception e){
			e.printStackTrace();
			map.put("msg", "权限设置失败");
		}
		return map;
	}

	// 查看权限
	@RequestMapping("/viewPermissionByUser")
	@ResponseBody
	public SysRole viewPermissionByUser(String name) {
		SysRole sysRole = sysService.findRoleAndPermissionLIstByUserId(name);
		System.out.println("所选权限级别名称：" + sysRole.getName() + "=============="); // null
		return sysRole;
	}

	// @RequestMapping("/login")
	// public String login(String username, String password, HttpSession
	// session, Model model) {
	//
	// Employee employee = employeeService.findUserByUsername(username);
	// if (employee != null) {
	// if (password.equals(employee.getPassword())) {
	// session.setAttribute(Constants.GLOBAL_SESSION_ID, employee);
	// return "index";
	// } else {
	// model.addAttribute("errorMsg", "密码错误");
	// return "login";
	// }
	// } else {
	// // 失败，login.jsp
	// model.addAttribute("errorMsg", "帐号不存在");
	// return "login";
	// }
	// }
	//
	// @RequestMapping("/logout")
	// public String logout(HttpSession session) {
	// session.invalidate();
	// return "login";
	// }

	@RequestMapping("/findNextManager")
	@ResponseBody
	public List<Employee> findNextManager(int level) {
		level++; // 下一个级别
		List<Employee> list = employeeService.findEmployeeByLevel(level);
		return list;
	}

}
