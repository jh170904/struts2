package com.test1;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	//Domain Object를 변수형식으로 생성 가능(struts2 덕분!)
	private User user;
	private String message;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	//message는 Setter를 사용하지않고 execute 메소드를 통해 생성할 것
	public String getMessage() {
		return message;
	}
	
	@Override
	public String execute() throws Exception {
		
		message = user.getUserName() + "님 반가워요!";
		return "ok";//반환값으로 문자열도 가능
	}
}
