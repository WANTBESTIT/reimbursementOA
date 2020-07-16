package cn.web.workflow.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class testReimburesement {
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	@Test
	public void testDeploy() throws FileNotFoundException {
		InputStream input = new FileInputStream(
				"D:\\eclipse_workplace\\jee\\reimbursementOA\\config\\workflow\\baoxiaoProcess.zip");
		ZipInputStream zipIn = new ZipInputStream(input);
		// 一些定义文件，部署文件和支持数据(例如BPMN2.0 XML文件，表单定义文件，流程定义图像文件等)，
		// 这些文件都存储在Activiti内建的Repository中
		Deployment deploy = processEngine.getRepositoryService().createDeployment().name("报销流程")
				.addZipInputStream(zipIn).deploy();

		System.out.println("流程的id：" + deploy.getId());
		System.out.println("流程的name：" + deploy.getName());
	}
	
	 /**
     * 启动流程实例  act_ru_execution：流程实例的执行对象表  instance:代表某一个流程的具体操作
     */
    @Test
    public void testStartProcess3() {
        String key = "baoxiao";
        ProcessInstance instance =
                processEngine.getRuntimeService().startProcessInstanceByKey(key);
        System.out.println("实例id：" + instance.getId());
        System.out.println("流程id: " + instance.getProcessDefinitionId());
    }
}
