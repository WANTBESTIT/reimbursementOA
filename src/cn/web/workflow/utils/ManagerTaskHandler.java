package cn.web.workflow.utils;

import javax.servlet.http.HttpSession;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.web.workflow.pojo.Employee;
import cn.web.workflow.service.EmployeeService;

public class ManagerTaskHandler implements TaskListener {

	@Override
	public void notify(DelegateTask task) {
		// 硬编程
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		// 通过上下文获取得到EmployeeServiceImplement实现类
		EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");

		// 要获得上级id，必须先获取当前用户id
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);

		Employee manager = employeeService.findEmployeeByManagerId(employee.getManagerId());

		// 设置待办人名称
		task.setAssignee(manager.getName());
	}

}
