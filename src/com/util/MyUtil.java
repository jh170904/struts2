package com.util;

public class MyUtil {

	//��ü������ �� ���ϱ�
	//numPerPage : �� ȭ�鿡 ǥ���� ������ ����
	//dataCount : ��ü ������ ����
								
	public int getPageCount(int numPerPage, int dataCount){
		
		int pageCount = 0;
		
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage != 0)
			pageCount++;
		
		return pageCount;	
	}
	
	//����¡ ó��
	//currentpage : ���� ǥ���� ������
	//totalPage : ��ü ������ ��
	//listUrl : ��ũ�� ������ URL
	
	public String pageIndexList(int currentPage, int totalPage, String listUrl){
		
		int numPerBlock = 3; //ȭ��ǥ�ȿ� �ִ� �� < 1 2 3 4 > 
		int currentPageSetup; // ȭ��ǥ�� ������ �ִ� ����1 < 2 3 4 5 >
		int page;
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage==0 ||totalPage==0){
			return "";
		}
		
		//list.jsp?pageNum=9 =>�˻����� / �ؿ��ڵ�����? & �ǹ�
		//list.jsp?searchKey=name&searchValue=suzi&pageNum=9 =>�˻��� �����ʹ� ������ �ٳ���� ����ڰ� �˻��� ���Ҷ�����
		if(listUrl.indexOf("?")!=-1){ //����ǥ�� ������
			listUrl = listUrl + "&";
			//list.jsp?searchKey=name&searchValue=suzi&
		}else{
			listUrl = listUrl + "?";
			//list.jsp?
		}
		
		//ǥ���� ù���������� -1 �� ��(������ ����ִ� ��)
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock; //< 6 5�����;��� 5�� ���� ���ϴ� ����
		
		if(currentPage % numPerBlock == 0) //10�������� ���ð�� ���� ���� �������� 0�� ��쿡
			currentPageSetup = currentPageSetup - numPerBlock; //10-5 ù 5�� ����Ǿ�������
		
		
		//�� ���� 1~5������ �ʿ����
		if(totalPage>numPerBlock && currentPageSetup > 0){
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup +
					"\">������</a>&nbsp;");
			
			//<a href="list.jsp?pagenum=5">������</a>&nbsp; �������ô� ���ڶ�� �˷���
		}
		
		//�ٷΰ��� ������
		page = currentPageSetup + 1; //�� +1 �̶�� �� �� ������ 
		
		while(page<=totalPage && page<= (currentPageSetup + numPerBlock)){ // �����̿� �ִ� ����(page) ��(���̿��ִ¼��� ������������ �۰� ������ ������ ����������
			
			if(page == currentPage){
				
				sb.append("<font color=\"Fuchsia\">" + page + "</font>&nbsp;"); //������������ ������ ä������
			}else{
				sb.append("<a href=\"" + listUrl + "pageNum=" + page +  //�ƴϸ� ��� ����
						"\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=10">10</a>&nbsp;
			}
			
			page++;
			
		}
		// ������
		if(totalPage - currentPageSetup > numPerBlock){
			
			sb.append("<a href=\"" + listUrl + "pageNum=" + page +
					"\">������</a>&nbsp;");
			//<a href ="list.jsp?pageNum=11">������</a>&nbsp;

		}
		
		return sb.toString();
	}
	
	
}
