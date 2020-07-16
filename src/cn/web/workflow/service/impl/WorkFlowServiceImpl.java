package cn.web.workflow.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.web.workflow.mapper.BaoxiaobillMapper;
import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.service.WorkFlowService;
import cn.web.workflow.utils.Constants;

@Service
public class WorkFlowServiceImpl implements WorkFlowService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private BaoxiaobillMapper baoxiaoBillMapper;

	@Override
	public List<ProcessDefinition> findAllProcessDefinitions() {
		return repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().list();

	}

	// 查询所有的流程信息
	@Override
	public List<Deployment> findAllDeployments() {
		return repositoryService.createDeploymentQuery().list();
	}

	// 部署流程
	@Override
	public void deployProcess(String processName, InputStream input) {
		ZipInputStream zipInput = new ZipInputStream(input);
		Deployment deployment = repositoryService.createDeployment().name(processName).addZipInputStream(zipInput)
				.deploy();
	}

	// 提交
	@Override
	public void saveStartProcess(Baoxiaobill baoxiaoBill, Employee employee) {
		// 1. 保存请假单
		baoxiaoBill.setCreatdate(new Date());
		baoxiaoBill.setState(1);
		baoxiaoBill.setUserId(Integer.parseInt(employee.getId().toString()));
		baoxiaoBillMapper.insert(baoxiaoBill); // mybatis把主键回填到pojo对象中

		// 2. 启动流程
		String key = Constants.BAOXIAO_KEY;
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置流程变量=待办人
		map.put("inputUser", employee.getName());
		String BUSSINESS_KEY = Constants.BAOXIAO_KEY + "." + baoxiaoBill.getId();
		System.out.println(BUSSINESS_KEY);

		// 怎样把流程业务数据和应用的业务表的数据相关联： 例如：如果得到流程实例，可以查询出对应的员工信息
		runtimeService.startProcessInstanceByKey(key, BUSSINESS_KEY, map);
	}

	@Override
	public List<Task> findTaskListByUserId(String name) {
		return taskService.createTaskQuery().taskAssignee(name).list();
	}

	// 根据任务流程id查找请假单
	@Override
	public Baoxiaobill findBaoxiaoBillByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 获取流程实例Id
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
				.singleResult();
		String bussin_key = pi.getBusinessKey();
		System.out.println(bussin_key); // 流程.请假单Id 
		// 获取请假单的Id
		String billId = bussin_key.split("\\.")[1];
		System.out.println(billId);
		Baoxiaobill leavebill = baoxiaoBillMapper.selectByPrimaryKey(Integer.parseInt(billId));
		return leavebill;
	}

	// 根据任务ID查找批注
	@Override
	public List<Comment> findCommentListByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据流程实例Id查找批注
		List<Comment> commentList = taskService.getProcessInstanceComments(task.getProcessInstanceId());
		return commentList;
	}

	// 保存提交的任务数据
	@Override
	public void savesubmitTask(Integer id, String taskId, String comment, String outcome, String name) {
		// 添加批注
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 添加当前任务的代办人
		Authentication.setAuthenticatedUserId(name);
		String processInstanceId = task.getProcessInstanceId();
		taskService.addComment(taskId, processInstanceId, comment);
		Map<String,Object> variables = new HashMap<>();
		if(outcome!=null && !outcome.equals("默认提交")){
			variables.put("message", outcome);
			taskService.complete(taskId,variables);
		}else{
			taskService.complete(taskId);
		}
		// 获取流程实例id
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		// 更改请假单的状态
		if (pi == null) { // 流程结束
			Baoxiaobill baoxioaBill = baoxiaoBillMapper.selectByPrimaryKey(id);
			baoxioaBill.setState(2);
			baoxiaoBillMapper.updateByPrimaryKey(baoxioaBill);
		}
	}

	// 根据任务ID查询流程定义ID
	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		// 使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		// 查询流程定义的对象
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()// 创建流程定义查询对象，对应表act_re_procdef
				.processDefinitionId(processDefinitionId)// 使用流程定义ID查询
				.singleResult();
		return pd;
	}

	@Override
	public Map<String, Object> findCoordingByTask(String taskId) {
		// 存放坐标
		Map<String, Object> map = new HashMap<String, Object>();
		// 使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)// 使用任务ID查询
				.singleResult();
		// 获取流程定义的ID
		String processDefinitionId = task.getProcessDefinitionId();
		// 获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		// 流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		// 使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()// 创建流程实例查询
				.processInstanceId(processInstanceId)// 使用流程实例ID查询
				.singleResult();
		// 获取当前活动的ID
		String activityId = pi.getActivityId();
		// 获取当前活动对象
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);// 活动ID
		// 获取坐标
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		return map;
	}

	// 删除流程
	@Override
	public void delectDeployment(String id) {
		repositoryService.deleteDeployment(id, true);
	}

	// 获取流程图片
	@Override
	public InputStream findImageInputStream(String deploymentId, String imageName) {
		return this.repositoryService.getResourceAsStream(deploymentId, imageName);
	}

	// 在我的报销单中查看当前流程图
	@Override
	public Task findTaskByBussinessKey(String BUSSINESS_KEY) {
		Task task = this.taskService.createTaskQuery().processInstanceBusinessKey(BUSSINESS_KEY).singleResult();
		return task;
	}
	
	//根据报销单ID查询历史批注
	@Override
	public List<Comment> findCommentByBaoxiaoBillId(Long id) {
//		String test= String.valueOf(id);
		String bussiness_key = Constants.BAOXIAO_KEY +"."+id;
		System.out.println(bussiness_key);
		HistoricProcessInstance pi = this.historyService.createHistoricProcessInstanceQuery()
													.processInstanceBusinessKey(bussiness_key).singleResult();		
		List<Comment> commentList = this.taskService.getProcessInstanceComments(pi.getId());		
		return commentList;
	}	

}
