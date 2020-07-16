package cn.web.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.Employee;

public interface WorkFlowService {

	public void deployProcess(String processName,InputStream input);
	
	public List<Deployment> findAllDeployments();
	
	public List<ProcessDefinition> findAllProcessDefinitions();
	
	public void saveStartProcess(Baoxiaobill baoxiaoBill, Employee employee);

	public List<Task> findTaskListByUserId(String name);

	// 根据当前任务ID查询报销单数据
	public Baoxiaobill findBaoxiaoBillByTaskId(String taskId);

	public List<Comment> findCommentListByTaskId(String taskId);

	public void savesubmitTask(Integer id,String taskId,String comment,String outcome,String name);

	public ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	public Map<String, Object> findCoordingByTask(String taskId);

	public void delectDeployment(String id);

	public InputStream findImageInputStream(String deploymentId, String imageName);

	public Task findTaskByBussinessKey(String BUSSINESS_KEY);

	public List<Comment> findCommentByBaoxiaoBillId(Long id);

}
