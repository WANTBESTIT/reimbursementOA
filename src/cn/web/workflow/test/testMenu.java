package cn.web.workflow.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class testMenu {

	@Autowired
	private SysPermissionCustomMapper sysPermissionCustomMapper;

	@Test
	public void testMenu() {
		List<TreeMenu> menus = sysPermissionCustomMapper.findMenuList();
		for (TreeMenu treeMenu : menus) {
			System.out.println(treeMenu.getId() + "," + treeMenu.getName()); // 一级菜单
		}
		List<SysPermission> subMenu = sysPermissionCustomMapper.getSubMenu();
		for (SysPermission sysPermission : subMenu) {
			System.out.println(
					"\t" + sysPermission.getName() + "," + sysPermission.getUrl() + "," + sysPermission.getPercode()); // 二级菜单
		}
	}
}
