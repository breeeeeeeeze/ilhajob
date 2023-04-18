package com.itwill.ilhajob.user.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.ilhajob.user.dto.AwardsDto;
import com.itwill.ilhajob.user.dto.CvDto;
import com.itwill.ilhajob.user.dto.EduDto;
import com.itwill.ilhajob.user.dto.ExpDto;
import com.itwill.ilhajob.user.dto.UserDto;
import com.itwill.ilhajob.user.service.AwardsService;
import com.itwill.ilhajob.user.service.CvService;
import com.itwill.ilhajob.user.service.EduService;
import com.itwill.ilhajob.user.service.ExpService;
import com.itwill.ilhajob.user.service.UserService;

@Controller
public class EduController {
	
	@Autowired
	private EduService eduService;
	@Autowired
	private CvService cvService;
	@Autowired
	private UserService userService;
	@Autowired
	private AwardsService awardsService;
	@Autowired
	private ExpService expService;
	

	@RequestMapping(value = "/edu-create", method = RequestMethod.POST)
	public String createEdu(
			@RequestParam(name = "eduStartDate") String eduStartDate, 
			@RequestParam(name = "eduEndDate") String eduEndDate, 
			@RequestParam(name = "eduName") List<String> eduNameList,
			@RequestParam(name = "eduMajor") List<String> eduMajorList,
			@RequestParam(name = "eduScore") String eduScore,
			@RequestParam(name = "eduContent") String eduContent,
			@RequestParam(name="id") Long cvId,
			HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			String userEmail = (String)request.getSession().getAttribute("sUserId");
			UserDto user = userService.findUser(userEmail);
			
			EduDto eduDto = new EduDto();
			
			String eduName = eduNameList.get(eduNameList.size()-1);
			String eduMajor = eduMajorList.get(eduMajorList.size()-1);
			
			eduDto.setEduName("");
			eduDto.setEduName(eduName);
			eduDto.setEduContent(eduContent);
			eduDto.setEduMajor(eduMajor);
			eduDto.setEduScore(eduScore);			
			eduDto.setUser(user);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime startDateTime = LocalDate.parse(eduStartDate, formatter).atStartOfDay();
			LocalDateTime endDateTime = LocalDate.parse(eduEndDate, formatter).atStartOfDay();
			
			eduDto.setEduStartDate(startDateTime);
			eduDto.setEduEndDate(endDateTime);
			eduService.createEdu(eduDto);

			redirectAttributes.addAttribute("cvId", cvId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:cv-detail";
	}
	
	@RequestMapping(value = "/edu", produces = "application/json;charset=UTF-8")
	public Map<String, Object> addEdu(@RequestBody EduDto eduDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		EduDto data = eduService.createEdu(eduDto);
		resultMap.put("data", data);
		return resultMap;
	}
	
	@RequestMapping(value = "edu-delete-action", method = RequestMethod.POST)
	public String deleteEdu(HttpServletRequest request, @RequestParam("eduId") String eduId, @RequestParam(name="id") Long cvId, Model model, RedirectAttributes redirectAttributes) {
		System.out.println(">>>>>>>>>>>>>>> eduId " + eduId);
		System.out.println(eduId.replace(',', ' ').trim());
		System.out.println(">>>>>>>>>>>>>>> String -> Long eduId : " + Long.parseLong(eduId.replace(',', ' ').trim()));
		Long longEduId = Long.parseLong(eduId.replace(',', ' ').trim());
		eduService.deleteEdu(longEduId);
		redirectAttributes.addAttribute("cvId", cvId);
		return "redirect:cv-detail";
	}
	
	
	
	
	
	
	// ajax
	
	@RequestMapping(value="/edu/delete/{eduId}", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> deleteEdu(@PathVariable("eduId") Long eduId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		System.out.println(">>>>>>>>>> eduId : " + eduId);
//		System.out.println(eduid.replace(',', ' ').trim());
//		eduService.deleteEduByEduSeq(Integer.parseInt(eduid.replace(',', ' ').trim()));
		int code = 1;
		String msg = "success";
		List<EduDto> eduData = new ArrayList<EduDto>();
		try {
			eduService.deleteEdu(eduId);
			Long userId = (Long)request.getSession().getAttribute("id");
			eduData = eduService.findEduListByUserId(userId);
			
			// RedirectAttributes
			Long cvId = cvService.findByUserId(userId).get(0).getId();
			redirectAttributes.addAttribute("cvId", cvId);
			//return new ResponseEntity<>(eduList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			//return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			code = 2;
			msg = "fail";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("eduData", eduData);
		
		return resultMap;
	}
}
