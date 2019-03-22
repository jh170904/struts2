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
		}
		request.setAttribute("lists",lists);
		request.setAttribute("totalDataCount",totalDataCount);
		request.setAttribute("pageIndexList",myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("urlArticle",urlArticle);
		
		return SUCCESS;
	}	
}
