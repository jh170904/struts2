package com.test2;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class TestAction extends ActionSupport 
implements Preparable,ModelDriven<User>{//Model�� �����Ϳ� �ش�

	//���������� if������ ��� 
	//���������� doPost, doGet�޼ҵ带 �ݵ�� �����߾�� ���� 
	//Struts1������ �޼ҵ�� �����Ͽ� ȣ��
	
	//Struts2�� �⺻������ �Ʒ��� ����. getDto(), getModel(), prepare()
	private static final long serialVersionUID = 1L;
	private User dto;
	
	public User getDto() {
		//getParameter�� ����
		return dto;
	}

	//ModelDriven�������̽�(model ����)
	@Override
	public User getModel() {
		return dto;
	}
	
	//Preparable�������̽�(dto ����)
	@Override
	public void prepare() throws Exception {
		dto = new User();//��ü ����
		//ȣ���ϴ� ��ɾ ��� struts2�� �˾Ƽ� ����
	}
	
	//struts1������ �Ű������� request�� response�� �׻� ���� �߾���
	//struts2������ �⺻������ �Ű������� �Է����� �����Ƿ� �޼ҵ��� �ڵ��� �ܼ�����
	//������ ����Ҷ����� ��û�� �ؾ���
	public String created() throws Exception {
		
		//==�� .equals()�� ������ �ٲ�� ������. ����!
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")){
			return INPUT; //���庯�� String com.opensymphony.xwork2.Action.INPUT = "input"
		}
		//������ ����� ���� request. ������ �Ű������� �Է����� �ʾ����Ƿ� �������־�� �Ѵ�
		//ServletActionContextŬ������ request�� ��û�ؼ� ����(Context�� ��ü�� �ǹ�!)
		HttpServletRequest request = ServletActionContext.getRequest(); 
		request.setAttribute("message", "��Ʈ����2.."); 
		//request.setAttribute("dto", dto);//�� �����͸� �Ѱ���µ� 
		//struts2�� �ڵ����� getDto �޼ҵ带 ���� ����
		
		return SUCCESS;
	}

}
