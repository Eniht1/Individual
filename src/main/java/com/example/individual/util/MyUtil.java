package com.example.individual.util;

import org.springframework.stereotype.Service;

@Service
public class MyUtil {

	public int getPageCount(int numPerPage, int dataCount) {

		int pageCount = 0;
		pageCount = dataCount / numPerPage;

		if (dataCount % numPerPage != 0) {
			pageCount++;
		}

		return pageCount;
	}

	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		StringBuffer sb = new StringBuffer(); // 메모리 낭비 방지를 위해 사용
		int numPerBlock = 5; // 이전과 다음 사이의 숫자를 몇개 표기할지 ◀이전 6 7 8 9 10 다음▶
		int currentPageSetup; // ◀이전 버튼에 들어갈 값
		int page; // 그냥 페이지 숫자를 클릭했을때 들어갈 값

		if (currentPage == 0 || totalPage == 0)
			return ""; // 데이터가 없다.

		// 검색어가 있을때 : /list?searchKrey=name&searchValue=춘식
		if (listUrl.indexOf("?") != -1) {
			// '?'가 들어있다면(쿼리스트링이 있다면)
			listUrl += "&";
		} else { // 쿼리스트링이 없으면
			listUrl += "?";
		}

		// 1. ◀이전 버튼 만들기

		// currentPage가 5 10 15 .. 일때 currentPageSetup = 5 10 15 ..
		currentPageSetup = (currentPage / numPerBlock) * numPerBlock;

		if (currentPage % numPerBlock == 0) {
			currentPageSetup = currentPageSetup - numPerBlock;
		}

		if (totalPage > numPerBlock && currentPageSetup > 0) {
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup + "\">◀이전</a>&nbsp;");
		}

		// 2. 그냥 페이지(6 7 8 9 10) 이동 버튼 만들기

		page = currentPageSetup + 1; // page 1 6 11 16 ...
		while (page <= totalPage && page <= (currentPageSetup + numPerBlock)) {
			if (page == currentPage) {
				// 현재 선택한 페이지라면
				sb.append("<font color=\"red\">" + page + "</font>&nbsp;");
			} else {
				// 현재 선택한 페이지가 아니라면
				sb.append("<a href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>&nbsp;");
			}

			page++;
		}

		// 3. 다음▶ 버튼 만들기
		if (totalPage - currentPageSetup > numPerBlock) {
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup + "\">다음▶</a>&nbsp;");
		}

		// 4. 버튼 합쳐서 문자열로 리턴
		System.out.println(sb.toString());

		return sb.toString();
	}
}
