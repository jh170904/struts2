package com.test;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	private String message;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String execute() throws Exception {
		
		message = userName + "님 반가워요!!";
		return SUCCESS; 
		//struts1에서 mapping.findForward("created"); 를 작성했던 것처럼 반환값 설정
		//SUCCESS는 내장변수. 
		//String com.opensymphony.xwork2.Action.SUCCESS = "success"
		//xml파일 작성시 success로 처리하면 된다.
	}
	
}
