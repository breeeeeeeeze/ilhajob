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

import com.itwill.ilhajob.user.dto.AwardsDto;
import com.itwill.ilhajob.user.dto.UserDto;
import com.itwill.ilhajob.user.service.AwardsService;
import com.itwill.ilhajob.user.service.UserService;

@Controller
public class AwardsController {
	@Autowired
	AwardsService awardsService;
	@Autowired
	UserService userService;

	@PostMapping(value = "/awards")
	public String createAwards(HttpServletRequest request, @RequestParam String awardsName,
			@RequestParam String awardsContent, @RequestParam String awardsDate, Model model) {
		String userEmail = (String) request.getSession().getAttribute("sUserId");
		UserDto user;
		AwardsDto awardsDto = new AwardsDto();
		try {
			user = userService.findUser(userEmail);

			LocalDateTime awardsDateTime = LocalDate.parse(awardsDate).atStartOfDay();

			awardsDto.setAwardsName(awardsName);
			awardsDto.setAwardsContent(awardsContent);
			awardsDto.setAwardsDate(awardsDateTime);
			awardsDto.setUser(user);

			awardsService.createAwards(awardsDto);

			List<AwardsDto> awardsList = awardsService.findAwardsByUserId(user.getId());
			model.addAttribute("awardsList", awardsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/candidate-dashboard-resume :: #awards-block";
	}

	@DeleteMapping(value = "/awards/{awardsId}")
	public String deleteAwards(@RequestParam(name = "awardsId") String id, HttpServletRequest request, Model model)
			throws Exception {
		Long awardsId = Long.parseLong(id.replace(",", ""));
		awardsService.removeAwardsById(awardsId);

		UserDto user = userService.findUser((String) request.getSession().getAttribute("sUserId"));

		List<AwardsDto> awardsList = awardsService.findAwardsByUserId(user.getId());
		model.addAttribute("awardsList", awardsList);

		return "/candidate-dashboard-resume :: #awards-block";
	}

	@PutMapping(value = "/awards/{awardsId}")
	public String updateAwards(HttpServletRequest request, @RequestParam(name = "awardsId") String id,
			@RequestParam String awardsName, @RequestParam String awardsContent, @RequestParam String awardsDate,
			Model model) throws Exception {

		AwardsDto awardsDto = new AwardsDto();

		Long awardsId = Long.parseLong(id);

		LocalDateTime awardsDateTime = LocalDate.parse(awardsDate).atStartOfDay();
		System.out.println("a/warsDatetime : " + awardsDateTime);

		awardsDto.setAwardsName(awardsName);
		awardsDto.setAwardsContent(awardsContent);
		awardsDto.setAwardsDate(awardsDateTime);

		awardsService.updateAwards(awardsId, awardsDto);

		UserDto user = userService.findUser((String) request.getSession().getAttribute("sUserId"));
		List<AwardsDto> awardsList = awardsService.findAwardsByUserId(user.getId());
		model.addAttribute("awardsList", awardsList);
		return "/candidate-dashboard-resume :: #awards-block";
	}
}
