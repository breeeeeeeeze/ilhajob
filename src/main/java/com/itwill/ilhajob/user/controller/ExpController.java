package com.itwill.ilhajob.user.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.ilhajob.user.dto.ExpDto;
import com.itwill.ilhajob.user.dto.UserDto;
import com.itwill.ilhajob.user.service.ExpService;
import com.itwill.ilhajob.user.service.UserService;

@Controller
public class ExpController {
	
	@Autowired
	private ExpService expService;
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/exp")
	public String createExp(
			HttpServletRequest request, 
			@RequestParam(name="expCorpName") String expCorpName,
			@RequestParam(name="expPosition") String expPosition,
			@RequestParam(name="expContent") String expContent,
			@RequestParam(name="expStartDate") String expStartDate,
			@RequestParam(name="expEndDate") String expEndDate,
			Model model
			) throws Exception {
		
		String userId = (String)request.getSession().getAttribute("sUserId");
		UserDto user = userService.findUser(userId);
		
		ExpDto expDto = new ExpDto();
		
		expCorpName = expCorpName.replace(",", "");
		expPosition = expPosition.replace(",", "");
		expContent = expContent.replace(",", "");
		expStartDate = expStartDate.replace(",", "");
		expEndDate = expEndDate.replace(",", "");
		
		expDto.setExpCorpName(expCorpName);
		expDto.setExpPosition(expPosition);
		expDto.setExpContent(expContent);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startDateTime = LocalDate.parse(expStartDate, formatter).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(expEndDate, formatter).atStartOfDay();
		expDto.setExpStartDate(startDateTime);
		expDto.setExpEndDate(endDateTime);
		expDto.setUser(user);
		
		expService.createExp(expDto);
		
		List<ExpDto> expList = expService.findExpListByUserId(user.getId());
		model.addAttribute("expList", expList);
		
		return "/candidate-dashboard-resume :: #exp-block";
	}
	
	@PutMapping(value = "/exp/{expId}")
	public String updateExp(
			@RequestParam(name="expId") String id,
			@RequestParam(name="expCorpName") String expCorpName,
			@RequestParam(name="expPosition") String expPosition,
			@RequestParam(name="expContent") String expContent,
			@RequestParam(name="expStartDate") String expStartDate,
			@RequestParam(name="expEndDate") String expEndDate,
			HttpServletRequest request, Model model) throws Exception{
		ExpDto expDto = new ExpDto();
		
		Long expId = Long.parseLong(id.replaceAll(",", ""));
		
		String[] beforeReplace = {expCorpName, expPosition, expContent, expStartDate, expEndDate};
		String[] afterReplace = new String[beforeReplace.length];
		for (int i = 0; i < beforeReplace.length; i++) {
			afterReplace[i] = beforeReplace[i].replace(",", "");
		}
		
		expStartDate = afterReplace[3];
		expEndDate = afterReplace[4];
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startDateTime = LocalDate.parse(expStartDate, formatter).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(expEndDate, formatter).atStartOfDay();
		
		expDto.setExpCorpName(afterReplace[0]);
		expDto.setExpPosition(afterReplace[1]);
		expDto.setExpContent(afterReplace[2]);
		expDto.setExpStartDate(startDateTime);
		expDto.setExpEndDate(endDateTime);
		expService.updateExp(expId, expDto);
		
		String userId = (String)request.getSession().getAttribute("sUserId");
		UserDto user = userService.findUser(userId);
		List<ExpDto> expList = expService.findExpListByUserId(user.getId());
		model.addAttribute("expList", expList);
		
		return "candidate-dashboard-resume :: #exp-block";
	}
	
	@DeleteMapping(value = "/exp/{expId}")
	public String deleteExp(@RequestParam(name = "expId") String id, HttpServletRequest request, Model model) throws Exception {
		Long expId = Long.parseLong(id.replace(",", ""));
		expService.removeById(expId);
		
		String userId = (String)request.getSession().getAttribute("sUserId");
		UserDto user = userService.findUser(userId);
		List<ExpDto> expList = expService.findExpListByUserId(user.getId());
		model.addAttribute("expList", expList);
		return "candidate-dashboard-resume :: #exp-block";
	}
}
