package com.homework.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration

//开启oauth2,auth server模式
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private JwtAccessTokenConverter tokenConverter;
	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;// 密码模式才需要配置,认证管理器
	@Autowired
	private ClientDetailsService clientDetailsService;

	// 配置客户端
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				// 客户端1
				.withClient("vue")// 账号
				.secret(passwordEncoder.encode("123456"))// 密码
				.resourceIds("vue_resourceId") //
				.authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit",
						"refresh_token")// 支持的授权模式
				.scopes("vue_scope")// 授权范围
				.autoApprove(false)// 是否无需确认自动授权
				.redirectUris("http://www.baidu.com");// 重定向地址
	}

	// 配置token管理服务
	@Bean
	public AuthorizationServerTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setClientDetailsService(clientDetailsService);
		defaultTokenServices.setSupportRefreshToken(true);// 支持刷新token
		defaultTokenServices.setTokenStore(tokenStore);// 配置token的存储方法
		defaultTokenServices.setAccessTokenValiditySeconds(3600 * 24);// token有效时长
		defaultTokenServices.setRefreshTokenValiditySeconds(3600 * 24 * 7);// 刷新token的有效时长
		// 配置token增强,把一般token转换为jwt token
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenConverter));
		defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
		return defaultTokenServices;
	}

	// 把上面的各个组件组合在一起
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)// 认证管理器
				.authorizationCodeServices(new InMemoryAuthorizationCodeServices())// 授权码管理
				.tokenServices(tokenServices())// token管理
				.allowedTokenEndpointRequestMethods(HttpMethod.POST);
	}

	// 配置哪些接口可以被访问
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")//
				.checkTokenAccess("permitAll()")//
				.allowFormAuthenticationForClients();// 允许表单认证
	}

}