package com.homework.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.homework.exception.handler.MyAccessDeniedHandler;
import com.homework.exception.handler.MyAuthenticationEntryPoint;

@Configuration

//开启oauth2,reousrce server模式
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	TokenStore tokenStore;
	@Autowired
	MyAccessDeniedHandler myAccessDeniedHandler;
	@Autowired
	MyAuthenticationEntryPoint myAuthenticationEntryPoint;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources//
				.resourceId("vue_resourceId").tokenStore(tokenStore).stateless(true);//
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http// 权限控制
				.authorizeRequests()//
				.anyRequest().permitAll()//
				// 异常配置
				.and()//
				.exceptionHandling()//
				.authenticationEntryPoint(myAuthenticationEntryPoint)// 未认证
				.accessDeniedHandler(myAccessDeniedHandler)// 已认证但无权限
				// session策略
				.and()//
				.sessionManagement()//
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				// 其他
				.and().cors()//
				.and().csrf().disable();//
	}

}
