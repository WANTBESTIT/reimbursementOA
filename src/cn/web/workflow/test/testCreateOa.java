package cn.web.workflow.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class testCreateOa {

	@Test
	public void testCreate(){
		ProcessEngineConfiguration config = 
				ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		ProcessEngine processEngine = config.buildProcessEngine();
	}
}
