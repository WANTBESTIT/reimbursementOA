package cn.web.workflow.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.web.workflow.exception.CustomException;
/**
 * 登录认证
 * @author 滨仔
 *
 */
@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login(HttpServletRequest request, Model model){
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");		
		if(exceptionName != null){
			if(UnknownAccountException.class.getName().equals(exceptionName)){
				model.addAttribute("errorMsg", "账号或密码错误");
				return "login";
			}  else if(IncorrectCredentialsException.class.getName().equals(exceptionName)){
				System.out.println("账号或密码错误");
				model.addAttribute("errorMsg", "账号或密码错误");
				return "login";
			} 
		}
		return "login"; //该login代表login.jsp,重定向到登录页面
	}
}
