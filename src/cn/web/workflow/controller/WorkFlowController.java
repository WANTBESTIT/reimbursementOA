package cn.web.workflow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.service.BaoxiaoBillService;
import cn.web.workflow.service.WorkFlowService;

/**
 * 工作流程功能实现
 * 
 * @author 滨仔
 *
 */
@Controller
public class WorkFlowController {

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	private BaoxiaoBillService baoxiaoBillService;

	// 发布流程
	@RequestMapping("/deployProcess")
	public String deployProcess(String processName, MultipartFile processFile) throws IOException {
		workFlowService.deployProcess(processName, processFile.getInputStream());
		return "redirect:/processDefinitionList";
	}

	// 查看流程
	@RequestMapping("/processDefinitionList")
	public ModelAndView processDefinitionList() {
		// 流程定义列表
		List<ProcessDefinition> pdList = workFlowService.findAllProcessDefinitions();
		// 部署流程列表
		List<Deployment> deployList = workFlowService.findAllDeployments();
		ModelAndView mv = new ModelAndView();
		mv.addObject("pdList", pdList);
		mv.addObject("depList", deployList);
		mv.setViewName("workflow_list");
		return mv;
	}

	// 删除部署流程
	@RequestMapping("/delDeployment/{id}")
	public String delDeployment(@PathVariable String id) {
		workFlowService.delectDeployment(id);
		return "redirect:/processDefinitionList";
	}

	// 提交报销单数据
	@RequestMapping("/saveStartLeave")
	public String saveStartLeave(Baoxiaobill baoxiaoBill, HttpSession session) {
		ActiveUser employee = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		workFlowService.saveStartProcess(baoxiaoBill, employee);
		return "redirect:/myTaskList";
	}

	// 我的待办事务
	@RequestMapping("/myTaskList")
	public ModelAndView myTaskList(HttpSession session) {
		ActiveUser employee = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		List<Task> list = workFlowService.findTaskListByUserId(employee.getUsername());
		ModelAndView mv = new ModelAndView();
		mv.addObject("taskList", list);
		mv.setViewName("workflow_task");
		return mv;
	}

	// 办理任务
	@RequestMapping("/submitTask")
	public String submitTask(Integer id, String taskId, String comment, String outcome, HttpSession session) {
		ActiveUser employee = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		workFlowService.savesubmitTask(id, taskId, comment, outcome, employee.getUsername());
		return "redirect:/myTaskList";
	}

	// 显示批注信息
	@RequestMapping("/viewTaskForm")
	public ModelAndView viewTaskForm(String taskId) {

		Baoxiaobill baoxiaoBill = workFlowService.findBaoxiaoBillByTaskId(taskId);

		List<Comment> commentList = workFlowService.findCommentListByTaskId(taskId);
		// 获取当前活动节点的连线信息
		List<String> outcomeList = baoxiaoBillService.findOutComeListByTaskId(taskId);

		ModelAndView mv = new ModelAndView();
		mv.addObject("bill", baoxiaoBill);
		mv.addObject("commentList", commentList);
		mv.addObject("outcomeList", outcomeList);
		mv.addObject("taskId", taskId);
		mv.setViewName("approve_baoxiao");
		return mv;
	}

	// 查看流程图
	@RequestMapping("/viewImage")
	public String viewImage(String deploymentId, String imageName, HttpServletResponse response) throws IOException {
		InputStream in = workFlowService.findImageInputStream(deploymentId, imageName);
		System.out.println("deploymentId=========" + deploymentId);
		System.out.println("imageName=========" + imageName);
		OutputStream out = response.getOutputStream();

		// 4：将输入流中的数据读取出来，写到输出流中
		for (int b = -1; (b = in.read()) != -1;) {
			out.write(b);
		}
		out.close();
		in.close();
		return null;
	}

	// 查看流程当前进程图
	@RequestMapping("/viewCurrentImage")
	public ModelAndView viewCurrentImage(String taskId) {
		/** 一：查看流程图 */
		// 1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);

		ModelAndView mv = new ModelAndView();
		mv.addObject("deploymentId", pd.getDeploymentId());
		mv.addObject("imageName", pd.getDiagramResourceName());
		/** 二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中 */
		Map<String, Object> map = workFlowService.findCoordingByTask(taskId);

		mv.addObject("acs", map);
		mv.setViewName("viewimage");
		return mv;
	}

}
