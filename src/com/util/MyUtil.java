package com.util;

public class MyUtil {

	//전체페이지 수 구하기
	//numPerPage : 한 화면에 표시할 데이터 갯수
	//dataCount : 전체 데이터 갯수
								
	public int getPageCount(int numPerPage, int dataCount){
		
		int pageCount = 0;
		
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage != 0)
			pageCount++;
		
		return pageCount;	
	}
	
	//페이징 처리
	//currentpage : 현재 표시할 페이지
	//totalPage : 전체 페이지 수
	//listUrl : 링크를 설정한 URL
	
	public String pageIndexList(int currentPage, int totalPage, String listUrl){
		
		int numPerBlock = 3; //화살표안에 있는 수 < 1 2 3 4 > 
		int currentPageSetup; // 화살표가 가지고 있는 숫자1 < 2 3 4 5 >
		int page;
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage==0 ||totalPage==0){
			return "";
		}
		
		//list.jsp?pageNum=9 =>검색안함 / 밑에코딩들은? & 의미
		//list.jsp?searchKey=name&searchValue=suzi&pageNum=9 =>검색된 데이터는 가지고 다녀야함 사용자가 검색을 안할때까지
		if(listUrl.indexOf("?")!=-1){ //물음표가 있으면
			listUrl = listUrl + "&";
			//list.jsp?searchKey=name&searchValue=suzi&
		}else{
			listUrl = listUrl + "?";
			//list.jsp?
		}
		
		//표시할 첫페이지에서 -1 한 값(이전에 담겨있는 값)
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock; //< 6 5가나와야함 5의 값을 구하는 공식
		
		if(currentPage % numPerBlock == 0) //10페이지가 나올경우 위에 값이 나머지가 0일 경우에
			currentPageSetup = currentPageSetup - numPerBlock; //10-5 첫 5가 저장되어있을것
		
		
		//◀ 이전 1~5까지는 필요없음
		if(totalPage>numPerBlock && currentPageSetup > 0){
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup +
					"\">◀이전</a>&nbsp;");
			
			//<a href="list.jsp?pagenum=5">◀이전</a>&nbsp; 역슬러시는 문자라고 알려줌
		}
		
		//바로가기 페이지
		page = currentPageSetup + 1; //◀ +1 이라는 뜻 ◀ 다음값 
		
		while(page<=totalPage && page<= (currentPageSetup + numPerBlock)){ // ◀사이에 있는 수들(page) ▶(사이에있는수가 총페이지보다 작고 마지막 수보다 작을때까지
			
			if(page == currentPage){
				
				sb.append("<font color=\"Fuchsia\">" + page + "</font>&nbsp;"); //현재페이지는 색으로 채워지고
			}else{
				sb.append("<a href=\"" + listUrl + "pageNum=" + page +  //아니면 계속 찍어라
						"\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=10">10</a>&nbsp;
			}
			
			page++;
			
		}
		// ▶다음
		if(totalPage - currentPageSetup > numPerBlock){
			
			sb.append("<a href=\"" + listUrl + "pageNum=" + page +
					"\">▶다음</a>&nbsp;");
			//<a href ="list.jsp?pageNum=11">▶다음</a>&nbsp;

		}
		
		return sb.toString();
	}
	
	
}
