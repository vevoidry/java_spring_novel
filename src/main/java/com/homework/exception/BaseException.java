package com.homework.exception;

//所有自定义异常的父类，相当于一个中间层
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BaseException() {
		super();
	}

	public BaseException(String message) {
		super(message);
	}

}
