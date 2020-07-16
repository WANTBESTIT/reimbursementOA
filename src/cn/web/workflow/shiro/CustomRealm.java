package cn.web.workflow.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.spi.LoggerFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;
import cn.web.workflow.service.SysService;

public class CustomRealm extends AuthorizingRealm {

	@Autowired
	private SysService sysService;

	// 执行认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		System.out.println("****************执行认证****************");
		String username = (String) token.getPrincipal();
		// 先判断账号是否存在，账号要唯一性
		Employee user = null;
		String password_db = null;
		List<SysPermission> menuTree = null;
		try {
			user = sysService.findSysUserByUserCode(username);

			if (user == null) {
				return null; // UnknownAccountException
			}
			password_db = user.getPassword();
			System.out.println(password_db);
			// 查询当前登录用户的菜单
			menuTree = sysService.findMenuListByUserId(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("**************" + menuTree);
		String salt = user.getSalt();
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUserid(user.getId());
		activeUser.setUsername(user.getName());
		activeUser.setMenuTree(menuTree);

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activeUser, password_db,
				ByteSource.Util.bytes(salt), "CustomRealm");
		return info;
	}

	// 执行授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection priceipal) {
		ActiveUser activeUser = (ActiveUser) priceipal.getPrimaryPrincipal();
		List<SysPermission> permissionList = null;
		try {
			permissionList = sysService.findPermissionListByUserId(activeUser.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> permissions = new ArrayList<>();
		for (SysPermission permission : permissionList) {
			permissions.add(permission.getPercode());
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		// 权限粒度更小
		info.addStringPermissions(permissions);
		return info;
	}
}
