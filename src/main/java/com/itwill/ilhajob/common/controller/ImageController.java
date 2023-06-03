package com.itwill.ilhajob.common.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.ilhajob.common.dto.BlogDto;
import com.itwill.ilhajob.common.service.BlogService;
import com.itwill.ilhajob.corp.dto.CorpDto;
import com.itwill.ilhajob.corp.service.CorpService;
import com.itwill.ilhajob.user.dto.UserDto;
import com.itwill.ilhajob.user.service.UserService;

/*
 * 이미지 업로드 ajax 요청 코드 => image-upload.js
 */
@Controller
public class ImageController {
	
	@Autowired
	private CorpService corpService;
	@Autowired
	private UserService userService;
	@Autowired
	private BlogService blogService;
	
	@ResponseBody
	@PostMapping(value = "/board-main-upload-action")
	public String board_main_upload_action(@RequestParam("images") List<MultipartFile> images, 
			 							   @RequestParam("id") Long id, 
										   HttpServletRequest request) throws Exception {
		System.out.println("blog_ididididididididididi>>>"+id);
		BlogDto blog = blogService.findBlog(id);
		System.out.println("blogDto>>>"+blog);
		String sUserId = (String)request.getSession().getAttribute("sUserId");
		UserDto loginUser = userService.findUser(sUserId);
		
		// 운영체제 확인
	    String os = System.getProperty("os.name").toLowerCase();

	    // 현재 실행중인 프로젝트의 경로
	    String projectPath = System.getProperty("user.dir");

	    // 운영체제 구분
	    if (os.contains("mac")) {
	        // macOS 업로드 경로
	    	String uploadDirectory = projectPath + "/upload/blog/";
	        
	        // 업로드 폴더 생성
		    File uploadFolder = new File(uploadDirectory);
		    if (!uploadFolder.exists()) {
		        uploadFolder.mkdirs();
		    }

		    // MultipartFile 배열로 받은 파일을 처리하는 로직
		    for (MultipartFile image : images) {
		        if (!image.isEmpty()) {
		            String fileName = image.getOriginalFilename();
		            String saveFileName = loginUser.getId() + "_profile" + fileName.substring(fileName.lastIndexOf("."));
		            // 파일 저장 로직
		            try {
		            	byte[] bytes = image.getBytes();
		                Path path = Paths.get(uploadDirectory + saveFileName);
		                Files.write(path, bytes);
		                
		                String pattern = uploadDirectory.substring(uploadDirectory.indexOf("upload"));
		                System.out.println(pattern);
		                
		                blog.setBlogImage(pattern + saveFileName);
		                System.out.println("이미지 업데이트후 blogDto>>>"+blog);
		                blogService.updateBlog(blog.getId(), blog);
		                
		            } catch (IOException e) {
		                return "이미지 업로드 실패...";
		            }
		        }
		    }
		    return "이미지 업로드 완료";
	        
	    } else {
		
		Map<String, String> pathMap = makeDir("blog");
		// MultipartFile 배열로 받은 파일을 처리하는 로직
	    for (MultipartFile image : images) {
	        if (!image.isEmpty()) {
	            String fileName = image.getOriginalFilename();
	            String saveFileName = blog.getId()+"_blog"+loginUser.getId()+"_user"+fileName.substring(fileName.lastIndexOf("."));
	            // 파일 저장 로직
	            try {
	                byte[] bytes = image.getBytes();
	                Path path = Paths.get(pathMap.get("absolutePath") + saveFileName);
	                Files.write(path, bytes);
	                blog.setBlogImage(pathMap.get("urlPath") + saveFileName);
	                System.out.println("이미지 업데이트후 blogDto>>>"+blog);
	                blogService.updateBlog(blog.getId(), blog);
	            } catch (IOException e) {
	            	return "이미지 업로드 실패...";
	            }
	        }
	    }
	    return "이미지 업로드 완료";
	    }
	}
	
	// 유저프로필 업로드
	@ResponseBody
	@PostMapping(value = "/user-profile-upload-action")
	public String user_profile_upload_action(@RequestParam("images") List<MultipartFile> images, HttpServletRequest request) throws Exception {
		String sUserId = (String)request.getSession().getAttribute("sUserId");
		UserDto loginUser = userService.findUser(sUserId);
		
		// 운영체제 확인
	    String os = System.getProperty("os.name").toLowerCase();

	    // 현재 실행중인 프로젝트의 경로
	    String projectPath = System.getProperty("user.dir");

	    // 운영체제 구분
	    if (os.contains("mac")) {
	        // macOS 업로드 경로
	    	String uploadDirectory = projectPath + "/upload/profile/";
	        
	        // 업로드 폴더 생성
		    File uploadFolder = new File(uploadDirectory);
		    if (!uploadFolder.exists()) {
		        uploadFolder.mkdirs();
		    }

		    // MultipartFile 배열로 받은 파일을 처리하는 로직
		    for (MultipartFile image : images) {
		        if (!image.isEmpty()) {
		            String fileName = image.getOriginalFilename();
		            String saveFileName = loginUser.getId() + "_profile" + fileName.substring(fileName.lastIndexOf("."));
		            // 파일 저장 로직
		            try {
		                byte[] bytes = image.getBytes();
		                Path path = Paths.get(uploadDirectory + saveFileName);
		                Files.write(path, bytes);
		                
		                String pattern = uploadDirectory.substring(uploadDirectory.indexOf("upload"));
		                System.out.println(pattern);
		                
		                loginUser.setUserImage(pattern + saveFileName);
		                userService.update(loginUser.getId(), loginUser);
		                request.getSession().setAttribute("profileAvatar", loginUser.getUserImage());
		                System.out.println("업로드 성공????????" + request.getSession().getAttribute("profileAvatar"));
		            } catch (IOException e) {
		                return "이미지 업로드 실패...";
		            }
		        }
		    }
		    return "이미지 업로드 완료";
	        
	    } else {
		//절대경로 profile은 config에서 경로등록
		Map<String, String> pathMap = makeDir("profile");
		
		// MultipartFile 배열로 받은 파일을 처리하는 로직
	    for (MultipartFile image : images) {
	        if (!image.isEmpty()) {
	            String fileName = image.getOriginalFilename();
	            String saveFileName = loginUser.getId()+"_profile"+fileName.substring(fileName.lastIndexOf("."));
	            // 파일 저장 로직
	            try {
	                byte[] bytes = image.getBytes();
	                Path path = Paths.get(pathMap.get("absolutePath") + saveFileName);
	                Files.write(path, bytes);
	                loginUser.setUserImage(pathMap.get("urlPath") + saveFileName);
	                userService.update(loginUser.getId(), loginUser);
	                request.getSession().setAttribute("profileAvatar", loginUser.getUserImage());
	                //return ResponseEntity.ok().body("{\"success\": true, \"imagePath\": \"" + corp.getCorpStoredFileName() + "\", \"message\": \"리뷰가 성공적으로 작성되었습니다.\"}");
	            } catch (IOException e) {
	            	//return ResponseEntity.status(5101).body("{\"success\": false, \"message\": \"이미지 업로드 실패.\"}");
	            	return "이미지 업로드 실패...";
	            }
	        }
	    }
	    return "이미지 업로드 완료";
	    }
	}
	
	// 기업로고 업로드
	@ResponseBody
	@PostMapping(value = "/corp-logo-upload-action")
	public String corp_logo_upload_action(@RequestParam("images") List<MultipartFile> images, HttpServletRequest request) throws Exception {
		Long sCorpId =(Long)request.getSession().getAttribute("id");
		CorpDto corp=corpService.findByCorpId(sCorpId);
		
		// 운영체제 확인
		String os = System.getProperty("os.name").toLowerCase();

		// 현재 실행중인 프로젝트의 경로
		String projectPath = System.getProperty("user.dir");
	    
		// 운영체제 구분
		if(os.contains("mac")) {
			
			// macOS 업로드 경로
			String uploadDirectory = projectPath + "/upload/logo/";
	        
	        // 업로드 폴더 생성
		    File uploadFolder = new File(uploadDirectory);
		    if (!uploadFolder.exists()) {
		        uploadFolder.mkdirs();
		    }

		    // MultipartFile 배열로 받은 파일을 처리하는 로직
		    for (MultipartFile image : images) {
		        if (!image.isEmpty()) {
		            String fileName = image.getOriginalFilename();
		            String saveFileName = corp.getId() + "_profile" + fileName.substring(fileName.lastIndexOf("."));
		            // 파일 저장 로직
		            try {
		                byte[] bytes = image.getBytes();
		                Path path = Paths.get(uploadDirectory + saveFileName);
		                Files.write(path, bytes);
		                
		                String pattern = uploadDirectory.substring(uploadDirectory.indexOf("upload"));
		                System.out.println(pattern);
		                
		                corp.setCorpStoredFileName(pattern + saveFileName);
		                corpService.update(corp.getId(), corp);
		                request.getSession().setAttribute("profileAvatar", corp.getCorpStoredFileName());
		                System.out.println("corp logo************ : " + request.getSession().getAttribute("profileAvatar"));
		            } catch (IOException e) {
		                return "이미지 업로드 실패...";
		            }
		        }
		    }
		    return "이미지 업로드 완료";
			
		} else {
		//절대경로 logo은 config에서 경로등록
		Map<String, String> pathMap = makeDir("logo");
		
		// MultipartFile 배열로 받은 파일을 처리하는 로직
	    for (MultipartFile image : images) {
	        if (!image.isEmpty()) {
	            String fileName = image.getOriginalFilename();
	            String saveFileName = corp.getId()+"_logo"+fileName.substring(fileName.lastIndexOf("."));
	            // 파일 저장 로직
	            try {
	                byte[] bytes = image.getBytes();
	                Path path = Paths.get(pathMap.get("absolutePath") + saveFileName);
	                Files.write(path, bytes);
	                corp.setCorpStoredFileName(pathMap.get("urlPath") + saveFileName);
	                corpService.update(corp.getId(), corp);
	                request.getSession().setAttribute("profileAvatar", corp.getCorpStoredFileName());
	                //return ResponseEntity.ok().body("{\"success\": true, \"imagePath\": \"" + corp.getCorpStoredFileName() + "\", \"message\": \"리뷰가 성공적으로 작성되었습니다.\"}");
	            } catch (IOException e) {
	            	//return ResponseEntity.status(5101).body("{\"success\": false, \"message\": \"이미지 업로드 실패.\"}");
	            	return "이미지 업로드 실패...";
	            }
	        }
	    }
	    return "이미지 업로드 완료";
		}
	}
	
	//폴더메이킹
	public static Map<String, String> makeDir(String path){
		Map<String, String> map = new HashMap<String,String>();
		//절대경로 저장
		List<String> absPath = new ArrayList<String>();
		absPath.add("upload/");
		absPath.add(path+"/");
		
		String urlPath="";
		String absolutePath="c:/final-project-team1-ilhajob/";
		File folder = new File(absolutePath);
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		for (String string : absPath) {
			absolutePath+=string;
			urlPath+=string;
			folder = new File(absolutePath);
			if(!folder.exists()) {
				folder.mkdir();
				System.out.println(folder+"폴더가 생성되었습니다");
			}else {
				System.out.println(folder+"이미 폴더가 존재합니다");
			}
		}
		map.put("absolutePath", absolutePath);
		map.put("urlPath", urlPath);
		return map;
	}

}
