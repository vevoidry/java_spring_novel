package com.homework.web.util;

//响应结果，任何请求的响应结果都是该类型的，即使抛出异常
public class ResponseObject {
	
	String code;
	String message;
	Object data;

	public ResponseObject(String code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
