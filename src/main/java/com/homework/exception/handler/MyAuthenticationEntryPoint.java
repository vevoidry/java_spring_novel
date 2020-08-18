package com.homework.exception.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.web.util.ResponseObject;

import lombok.extern.slf4j.Slf4j;

//未认证异常处理器
@Component
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("未认证，返回JSON格式的异常信息");
		response.setStatus(401);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(objectMapper.writeValueAsString(new ResponseObject("401", "未认证", null)));
		writer.flush();
		writer.close();
	}

}
