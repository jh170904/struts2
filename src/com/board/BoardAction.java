package com.board;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class BoardAction extends ActionSupport 
	implements Preparable,ModelDriven<BoardDTO>{

	private static final long serialVersionUID = 1L;	
	private BoardDTO dto;
	//getter만 생성
	public BoardDTO getDto() {
		return dto;
	}
	@Override
	public BoardDTO getModel() {
		return dto;
	}
	@Override
	public void prepare() throws Exception {
		dto = new BoardDTO();
	}
	
	public String created() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String searchKey = dto.getSearchKey();
		String searchValue = dto.getSearchValue();
		String pageNum = dto.getPageNum();
		
		//검색값 없을 경우
		if(searchValue==null||searchValue.equals("")){
			searchKey = "subject";
			searchValue = "";
		}
		
		//검색값 있을 경우 한글디코딩
		if(request.getMethod().equalsIgnoreCase("GET")){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		String params = "pageNum="+pageNum;
		if(!searchValue.equals("")){
			params += "&searchKey=" + searchKey; 
			//한글 보내기 위해 인코딩
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		request.setAttribute("params", params);
		request.setAttribute("searchKey", searchKey);
		request.setAttribute("searchValue", searchValue);
		
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")){
			//게시물 작성창 띄우기
			request.setAttribute("mode", "created");
			return INPUT;
		}
		//게시물 저장
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		int maxBoardNum = dao.getIntValue("board.maxBoardNum");
		
		dto.setBoardNum(maxBoardNum+1);
		dto.setIpAddr(request.getRemoteAddr());
		//GroupNum에는 자신의 BoardNum이 들어간다
		dto.setGroupNum(dto.getBoardNum());
		dto.setDepth(0);
		dto.setOrderNo(0);
		dto.setParent(0);
		//DB입력
		dao.insertData("board.insertData", dto);
		return SUCCESS;
		
	}
	
	public String list() throws Exception {
		//게시물 리스트 조회
		CommonDAO dao = CommonDAOImpl.getInstance();
		MyUtil myUtil = new MyUtil();
		HttpServletRequest request = ServletActionContext.getRequest();
		String cp = request.getContextPath();
		
		int numPerPage = 5;
		int totalPage = 0;
		int totalDataCount = 0;
		int currentPage = 1;
		
		if(dto.getPageNum()!=null&&!dto.getPageNum().equals("")){
			currentPage = Integer.parseInt(dto.getPageNum());
		}
		
		if(dto.getSearchValue()==null||dto.getSearchValue().equals("")){
			dto.setSearchKey("subject");
			dto.setSearchValue("");
		}
		
		if(request.getMethod().equalsIgnoreCase("GET")){
			dto.setSearchValue(URLDecoder.decode(dto.getSearchValue(),"UTF-8"));
		}
		
		Map<String, Object> hMap = new HashMap<String, Object>();
		
		hMap.put("searchKey",dto.getSearchKey());
		hMap.put("searchValue",dto.getSearchValue());
		
		totalDataCount = dao.getIntValue("board.dataCount",hMap);
		if(totalDataCount!=0){
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}
		
		if(currentPage>totalPage)
			currentPage = totalPage;
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		List<Object> lists = (List<Object>)dao.getListData("board.listData", hMap);
		
		int listNum,n=0;
		ListIterator<Object> it = lists.listIterator();
		while(it.hasNext()){
			BoardDTO vo = (BoardDTO)it.next();
			listNum = totalDataCount-(start+n-1);
			vo.setListNum(listNum);
			n++;
		}	
		String param = "";
		String urlList = "";
		String urlArticle = "";
		String params="";
		if(!dto.getSearchValue().equals("")){
			param = "searchKey="+dto.getSearchKey();
			param += "&searchValue="+URLEncoder.encode(dto.getSearchValue(),"UTF-8");
		}
		urlList = cp + "/bbs/list.action";
		urlArticle = cp + "/bbs/article.action?pageNum="+currentPage;
		if(!param.equals("")){
			//검색한경우
			urlList += "?" + param;
			urlArticle += "&" + param;
			params = "pageNum="+currentPage+"&"+param; 
		}
		
		request.setAttribute("lists",lists);
		request.setAttribute("params",params);
		request.setAttribute("totalDataCount",totalDataCount);
		request.setAttribute("pageIndexList",myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("urlArticle",urlArticle);
		
		return SUCCESS;
	}	
	
	public String article() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		//dto의 get로 만들경우 readData할 때 dto가 초기화되므로 매개변수 값 받고 진행 
		String searchKey = dto.getSearchKey();
		String searchValue = dto.getSearchValue();
		String pageNum = dto.getPageNum();
		int boardNum =dto.getBoardNum();
		
		//검색값 없을 경우
		if(searchValue==null||searchValue.equals("")){
			searchKey = "subject";
			searchValue = "";
		}
		
		//검색값 있을 경우 한글디코딩
		if(request.getMethod().equalsIgnoreCase("GET")){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		//조회수 증가	
		dao.updateData("board.updateHitCount",boardNum);
		
		//게시글 읽어오기
		dto = (BoardDTO)dao.getReadData("board.readData", boardNum);
		
		if(dto==null)
			return "read-error";
		
		//줄바꿈
		int lineSu = dto.getContent().split("\n").length;
		dto.setContent(dto.getContent().replaceAll("\n", "<br/>"));
		
		//이전글 다음글
		Map<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		hMap.put("groupNum", dto.getGroupNum());
		hMap.put("orderNo", dto.getOrderNo());
		
		//이전글 데이터
		BoardDTO preDTO = (BoardDTO)dao.getReadData("board.preReadData",hMap);
		int preBoardNum = 0;
		String preSubject = "";
		if(preDTO!=null){
			preBoardNum = preDTO.getBoardNum();
			preSubject = preDTO.getSubject();
		}
		
		//다음글 데이터
		BoardDTO nextDTO = (BoardDTO)dao.getReadData("board.nextReadData",hMap);
		int nextBoardNum = 0;
		String nextSubject = "";
		if(nextDTO!=null){
			nextBoardNum = nextDTO.getBoardNum();
			nextSubject = nextDTO.getSubject();
		}
		
		String params = "pageNum=" + pageNum ;
		if(!searchValue.equals("")){
			params += "&searchKey=" + searchKey; 
			//한글 보내기 위해 인코딩
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		//이전글
		request.setAttribute("preBoardNum", preBoardNum);
		request.setAttribute("preSubject", preSubject);
		//다음글
		request.setAttribute("nextBoardNum", nextBoardNum);
		request.setAttribute("nextSubject", nextSubject);
		//페이징
		request.setAttribute("params", params);
		request.setAttribute("lineSu",lineSu);
		request.setAttribute("pageNum",pageNum);
		
		return SUCCESS;
	}
	
	public String updated() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		String pageNum = dto.getPageNum();
		
		//검색값 없을 경우
		if(searchValue==null||searchValue.equals("")){
			searchKey = "subject";
			searchValue = "";
		}
		
		//검색값 있을 경우 한글디코딩
		if(request.getMethod().equalsIgnoreCase("GET")){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		String params = "pageNum="+pageNum;
		
		if(!searchValue.equals("")){
			params += "&searchKey=" + searchKey; 
			//한글 보내기 위해 인코딩
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		request.setAttribute("params", params);
		
		if(dto.getMode()==null||dto.getMode().equals("")){
			dto = (BoardDTO)dao.getReadData("board.readData", dto.getBoardNum());
			if(dto==null){
				return "read-error";
			}
			request.setAttribute("mode", "updated");
			request.setAttribute("pageNum", dto.getPageNum());
			return INPUT;
		}
		dao.updateData("board.updateData", dto);
		request.setAttribute("searchKey", searchKey);
		request.setAttribute("searchValue", searchValue);
		return SUCCESS;
	}
	
	public String reply() throws Exception {	
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		String searchKey = dto.getSearchKey();
		String searchValue = dto.getSearchValue();
		String pageNum = dto.getPageNum();
		
		//검색값 없을 경우
		if(searchValue==null||searchValue.equals("")){
			searchKey = "subject";
			searchValue = "";
		}
		
		//검색값 있을 경우 한글디코딩
		if(request.getMethod().equalsIgnoreCase("GET")){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		String params = "pageNum="+pageNum;
		if(!searchValue.equals("")){
			params += "&searchKey=" + searchKey; 
			//한글 보내기 위해 인코딩
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		request.setAttribute("params", params);
		request.setAttribute("searchKey", searchKey);
		request.setAttribute("searchValue", searchValue);
		
		//답변창
		if(dto==null||dto.getMode()==null||dto.getMode().equals("")){
			dto = (BoardDTO)dao.getReadData("board.readData", dto.getBoardNum());
			
			if(dto==null)
				return "read-error";
			
			//엔터
			String temp = "\r\n\r\n--------------------------------------------\r\n\r\n" ;
			temp += "[답변]\r\n";
			
			//원문이 위에 오고 답변이 기재되도록
			dto.setSubject("[답변]" + dto.getSubject());
			dto.setContent(dto.getContent() +  temp);
			dto.setName("");
			dto.setEmail("");
			dto.setPwd("");
			
			request.setAttribute("mode", "reply");
			request.setAttribute("pageNum", dto.getPageNum());
			return INPUT;
		}
		
		//답변작성
		//orderNo 변경
		Map<String,Object> hMap = new HashMap<String, Object>();
		hMap.put("groupNum", dto.getGroupNum());
		hMap.put("orderNo",dto.getOrderNo());
		//SQL문 실행
		dao.updateData("board.orderNoUpdate", hMap);
		
		//답변입력
		int maxBoardNum = dao.getIntValue("board.maxBoardNum");
		dto.setBoardNum(maxBoardNum+1);
		dto.setIpAddr(request.getRemoteAddr());
		
		//답변이 밑에 달리도록 
		dto.setDepth(dto.getDepth()+1);
		dto.setOrderNo(dto.getOrderNo()+1);
		
		dao.insertData("board.insertData", dto);	
		return SUCCESS;
	}
	
	public String deleted() throws Exception {
		CommonDAO dao = CommonDAOImpl.getInstance();
		dao.deleteData("board.deleteData",dto.getBoardNum());
		return SUCCESS;
	}
		
}
