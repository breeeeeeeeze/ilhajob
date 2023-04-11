package com.itwill.ilhajob.corp.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.type.LocalDateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwill.ilhajob.common.service.AppService;
import com.itwill.ilhajob.corp.dto.CorpDto;
import com.itwill.ilhajob.corp.exception.CorpNotFoundException;
import com.itwill.ilhajob.corp.service.CorpService;
import com.itwill.ilhajob.user.exception.PasswordMismatchException;


@Controller
public class CorpController {

	@Autowired
	private CorpService corpService;
	@Autowired
	private AppService appService;

//	@RequestMapping("/index")
//	public String main() {
//		String forward_path = "index";
//		return forward_path;
//	}

	@RequestMapping("/corp-list")

	public String corp_list(Model model) throws Exception {
		List<CorpDto> corpList = corpService.findCorpAll();
		model.addAttribute("corpList", corpList);
		String forward_path = "corp-list";

		return forward_path;

	}

	@RequestMapping("corp-detail")
	public String corp_detail_view(@RequestParam("corpLoginId") String corpLoginId,Model model) throws Exception {
		CorpDto corpDto=corpService.findCorp(corpLoginId);
		System.out.println(corpDto);
		model.addAttribute("corp", corpDto);
		return "corp-detail";

	}

//	@RequestMapping("/login")
//	public String login() {
//		String forward_path = "login";
//		return forward_path;
//	}
	@PostMapping("corp_login_action")
	public String corp_login_action(@ModelAttribute("fcorp") CorpDto corpDto, Model model, HttpSession session)
			throws Exception {
		String forwardPath = "";
		try {
			corpService.login(corpDto.getCorpLoginId(), corpDto.getCorpPassword());
			session.setAttribute("sCorpId", corpDto.getCorpLoginId());
			System.out.println(corpDto.getCorpLoginId());
			forwardPath = "redirect:dashboard";
		} catch (CorpNotFoundException e) {
			e.printStackTrace();
			model.addAttribute("msg1", e.getMessage());
			forwardPath = "login";
		} catch (PasswordMismatchException e) {
			e.printStackTrace();
			model.addAttribute("msg2", e.getMessage());
			forwardPath = "login";
		}
		return forwardPath;
	}

	/*****************************************************************************
	 * 대쉬보드
	 ******************************************************************************/

	@RequestMapping("/dashboard")
	public String corp_dashboard_view(HttpServletRequest request) throws Exception {
		String forwardPath = "";

		/************** login check **************/
		request.getSession().setAttribute("id", "1L"); //임시로 아이디 로그인상태
		request.getSession().setAttribute("sCorpId", "corp_01"); //임시로 아이디 로그인상태
		String sCorpId =(String)request.getSession().getAttribute("sCorpId");
		if(sCorpId==null) {
			forwardPath= "redirect:login";
		}else {
			//System.out.println(loginCorp);
			CorpDto loginCorp=corpService.findCorp(sCorpId);
			request.setAttribute("loginCorp", loginCorp);
			forwardPath="dashboard";
		}
		return forwardPath;
	}

	@RequestMapping("/dashboard-company-profile")
	public String corp_dashboard_company_profile(HttpServletRequest request, Model model) throws Exception {

		String forwardPath = "";

		String sCorpId = (String) request.getSession().getAttribute("sCorpId");
		CorpDto corpDto = corpService.findCorp(sCorpId);
		model.addAttribute("corp", corpDto);
		forwardPath = "dashboard-company-profile";

		return forwardPath;
	}

	@PostMapping("/corp-update-action")
	public String corp_update_action(@ModelAttribute("corp") CorpDto corpDto, HttpServletRequest request)throws Exception {
		Long id = corpDto.getId();
		System.out.println(corpDto.getCorpEst());
		
		corpDto.setCorpEst(LocalDate.now());
		corpService.update(id, corpDto);
		request.setAttribute("corLoginpId", corpDto.getCorpLoginId());
		return "corp-detail";
	}

	@RequestMapping("/dashboard-manage-job")
	public String corp_dashboard_manage_job(HttpServletRequest request, Model model) throws Exception {
		String sCorpId = (String) request.getSession().getAttribute("sCorpId");
		CorpDto corpDto = corpService.findCorpWithRecruits(sCorpId);
		model.addAttribute("corp", corpDto);
		// 지원자 숫자 보여주기
		Long appCount = appService.findAppCountByCorpId(sCorpId);
		System.out.println(appCount);
		model.addAttribute("appCount", appCount);

		return "dashboard-manage-job";
	}

	@RequestMapping("/dashboard-applicants")
	public String corp_dashboard_applicants() {
		return "dashboard-applicants";
	}


	// 검색기능
	@GetMapping("/search")
	public String searchCorp() {
		return "search";
	}

	// 검색기능
	@ResponseBody
	@PostMapping("/search")
	public Map<String, Object> searchCorp(@RequestParam("query") String query, Model model) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 상품 검색 서비스 호출
		List<CorpDto> searchResults = corpService.searchCorpList(query);

		resultMap.put("corpList", searchResults);
		// 결과 페이지를 반환
		return resultMap;
	}


//	@ExceptionHandler(Exception.class)
//	public String corp_exception_handler(Exception e) {
//		System.out.println("에러..");
//		return "shop-checkout";
//	}
}
