package cn.web.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.web.workflow.mapper.BaoxiaobillMapper;
import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.BaoxiaobillExample;
import cn.web.workflow.service.BaoxiaoBillService;
import cn.web.workflow.utils.Constants;

@Service
public class BaoxiaoBillServiceImpl implements BaoxiaoBillService {

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

	//动态获取流程方向
	@Override
	public List<String> findOutComeListByTaskId(String taskId) {
		// 返回存放连线的名称集合
		List<String> list = new ArrayList<String>();
		// 1:使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 2：获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		// 3：查询ProcessDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		// 使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		// 使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
				.processInstanceId(processInstanceId)// 使用流程实例ID查询
				.singleResult();
		// 获取当前活动的ID
		String activityId = pi.getActivityId();
		// 4：获取当前活动完成之后连线的名称
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		// 5：获取当前活动完成之后连线的名称
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if (pvmList != null && pvmList.size() > 0) {
			for (PvmTransition pvm : pvmList) {
				String name = (String) pvm.getProperty("name");
				if (StringUtils.isNotBlank(name)) {
					list.add(name);
				} else {
					list.add("默认提交");
				}
			}
		}
		return list;
	}

	// 我的报销单
	@Override
	public List<Baoxiaobill> findBiaoxiaoBill(BaoxiaobillExample baoxiaobill) {
		List<Baoxiaobill> baoxiaoList = baoxiaoBillMapper.selectByExample(baoxiaobill);
		return baoxiaoList;
	}
	//报销单中删除流程结束了的数据
	@Override
	public void deleteBaoxiaoBillById(Integer id) {
		baoxiaoBillMapper.deleteByPrimaryKey(id);
	}

	// 根据报销单ID查找报销单数据
	@Override
	public Baoxiaobill findBaoxiaoBillById(Long id) {
		Baoxiaobill baoxiaoBill = baoxiaoBillMapper.selectByPrimaryKey(Integer.parseInt(id.toString()));
		return baoxiaoBill;
	}
}
