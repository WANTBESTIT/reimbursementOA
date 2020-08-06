package cn.web.workflow.pojo;

import java.util.List;

/**
 * 用户身份信息，存入session 由于tomcat将session会序列化在本地硬盘上，所以使用Serializable接口
 * 
 * @author Thinkpad
 * 
 */
public class ActiveUser extends Employee implements java.io.Serializable {
	private Long userid;// 用户id（主键）
	private String usercode;// 用户账号
	private String username;// 用户名称

	private List<TreeMenu> menuTree;// 菜单
	private List<SysPermission> children;// 权限

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public List<TreeMenu> getMenuTree() {
		return menuTree;
	}

	public void setMenuTree(List<TreeMenu> menuTree) {
		this.menuTree = menuTree;
	}

	public List<SysPermission> getChildren() {
		return children;
	}

	public void setChildren(List<SysPermission> children) {
		this.children = children;
	}

}
