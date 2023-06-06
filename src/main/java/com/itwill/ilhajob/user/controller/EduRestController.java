package com.itwill.ilhajob.user.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.ilhajob.user.dto.EduDto;
import com.itwill.ilhajob.user.dto.UserDto;
import com.itwill.ilhajob.user.service.EduService;
import com.itwill.ilhajob.user.service.UserService;

@Controller
public class EduRestController {

	@Autowired
	private EduService eduService;
	@Autowired
	private UserService userService;

	@PostMapping(value = "/edu")
	public String createEdu(@RequestParam(name = "eduStartDate") String eduStartDate,
			@RequestParam(name = "eduEndDate") String eduEndDate, @RequestParam(name = "eduName") String eduName,
			@RequestParam(name = "eduMajor") String eduMajor, @RequestParam(name = "eduScore") String eduScore,
			@RequestParam(name = "eduContent") String eduContent,
			HttpServletRequest request, Model model) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userEmail = (String) request.getSession().getAttribute("sUserId");
			UserDto user = userService.findUser(userEmail);
			EduDto eduDto = new EduDto();

			eduDto.setEduName(eduName);
			eduDto.setEduContent(eduContent);
			eduDto.setEduMajor(eduMajor);
			eduDto.setEduScore(eduScore);
			eduDto.setUser(user);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			System.out.println(">>>>>>>>>>>>>>>>>> eduStartDate : " + eduStartDate);
			System.out.println(">>>>>>>>>>>>>>>>>> eduEndDate : " + eduEndDate);
			LocalDateTime startDateTime = LocalDate.parse(eduStartDate, formatter).atStartOfDay();
			LocalDateTime endDateTime = LocalDate.parse(eduEndDate, formatter).atStartOfDay();

			eduDto.setEduStartDate(startDateTime);
			eduDto.setEduEndDate(endDateTime);
			EduDto thisEdu = eduService.createEdu(eduDto);

			System.out.println(">>>>>>>>>>>>>>>> createEdu : " + eduDto);
			eduService.updateEdu(thisEdu.getId(), eduDto);
			System.out.println(">>>>>>>>>>>>>>>> updateEdu : " + eduDto);
			List<EduDto> eduList = eduService.findEduListByUserId(user.getId());
			resultMap.put("eduList", eduList);
			model.addAttribute("eduList", eduList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/candidate-dashboard-resume :: #edu-block";
	}
	
	@DeleteMapping(value = "/edu/{eduId}")
	public String deleteEdu(HttpServletRequest request, @PathVariable(value = "eduId") String eduId,
			RedirectAttributes redirectAttributes, Model model) {

		Map<String, Object> resultMap = new HashMap<>();
		Long longEduId = Long.parseLong(eduId.replace(',', ' ').trim());
		eduService.deleteEdu(longEduId);
		Long userId = (Long) request.getSession().getAttribute("id");

		int code = 1;
		List<EduDto> eduList = eduService.findEduListByUserId(userId);

		resultMap.put("code", code);
		resultMap.put("eduList", eduList);
		
		model.addAttribute("eduList", eduList);

		return "/candidate-dashboard-resume :: #edu-block";
	}

	@PutMapping(value = "edu-update")
	public String updateEdu(@RequestParam(name = "eduId") String eduId,
			@RequestParam(name = "eduStartDate") List<String> eduStartDateList,
			@RequestParam(name = "eduEndDate") List<String> eduEndDateList,
			@RequestParam(name = "eduName") List<String> eduNameList,
			@RequestParam(name = "eduMajor") List<String> eduMajorList,
			@RequestParam(name = "eduScore") List<String> eduScoreList,
			@RequestParam(name = "eduContent") List<String> eduContentList,
			HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		try {
			String userEmail = (String) request.getSession().getAttribute("sUserId");
			UserDto user = userService.findUser(userEmail);

			long longeduId = Long.parseLong(eduId.replace(",", " ").trim());

			EduDto thisEdu = eduService.findById(longeduId);

			String eduName = eduNameList.get(eduNameList.size() - 1);
			String eduMajor = eduMajorList.get(eduMajorList.size() - 1);
			String eduStartDate = eduStartDateList.get(eduStartDateList.size() - 1);
			String eduEndDate = eduEndDateList.get(eduEndDateList.size() - 1);
			String eduScore = eduScoreList.get(eduScoreList.size() - 1);
			String eduContent = eduContentList.get(eduContentList.size() - 1);

			thisEdu.setEduName(eduName);
			thisEdu.setEduContent(eduContent);
			thisEdu.setEduMajor(eduMajor);
			thisEdu.setEduScore(eduScore);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime startDateTime = LocalDate.parse(eduStartDate, formatter).atStartOfDay();
			LocalDateTime endDateTime = LocalDate.parse(eduEndDate, formatter).atStartOfDay();

			thisEdu.setEduStartDate(startDateTime);
			thisEdu.setEduEndDate(endDateTime);
			eduService.updateEdu(thisEdu.getId(), thisEdu);
			List<EduDto> eduList = eduService.findEduListByUserId(user.getId());
			model.addAttribute("eduList", eduList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/candidate-dashboard-resume :: #edu-block";
	}

}
