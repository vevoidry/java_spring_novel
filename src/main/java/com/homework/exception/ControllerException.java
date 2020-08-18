package com.homework.exception;

//Controller层抛出的异常
public class ControllerException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ControllerException() {
		super();
	}
	
	public ControllerException(String message) {
		super(message);
	}

}
