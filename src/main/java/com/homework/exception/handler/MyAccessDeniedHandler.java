package com.homework.exception.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.web.util.ResponseObject;

import lombok.extern.slf4j.Slf4j;

//无权限异常处理器
@Component
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("已认证无权限，返回JSON格式的异常信息");
		response.setStatus(403);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(objectMapper.writeValueAsString(new ResponseObject("403", "已认证无权限", null)));
		writer.flush();
		writer.close();
	}

}
