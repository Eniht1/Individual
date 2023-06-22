package com.example.individual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.individual.dto.Individual;
import com.example.individual.service.IndividualService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndividualController {
	@Autowired
	private IndividualService individualService;
	
	
	@RequestMapping(value = "/")
	public String index() {
		return "/index";
	}
	
	@RequestMapping(value="/created", method = RequestMethod.GET)
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
			model.addAttribute("errorMessage", "게시글 작성 중 에러가 발생했습니다.");
			return "bbs/created";
		}
		return "redirect:/list";
	}
}
