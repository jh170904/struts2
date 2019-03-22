package com.test2;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class TestAction extends ActionSupport 
implements Preparable,ModelDriven<User>{//Model은 데이터에 해당

	//서블릿에서는 if문으로 사용 
	//서블릿에서는 doPost, doGet메소드를 반드시 생성했어야 했음 
	//Struts1에서는 메소드로 분해하여 호출
	
	//Struts2의 기본형식은 아래와 같다. getDto(), getModel(), prepare()
	private static final long serialVersionUID = 1L;
	private User dto;
	
	public User getDto() {
		//getParameter의 역할
		return dto;
	}

	//ModelDriven인터페이스(model 관리)
	@Override
	public User getModel() {
		return dto;
	}
	
	//Preparable인터페이스(dto 생성)
	@Override
	public void prepare() throws Exception {
		dto = new User();//객체 생성
		//호출하는 명령어가 없어도 struts2가 알아서 진행
	}
	
	//struts1에서는 매개변수로 request와 response가 항상 존재 했었음
	//struts2에서는 기본적으로 매개변수를 입력하지 않으므로 메소드의 코딩이 단순해짐
	//하지만 사용할때마다 요청을 해야함
	public String created() throws Exception {
		
		//==와 .equals()의 순서가 바뀌면 오류남. 주의!
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")){
			return INPUT; //내장변수 String com.opensymphony.xwork2.Action.INPUT = "input"
		}
		//세션을 만드는 것은 request. 하지만 매개변수로 입력하지 않았으므로 생성해주어야 한다
		//ServletActionContext클래스에 request를 요청해서 받음(Context는 전체를 의미!)
		HttpServletRequest request = ServletActionContext.getRequest(); 
		request.setAttribute("message", "스트럿츠2.."); 
		//request.setAttribute("dto", dto);//로 데이터를 넘겼었는데 
		//struts2는 자동으로 getDto 메소드를 통해 전달
		
		return SUCCESS;
	}

}
