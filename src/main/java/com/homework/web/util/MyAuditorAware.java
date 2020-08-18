package com.homework.web.util;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

//JPA的一些功能需要用到登录用户的信息
@Configuration
public class MyAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("System");
	}

}
