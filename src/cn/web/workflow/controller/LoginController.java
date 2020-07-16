package cn.web.workflow.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.web.workflow.exception.CustomException;

@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login(HttpServletRequest request) throws Exception {
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");
		System.out.println("错误名：" + exceptionName);
		if (exceptionName != null) {
			// 1. 账号异常
			if (UnknownAccountException.class.getName().equals(exceptionName)) {
				throw new CustomException("账号错误哦");
			}
			if (IncorrectCredentialsException.class.getName().equals(exceptionName)) {
				throw new CustomException("密码错误哦");
			}
		}
		return "login";
	}
}
