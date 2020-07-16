package cn.web.workflow.service;

import java.util.List;

import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.BaoxiaobillExample;

public interface BaoxiaoBillService {
	// 根据报销ID显示报销单
	public List<Baoxiaobill> findBiaoxiaoBill(BaoxiaobillExample baoxiaoBill);

	public List<String> findOutComeListByTaskId(String taskId);

	public void deleteBaoxiaoBillById(Integer id);

	public Baoxiaobill findBaoxiaoBillById(Long id);

}
