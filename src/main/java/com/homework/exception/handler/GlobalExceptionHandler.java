package com.homework.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.homework.exception.ControllerException;
import com.homework.exception.ServiceException;
import com.homework.web.util.ResponseObject;

import lombok.extern.slf4j.Slf4j;

//全局异常处理类
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { ControllerException.class, ServiceException.class })
	public ResponseEntity<ResponseObject> controllerExceptionHandler(Exception e) {
		log.info("ControllerException/ServiceException:" + e.getMessage());
		return new ResponseEntity<ResponseObject>(new ResponseObject("400", e.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
}
