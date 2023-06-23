package com.example.individual.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.individual.dto.Individual;
import com.example.individual.service.IndividualService;
import com.example.individual.util.MyUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndividualController {
	@Autowired
	private IndividualService individualService;
	
	@Autowired
	MyUtil myUtil;

	@RequestMapping(value = "/")
	public String index() {
		return "/index";
	}

	@RequestMapping(value = "/created", method = RequestMethod.GET)
	public String created() {
		return "bbs/created";
	}

	@RequestMapping(value = "/created", method = RequestMethod.POST)
	public String createOK(Individual individual, HttpServletRequest request, Model model) {

		try {
			int maxNum = individualService.maxNum();

			individual.setNum(maxNum + 1);

			individualService.insertData(individual);

		} catch (Exception e) {
			e.printStackTrace();
			return "bbs/created";
		}
		return "redirect:/list";
	}
	
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(Individual individual, HttpServletRequest request, Model model)  {
		
		try {
			String pageNum = request.getParameter("pageNum"); //바뀌는 페이지 번호
			int currentPage = 1; //현재 페이지 번호(default = 1)
			
			if(pageNum != null) currentPage = Integer.parseInt(pageNum);
			
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			
			if(searchValue == null) {
				searchKey = "subject"; //검색 키워드의 default = subject
				searchValue = ""; //검색어의 default = ""(빈문자열)
			} else {
				if(request.getMethod().equalsIgnoreCase("GET")) {
					//get방식으로 request가 왔다면
					//쿼리 파라메터의 값(searchValue)을 디코딩해준다.
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
				}
			}
			
			//1. 전체 게시물의 개수를 가져온다.(페이징 처리에 필요)
			int dataCount = individualService.getDataCount(searchKey, searchValue);
			
			//2. 페이징 처리를 한다.(준비단계)
			int numPerPage = 5; //페이지당 보여줄 데이터의 개수
			int totalPage = myUtil.getPageCount(numPerPage, dataCount); //페이지의 전체 개수를 보여준다.
			
			if(currentPage > totalPage) currentPage = totalPage; //totalPage보다 크면 안된다.
			
			int start = (currentPage - 1) * numPerPage + 1; // 1 6 11 16 ....
			int end = currentPage * numPerPage; // 5 10 15 20 ....
			
			//3. 전체 게시물 리스트를 가져온다.
			List<Individual> lists = individualService.getLists(searchKey, searchValue, start, end);
			
			//4. 페이징 처리를 한다.
			String param = "";	
			
			if(searchValue != null && !searchValue.equals("")) {
				//검색어가 있다면
				param = "searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			
			String listUrl = "/list";
			
			// /list?searchKrey=name&searchValue=춘식
			if (!param.equals("")) listUrl += "?" + param;
			
			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
			
			String articleUrl = "/article?pageNum=" + currentPage;
			
			if(!param.equals("")) {
				articleUrl += "&" + param;
				// /article?pageNum=1&searchKey=subject&searchValue=춘식
			}
			
			model.addAttribute("lists", lists);
			model.addAttribute("articleUrl", articleUrl);
			model.addAttribute("pageIndexList", pageIndexList);
			model.addAttribute("dataCount", dataCount);
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "리스트를 불러오는 중 에러가 발생했습니다.");
		}
		

		
		return "bbs/list";
	}
	
	@RequestMapping(value = "/article", method = RequestMethod.GET)
	public String article(HttpServletRequest request, Model model)  {
		try {
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			
			if(searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
			//1. 조회수 늘리기
			individualService.updateHitCount(num);
			
			//2. 게시물 데이터 가져오기
			Individual individual = individualService.getReadData(num);
			
			if(individual == null) {
				return "redirect:/list?pageNum=" + pageNum;
			}
			
			//게시글의 라인수 구하기
			int lineSu = individual.getContent().split("\n").length;
			
			String param = "pageNum=" + pageNum;
			
			if(searchValue != null && !searchValue.equals("")) {
				//검색어가 있다면
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			
			model.addAttribute("individual", individual);
			model.addAttribute("params", param);
			model.addAttribute("lineSu", lineSu);
			model.addAttribute("pageNum", pageNum);
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글을 불러오는 중 에러가 발생했습니다.");
		}
		
		return "bbs/article";
	}

	@RequestMapping(value = "/updated", method = RequestMethod.GET)
	public String updated(HttpServletRequest request, Model model) {
		try {
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");

			if (searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}

			Individual individual = individualService.getReadData(num);

			if (individual == null) {
				return "redirect:/list?pageNum=" + pageNum;
			}

			String param = "pageNum" + pageNum;

			if (searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}

			model.addAttribute("individual", individual);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("params", param);
			model.addAttribute("searchKey", searchKey);
			model.addAttribute("searchValue", searchValue);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "bbs/updated";
	}
	
	@RequestMapping(value = "/updated_ok", method = RequestMethod.POST)
	public String updatedOK(Individual individual, HttpServletRequest request, Model model){
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		String param = "?pageNum=" + pageNum;
		
		try {
			individual.setContent(individual.getContent().replaceAll("<br/>", "\r\n"));
			individualService.updateData(individual);
			
			if(searchValue !=null && !searchValue.equals("")) {
				//검색어가 있다면
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				param += "&errorMessage=" + URLEncoder.encode("게시글 수정중 에러가 발생했습니다", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		
		return "redirect:/list" + param;
	}
	
	@RequestMapping(value="/deleted_ok", method= {RequestMethod.GET})
	public String deleteOK(HttpServletRequest request, Model model) {
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		String param = "?pageNum=" + pageNum;
		
		try {
			individualService.deleteData(num);
			
			if(searchValue != null && !searchValue.equals("")) {
				//검색어가 있다면
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				param += "&errorMessage=" + URLEncoder.encode("게시글 삭제중 에러가 발생했습니다", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		
		return "redirect:/list" + param;
	}
}
