package com.homework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.homework.web.service.UserService;

//获取用户账号，密码，权限用于认证。若认证成功则进入认证成功处理器，否则进入认证失败处理器。
@Component
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 获取用户的基本信息
		com.homework.web.pojo.User user = userService.selectByUsername(username);
//		user=new com.homework.web.pojo.User();
//		user.setUsername(username);
//		user.setPassword("123456");
//		user.setRole("ADMIN");
		// 构建包装了用户信息的对象并返回
		return new User(username, passwordEncoder.encode(user.getPassword()),
				AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + user.getRole()));
	}

}
