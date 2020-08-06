package cn.web.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Baoxiaobill;

public interface WorkFlowService {

	public void deployProcess(String processName, InputStream input);

	// 查找所有部署流程信息
	public List<Deployment> findAllDeployments();

	// 查找所有流程定义信息
	public List<ProcessDefinition> findAllProcessDefinitions();

	public void saveStartProcess(Baoxiaobill baoxiaoBill, ActiveUser employee);

	public List<Task> findTaskListByUserId(String name);

	// 根据当前任务ID查询报销单数据
	public Baoxiaobill findBaoxiaoBillByTaskId(String taskId);

	public List<Comment> findCommentListByTaskId(String taskId);

	public void savesubmitTask(Integer id, String taskId, String comment, String outcome, String name);

	public ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	// 通过taskId查找当前活动
	public Map<String, Object> findCoordingByTask(String taskId);

	// 删除部署的流程
	public void delectDeployment(String id);

	// 查找流程图
	public InputStream findImageInputStream(String deploymentId, String imageName);

	public Task findTaskByBussinessKey(String BUSSINESS_KEY);

	public List<Comment> findCommentByBaoxiaoBillId(Long id);

}
