package com.test1;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	//Domain Object�� ������������ ���� ����(struts2 ����!)
	private User user;
	private String message;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	//message�� Setter�� ��������ʰ� execute �޼ҵ带 ���� ������ ��
	public String getMessage() {
		return message;
	}
	
	@Override
	public String execute() throws Exception {
		
		message = user.getUserName() + "�� �ݰ�����!";
		return "ok";//��ȯ������ ���ڿ��� ����
	}
}
