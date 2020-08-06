package cn.web.workflow.controller;

import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.BaoxiaobillExample;
import cn.web.workflow.service.BaoxiaoBillService;
import cn.web.workflow.service.WorkFlowService;
import cn.web.workflow.utils.Constants;
/**
 * 报销功能实现
 * @author 滨仔
 *
 */
@Controller
public class BaoxiaoBillController {

	@Autowired
	private BaoxiaoBillService baoxiaoBillService;
	@Autowired
	private WorkFlowService workFlowService;

	// 我的报销单
	@RequestMapping("/myBaoxiaoBill")
	public ModelAndView myBaoxiaoBill(BaoxiaobillExample baoxiaoBill,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
		ModelAndView mv = new ModelAndView();
		// 分页拦截
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		List<Baoxiaobill> baoxiaoList = baoxiaoBillService.findBiaoxiaoBill(baoxiaoBill);
		PageInfo pageInfos = new PageInfo<>(baoxiaoList);

		mv.addObject("pageInfo", pageInfos);
		mv.addObject("baoxiaoList", baoxiaoList);
		mv.setViewName("baoxiao_list");
		return mv;
	}

	// 我的报销单-->查看当前流程图
	@RequestMapping("/viewCurrentImageByBill")
	public String viewCurrentImageByBill(Integer id,ModelMap model) {
		String BUSSINESS_KEY = Constants.BAOXIAO_KEY + "." + id;
		System.out.println("=========="+BUSSINESS_KEY+"==========");
		Task task = this.workFlowService.findTaskByBussinessKey(BUSSINESS_KEY);
		/**一：查看流程图*/
		//1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(task.getId());
        System.out.println(pd);
		model.addAttribute("deploymentId", pd.getDeploymentId());
		model.addAttribute("imageName", pd.getDiagramResourceName());
		/**二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中*/
		Map<String, Object> map = workFlowService.findCoordingByTask(task.getId());

		model.addAttribute("acs", map);
		return "viewimage";
	}

	// 查询审核记录
	@RequestMapping("/findInMyBaoxiaoBill")
	public ModelAndView findInMyBaoxiaoBill(Long id) {
		Baoxiaobill baoxiaoBill = baoxiaoBillService.findBaoxiaoBillById(id);
		List<Comment> commentList = workFlowService.findCommentByBaoxiaoBillId(id);
		ModelAndView mv = new ModelAndView();
		mv.addObject("bill", baoxiaoBill);
		mv.addObject("commentList", commentList);
		mv.setViewName("baoxiaolist_his");
		return mv;
	}

	// 删除报销表中的数据
	@RequestMapping("/deleteBaoxiaoBill/{id}")
	public String deleteBaoxiaoBill(@PathVariable Integer id) {
		baoxiaoBillService.deleteBaoxiaoBillById(id);
		return "redirect:/myBaoxiaoBill";
	}
}
