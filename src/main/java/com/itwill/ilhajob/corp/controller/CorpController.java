
package com.itwill.ilhajob.corp.controller;

import java.io.File;

import java.util.Comparator;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.type.LocalDateType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.ilhajob.common.dto.AppDto;
import com.itwill.ilhajob.common.dto.BlogDto;
import com.itwill.ilhajob.common.dto.CorpTagDto;
import com.itwill.ilhajob.common.dto.TagDto;
import com.itwill.ilhajob.common.entity.Tag;
import com.itwill.ilhajob.common.service.AppService;
import com.itwill.ilhajob.common.service.CorpTagService;
import com.itwill.ilhajob.common.service.TagService;
import com.itwill.ilhajob.corp.dto.CorpDto;
import com.itwill.ilhajob.corp.dto.CorpImageDto;
import com.itwill.ilhajob.corp.dto.CorpResponseDto;
import com.itwill.ilhajob.corp.dto.ManagerDto;
import com.itwill.ilhajob.corp.dto.RecruitDto;
import com.itwill.ilhajob.corp.entity.Corp;
import com.itwill.ilhajob.corp.entity.CorpImage;
import com.itwill.ilhajob.corp.entity.Manager;
import com.itwill.ilhajob.corp.exception.CorpNotFoundException;
import com.itwill.ilhajob.corp.repository.ManagerRepository;
import com.itwill.ilhajob.corp.service.CorpImageService;
import com.itwill.ilhajob.corp.service.CorpService;
import com.itwill.ilhajob.corp.service.CorpServiceImpl;
import com.itwill.ilhajob.corp.service.ManagerService;
import com.itwill.ilhajob.corp.service.RecruitService;
import com.itwill.ilhajob.user.controller.LoginCheck;
import com.itwill.ilhajob.user.dto.ReviewDto;
import com.itwill.ilhajob.user.dto.UserDto;
import com.itwill.ilhajob.user.exception.PasswordMismatchException;
import com.itwill.ilhajob.user.service.ReviewService;
import com.itwill.ilhajob.user.service.UserService;

@Controller
public class CorpController {

	@Autowired
	private CorpService corpService;
	@Autowired
	private CorpImageService corpImageService;
	@Autowired
	private RecruitService recruitService;

	@Autowired
	private UserService userService;

	@Autowired
	private AppService appService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CorpTagService corpTagService;

	@Autowired
	private TagService tagService;

	@RequestMapping("/index")
	public String main() {
		String forward_path = "index";
		return forward_path;
	}

	
	@GetMapping("/corp-list")
	public String corp_list(@RequestParam(defaultValue = "0") int page,
	                        @RequestParam(defaultValue = "12") int size,
	                        Model model) throws Exception {
		//페이징 기능 추가->일단 12개씩 나오게 해놓음
	    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
	    Page<CorpDto> corpPage = corpService.findAll(pageable);
	    int nowPage = corpPage.getNumber();
	    
	    //이전, 다음페이지 설정해야함...
	    model.addAttribute("corpList", corpPage.getContent());
	    model.addAttribute("nowPage", nowPage);
	    model.addAttribute("totalPage", corpPage.getTotalPages());
	    model.addAttribute("prePage", corpPage.hasPrevious() ? corpPage.previousPageable().getPageNumber() : 0);
	    model.addAttribute("nextPage", corpPage.hasNext() ? corpPage.nextPageable().getPageNumber() : corpPage.getTotalPages() - 1);
	    
		List<CorpTagDto> corpTagList = corpTagService.selectAll();
		List<TagDto> tagList = tagService.selectAll();
		model.addAttribute("tagList", tagList);
		model.addAttribute("corpTagList", corpTagList);
		String forward_path = "corp-list";
	    
	    return "corp-list";
	}

//	@RequestMapping("/corp-list")
//	public String corp_list(@RequestParam(defaultValue = "0") int page,
//	                        @RequestParam(defaultValue = "12") int size,
//	                        Model model) throws Exception {
//	    String forward_path = "corp-list";
//	    //페이징 기능 추가
//	    Page<CorpDto> corpPage=corpService.getCorpList(page, size);
//	    System.out.println("corpPage>>>>>>"+corpPage); //corpPage>>>>>>Page 1 of 2 containing com.itwill.ilhajob.corp.dto.CorpDto instances
//	    System.out.println("page는 뭐임??>>>>>>>"+page); //0
//	    model.addAttribute("corpList",corpPage.getContent());
//	    model.addAttribute("page",corpPage);
//	    
//	    // 현재 페이지 정보와 총 페이지 수를 모델에 추가
//	    model.addAttribute("currentPage",corpPage.getPageable());
//	    System.out.println("currentPage는 뭐임??>>>>>>"+corpPage.getPageable());
//	    model.addAttribute("totalPages", corpPage.getTotalPages());
//
//	    List<CorpTagDto> corpTagList = corpTagService.selectAll();
//	    List<TagDto> tagList = tagService.selectAll();
//	    model.addAttribute("corpTagList", corpTagList);
//	    model.addAttribute("tagList", tagList);
//
//	    return forward_path;
//	}
	
	
	@RequestMapping("corp-detail")
	public String corp_detail_view(@RequestParam("corpId") Long corpId, HttpServletRequest request, Model model)
			throws Exception {
		String sUserId = (String) request.getSession().getAttribute("sUserId");

		// 공고 개수 불러오기
		CorpDto corpDto1 = corpService.findByCorpId(corpId);
		System.out.println("corpDto1>>>>>>" + corpDto1);
		Long recruitCount = recruitService.countByCorpId(corpDto1.getId());
		System.out.println("공고개수>>>>>>" + recruitCount);
		model.addAttribute("recruitCount", recruitCount);

		if (sUserId == null) {
			CorpDto corpDto = corpService.findByCorpId(corpId);
			model.addAttribute("corp", corpDto);

			// 기업 태그 리스트 뿌리기
			List<CorpTagDto> corpTagList = corpTagService.selectAllByCorpId(corpDto.getId());
			model.addAttribute("corpTagList", corpTagList);

			// 공고 목록 뿌리기
			List<RecruitDto> recruitList = recruitService.findRecruitAll();
			List<RecruitDto> recruitList1 = new ArrayList<>();
			for (RecruitDto recruitDto : recruitList) {
				if (recruitDto.getCorp().getId() == corpDto.getId()) {
					recruitList1.add(recruitDto);
				}
			}
			model.addAttribute("recruitList", recruitList1);
			// 리뷰 목록 뿌리기

			List<ReviewDto> reviewList = corpService.findReviewList(corpDto.getId());
			model.addAttribute("reviewList", reviewList);
		} else {
			CorpDto corpDto = corpService.findByCorpId(corpId);
			model.addAttribute("corp", corpDto);

			// 공고 목록 뿌리기
			List<RecruitDto> recruitList = recruitService.findRecruitAll();
			List<RecruitDto> recruitList1 = new ArrayList<>();

			for (RecruitDto recruitDto : recruitList) {
				if (recruitDto.getCorp().getId() == corpDto.getId()) {
					recruitList1.add(recruitDto);
				}
			}
			model.addAttribute("recruitList", recruitList1);
			// String sUserId = (String)request.getSession().getAttribute("sUserId");
			UserDto loginUser = userService.findUser(sUserId);
			long count = reviewService.isReviewDuplicate(loginUser.getId(), corpDto.getId()); // 이미 리뷰존재하면 1, 없으면 0
			model.addAttribute("count", count);
			request.setAttribute("loginUser", loginUser);

			// 리뷰 목록 뿌리기

			List<ReviewDto> reviewList = corpService.findReviewList(corpDto.getId());
			model.addAttribute("reviewList", reviewList);
		}

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
			session.setAttribute("sCorpId", corpDto.getId());
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
	
	@RequestMapping("/corp_logout_action")
	public String corp_logout_action(@ModelAttribute("fcorp")CorpDto corpDto,HttpServletRequest request, HttpSession session) {
		String forwardPath="";
		request.getSession().invalidate();
		forwardPath="redirect:index";
		return forwardPath;
	}
	

	/*****************************************************************************
	 * 대쉬보드
	 ******************************************************************************/

	@RequestMapping("/dashboard")
	public String corp_dashboard_view(HttpServletRequest request) throws Exception {
		String forwardPath = "";

		/************** login check **************/
		// request.getSession().setAttribute("id", "1L"); //임시로 아이디 로그인상태
		request.getSession().setAttribute("sCorpId", 1L); // 임시로 아이디 로그인상태
		Long sCorpId = (Long) request.getSession().getAttribute("sCorpId");
		if (sCorpId == null) {
			forwardPath = "redirect:login";
		} else {
			// System.out.println(loginCorp);
			CorpDto loginCorp = corpService.findByCorpId(sCorpId);
			request.setAttribute("corp", loginCorp);
			forwardPath = "dashboard";
		}
		return forwardPath;
	}

	@RequestMapping("/dashboard-company-profile")
	public String corp_dashboard_company_profile(HttpServletRequest request, Model model) throws Exception {

		String forwardPath = "";
		Long sCorpId = (Long) request.getSession().getAttribute("sCorpId");
		CorpDto corpDto = corpService.findByCorpId(sCorpId);
		/*********** CorpImage 코프 로그인아이디로 리스트뽑아오기 *****************/
		List<CorpImageDto> corpImageList = corpImageService.findAllByCorpId(sCorpId);
		/*******************************************************************/
		model.addAttribute("corp", corpDto);
		model.addAttribute("corpImageList", corpImageList);
		forwardPath = "dashboard-company-profile";

		return forwardPath;
	}

	@PostMapping("/corp-update-action")
	public String corp_update_action(@ModelAttribute("corp") CorpResponseDto corpDto, @RequestParam("date") String date,
			HttpServletRequest request)
			throws Exception {
		Long id = corpDto.getId();
		System.out.println(corpDto);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime time = LocalDate.parse(date, formatter).atStartOfDay();
		CorpDto corp = CorpDto.builder().corpAddress(corpDto.getCorpAddress())
										.corpBusinessNo(corpDto.getCorpBusinessNo())
										.corpComment(corpDto.getCorpComment())
										.corpEst(time)
										.corpName(corpDto.getCorpName())
										.corpLoginId(corpDto.getCorpLoginId())
										.corpPhone(corpDto.getCorpPhone())
										.corpPassword(corpDto.getCorpPassword())
										.corpOriginalFileName(corpDto.getCorpOriginalFileName())
										.corpSales(corpDto.getCorpSales())
										.corpWebsite(corpDto.getCorpWebsite())
										.corpSize(corpDto.getCorpSize())
										.corpWelfare(corpDto.getCorpWelfare())
										.corpStoredFileName(corpDto.getCorpStoredFileName()).build();
		
		corpService.update(id, corp);
		request.setAttribute("corpId", id);
		return "redirect:corp-detail?corpId="+id;
		
	}

	@RequestMapping("/dashboard-manage-job")
	public String corp_dashboard_manage_job(@ModelAttribute("message") String message, HttpServletRequest request,
			Model model, @RequestParam(value = "sortType", required = false) String sortType) throws Exception {
		// 회사의 작성 공고 띄우기
		Long sCorpId = (Long) request.getSession().getAttribute("sCorpId");
		CorpDto corpDto = corpService.findByCorpId(sCorpId);
		List<RecruitDto> recruitList = recruitService.findAllByCorpId(corpDto.getId());
		model.addAttribute("recruitList", recruitList);

		// 공고 정렬
		// 마감일 내림차순
		if ("rcDeadlinedesc".equalsIgnoreCase(sortType)) {
			recruitList.sort((o1, o2) -> o2.getRcDeadline().compareTo(o1.getRcDeadline()));
		} else {
			// 마감일 오름차순
			recruitList.sort(Comparator.comparing(RecruitDto::getRcDeadline));
		}
		model.addAttribute("recruitList", recruitList);

		return "dashboard-manage-job";
	}

	// 지원자 관련
	@RequestMapping(value = "/dashboard-applicants", params = "id")
	public String corp_dashboard_applicants(@RequestParam("id") long id, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		// 지원자 이력서 리스트 불러오기
		try {
			List<AppDto> appList = appService.findAllByRecruitId(id);
			// 리스트 있을 때
			model.addAttribute("appList", appList);
			//appStatus=2인것만 따로 담기(승인)
			List<AppDto> approveList=appList.stream().filter(appDto->appDto.getAppStatus()==2)
													 .collect(Collectors.toList());
			System.out.println("approveList>>>>>>>>>"+approveList);
			model.addAttribute("approveList",approveList);
			//appStatus=3인것만 따로 담기(거절)
			List<AppDto> rejectList=appList.stream().filter(appDto->appDto.getAppStatus()==3)
													.collect(Collectors.toList());
			model.addAttribute("rejectList",rejectList);
			
			
		} catch (Exception e) {
			// 리스트 없을 때
			// redirectAttributes.addFlashAttribute("message", e.getMessage());
			// redirectAttributes.addFlashAttribute("alertType", "danger"); // alert 창 색상을
			// 지정하기 위한 속성
			model.addAttribute("errorMsg", e.getMessage());
			return "redirect:dashboard-manage-job";
		}

		// 이력서의 회원 정보 가져오기
		List<AppDto> userList = appService.findAllByUserId(id);
		System.out.println("이력서 회원 정보 확인>>>>>"+userList);
		model.addAttribute("userList", userList);

		// 해당 공고 디테일 뿌리기
		RecruitDto recruit = recruitService.findRecruit(id);
		model.addAttribute("recruit", recruit);
		
		
		

		return "dashboard-applicants";
	}

	/*
	 * // 검색기능
	 * 
	 * @GetMapping("/search") public String searchCorp() { return "search"; }
	 * 
	 * // 검색기능
	 * 
	 * @ResponseBody
	 * 
	 * @PostMapping("/search") public Map<String, Object>
	 * searchCorp(@RequestParam("query") String query, Model model) throws Exception
	 * { Map<String, Object> resultMap = new HashMap<String, Object>(); // 상품 검색 서비스
	 * 호출 List<CorpDto> searchResults = corpService.searchCorpList(query);
	 * 
	 * resultMap.put("corpList", searchResults); // 결과 페이지를 반환 return resultMap; }
	 */

	// 이미지 업로드
	@PostMapping("/imageUpload")
	public String handleImageUpload(MultipartFile file, Model model, HttpServletRequest request) throws Exception {
		if (!file.isEmpty()) {
			try {
				// 파일 저장 로직 구현 (예: 서버에 파일 저장, 파일 정보 DB에 저장 등)
				String fileName = file.getOriginalFilename();
				String CorpImageUrl = "C:\\2022-11-JAVA-DEVELOPER\\git_repositories\\final-project-team1-xxx\\src\\main\\resources\\imageUpload\\"
						+ fileName;
				file.transferTo(new File(CorpImageUrl));

//                CorpImageDto corpImage = new CorpImageDto(5,
//                									CorpImageUrl,
//                									1);
//                corpImageService.insertCorpImage(corpImage);
				// 파일 처리가 완료된 후에는 해당 정보를 모델에 추가하여 View로 전달
				model.addAttribute("fileName", fileName);

				// 업로드 처리가 완료된 후에는 성공 페이지 또는 결과 페이지를 반환
				return "/dashboard"; // 성공 페이지 또는 결과 페이지를 반환

			} catch (IOException e) {
				// 파일 업로드 처리 중에 예외 발생 시 에러 처리
				model.addAttribute("error", "Failed to upload file. Error: " + e.getMessage());
				return "error"; // 에러 페이지 또는 에러 처리 로직을 반환
			}
		} else {
			// 업로드된 파일이 없는 경우 예외 처리 또는 에러 처리
			model.addAttribute("error", "No file uploaded.");
			return "error"; // 에러 페이지 또는 에러 처리 로직을 반환
		}
	}

	@RequestMapping("/image-test")
	public String image_test(HttpServletRequest request, Model model) throws Exception {

		String forwardPath = "";
		request.getSession().setAttribute("sCorpId", 1L); // 임시로 아이디 로그인상태
		Long sCorpId = (Long) request.getSession().getAttribute("sCorpId");
		CorpDto corpDto = corpService.findByCorpId(sCorpId);
		/*********** CorpImage 코프 로그인아이디로 리스트뽑아오기 *****************/
		List<CorpImageDto> corpImageList = corpImageService.findAllByCorpId(sCorpId);
		/*******************************************************************/
		model.addAttribute("corp", corpDto);
		model.addAttribute("corpImageList", corpImageList);
		System.out.println("===========================================");
		forwardPath = "image-upload-test";
		return forwardPath;
	}

	// corpName, job 둘 중 하나만 알아도 + 둘다 알때 검색할 수 있는 기능
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchCorps(@RequestParam("corpName") String corpName, 
							  @RequestParam("job") String job,
							  @RequestParam(defaultValue = "0") int page,
		                      @RequestParam(defaultValue = "12") int size,
							  Pageable pageable,
	                          Model model) {
	    try {
	        List<CorpDto> corpSearchList = new ArrayList<>();
	      //페이징 기능 추가->일단 12개씩 나오게 해놓음
		    Pageable pageable1 = PageRequest.of(page, size, Sort.Direction.ASC, "id");
		    Page<CorpDto> corpPage = corpService.findAll(pageable1);
		    int nowPage = corpPage.getNumber();
		    
		    //이전, 다음페이지 설정해야함...
		    model.addAttribute("corpList", corpPage.getContent());
		    model.addAttribute("nowPage", nowPage);
		    model.addAttribute("totalPage", corpPage.getTotalPages());
		    model.addAttribute("prePage", corpPage.hasPrevious() ? corpPage.previousPageable().getPageNumber() : 0);
		    model.addAttribute("nextPage", corpPage.hasNext() ? corpPage.nextPageable().getPageNumber() : corpPage.getTotalPages() - 1);
	        // corpName만 알때
	        if (job.isEmpty()) {
	            corpSearchList = corpService.searchByCorpName(corpName,pageable1);
	        // job만 알 때
	        } else if(corpName.isEmpty()){
	            corpSearchList = corpService.searchByjob(job,pageable1);
	        // 둘 다 알 때
	        } else {
	            corpSearchList = corpService.searchCorps(corpName, job,pageable1);
	        }
	        //검색 결과 없을 때
	        model.addAttribute("noResults", corpSearchList.isEmpty());
	        model.addAttribute("corpList", corpSearchList);
	    } catch (Exception e) {
	        // 예외 처리
	        e.printStackTrace();
	        model.addAttribute("errorMsg", "검색어를 찾을 수 없습니다!");
	    }
	    return "corp-list";
	}
}