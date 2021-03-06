package com.kh.walkwork.community.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.walkwork.common.model.vo.Attachment;
import com.kh.walkwork.common.model.vo.PageInfo;
import com.kh.walkwork.common.template.Pagination;
import com.kh.walkwork.community.model.service.CommunityService;
import com.kh.walkwork.community.model.vo.BoardGood;
import com.kh.walkwork.community.model.vo.Community;
import com.kh.walkwork.community.model.vo.Reply;
import com.kh.walkwork.member.model.vo.Member;

@Controller
public class CommunityController {

	@Autowired
	private CommunityService communityService;
	
	@RequestMapping("write.co")
	public String writeCommunity() {
		return "community/communityWriteForm";
	}
	
	
	// 수정 페이지 이동
	@RequestMapping("modifyPage.co")
	public ModelAndView modifyPage(@RequestParam(value = "pageNo") int pageNo,ModelAndView mv) {
		
		Community detail = communityService.selectDetail(pageNo);
		ArrayList<Attachment> attachment = communityService.selectAttachmentDetail(pageNo);
		
		mv.addObject("detail", detail).addObject("attachment", attachment).setViewName("community/communityModify");
		
		return mv;
	}
	
	// 실제로 modify시키는거
	@RequestMapping("modify.co")
	public String modifyCommunity(HttpSession session, Attachment a, Community c, Model model) {
		String[] nameArr = a.getFileName().split(",");
//		String[] memberArr = a.getMemberNo().split(",");
		String[] originArr = a.getFileOriginName().split(",");
		String[] pathArr = a.getFilePath().split(",");
		String[] sizeArr = a.getFileSize().split(",");
		String[] docuArr = a.getDocuNo().split(",");

		int num = communityService.updateCommunity(c); 
		Member userInfo = (Member)session.getAttribute("loginUser");
		String memberNo = userInfo.getMemberNo();
	
		a.setBoardNo(c.getBoardNo());
		  
		// 사진의 정보를 db에 넣어줌 :-)
		if(a.getFileName().length() != 0) {
			for(int i=0; i< originArr.length; i++) {
				Attachment attachment = new Attachment();
				attachment.setFileName(nameArr[i].trim());
				attachment.setBoardNo(c.getBoardNo());
				attachment.setMemberNo(memberNo);
				attachment.setFileOriginName(originArr[i].trim());
				attachment.setFilePath(pathArr[i].trim());
				attachment.setFileSize(sizeArr[i].trim());
				attachment.setDocuNo(docuArr[i].trim());
				int num2 = communityService.insertAttachment(attachment);
				
			}
		}
		 

		return "redirect:list.co";
	}
	
	// 글 삭제
	@RequestMapping("delete.co")
	public String deleteCommunity(@RequestParam(value = "pageNo") int pageNo, Model model) {
		// 권한 내용
		
		int num = communityService.deleteCommunity(pageNo); 

		return "redirect:list.co";
	}

	// 글 가져오기
	@RequestMapping("list.co")
	public ModelAndView selectList(@RequestParam(value = "cpage", defaultValue="1") int currentPage, 
			ModelAndView mv, @RequestParam(value = "search", required = false, defaultValue = "") String search) {
		
		int listCount = communityService.selectListCount(search);
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 6);
		
		ArrayList<Community> list = communityService.selectList(pi, search);

		
		ArrayList<Community> top = communityService.selectTopCm();
		
		ArrayList<Attachment> topImages = communityService.selectViewAtt();
		
		
		mv.addObject("search", search).addObject("pi", pi).addObject("list", list).addObject("top", top)
		.addObject("topImages",topImages).setViewName("community/communityView");

		return mv;
		
	}
	
	// 내 게시글 조회
	@RequestMapping("mylist.co")
	public ModelAndView selectMyList(HttpSession session, @RequestParam(value = "cpage", defaultValue="1") int currentPage, ModelAndView mv, @RequestParam(value = "search", required = false, defaultValue = "") String search) {
		Member userInfo = (Member)session.getAttribute("loginUser");
		String memberNo = userInfo.getMemberNo();
		
		Community c = new Community();
		c.setBoardWriter(memberNo);
		c.setBoardTitle(search);
		
		int listCount = communityService.selectMyListCount(c); // 내 게시글 전체 수로 변경
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 6);
		
		
		
		Community community = new Community();
		community.setBoardTitle(search);
		community.setBoardWriter(memberNo);
		ArrayList<Community> list = communityService.selectMyCommunity(pi, community);
		
		
		mv.addObject("search", search).addObject("pi", pi).addObject("list", list).setViewName("community/communityMyWrting");

		return mv;
		
	}
	
	// 상세조회  
	@RequestMapping("detail.co")
	public ModelAndView detailCommunity(@RequestParam(value = "pageNo") int pageNo,ModelAndView mv) {
		
		
		Community detail = communityService.selectDetail(pageNo);
		ArrayList<Attachment> attachment = communityService.selectAttachmentDetail(pageNo);
		ArrayList<Reply> replys = communityService.selectReply(pageNo);
		
		mv.addObject("detail", detail).addObject("replys",replys).addObject("attachment", attachment).setViewName("community/communityDetail");
		
		return mv;
	}

	// 글 작성하기
	@RequestMapping("insert.co")
	public String insertCommunity(HttpSession session, Attachment a, Community c, Model model) {
		String[] nameArr = a.getFileName().split(",");
//			String[] memberArr = a.getMemberNo().split(",");
		String[] originArr = a.getFileOriginName().split(",");
		String[] pathArr = a.getFilePath().split(",");
		String[] sizeArr = a.getFileSize().split(",");
		String[] docuNo = a.getDocuNo().split(",");
		
		Member userInfo = (Member)session.getAttribute("loginUser");
		String memberNo = userInfo.getMemberNo();

		
		c.setBoardWriter(memberNo); 
		
		int num = communityService.insertCommunity(c); 
		int lastBno = communityService.lastBno();
		a.setBoardNo(lastBno);
		  
		// 사진의 정보를 db에 넣어줌 :-)
		if(a.getFileName().length() != 0) {
			for(int i=0; i< originArr.length; i++) {
				Attachment attachment = new Attachment();
				attachment.setFileName(nameArr[i].trim());
				attachment.setBoardNo(lastBno);
				attachment.setMemberNo(memberNo);
				attachment.setFileOriginName(originArr[i].trim());
				attachment.setFilePath(pathArr[i].trim());
				attachment.setFileSize(sizeArr[i].trim());
				attachment.setDocuNo(docuNo[i].trim());
				int num2 = communityService.insertAttachment(attachment);
				
			}
		}
		 

		return "redirect:list.co";
	}
	
	// 파일 삽입하기 
	@ResponseBody
	@RequestMapping("insert.ac")
	public List<Attachment> insertAttachment(HttpServletRequest req, HttpSession session,
			@RequestPart(value="file1", required=false) List<MultipartFile> multi ,Attachment a, Model model)  {
	
		String path = session.getServletContext().getRealPath("/resources/coFile/");
		
		List<Attachment> result = new ArrayList<Attachment>();
		try {
			for(MultipartFile mf : multi) {
				String uploadpath = path;
				String originFileName = mf.getOriginalFilename();
				String extName = originFileName.substring(originFileName.lastIndexOf("."), originFileName.length());
				long size = mf.getSize();
				String saveFileName =  genSaveFileName(extName);
				File dir = new File(path);
				if(!dir.exists()) {
					dir.mkdir();
				}
				if(!multi.isEmpty()) {
					File file = new File(uploadpath, saveFileName);
					
	                mf.transferTo(file);
	                
	                Attachment attaachment = new Attachment();
	                attaachment.setFileName(saveFileName);
//		                attaachment.setBoardNo(lastBno);
	                attaachment.setFileOriginName(originFileName);
	                attaachment.setFilePath(path);
	                attaachment.setFileSize(Long.toString(size));
	                
	                result.add(attaachment);
				}
			}
			
		} catch (Exception e) {
			System.err.println("그림 없을때 catch");
			e.printStackTrace();
		} 
		return result;
	}
	
	// 파일 삭제
	@ResponseBody
	@RequestMapping("delete.ac")
	public String deleteAttachment(HttpServletRequest req, HttpSession session, Attachment a, Model model)  {
		String result = "";
		if(a.getFileName().length() > 2) {
			System.out.println("del" + a.getFileName().length());
			String[] delArr = a.getFileName().split(",");
			int num = communityService.deleteAttachment(delArr);
		}
		
		return result;
	}
	
	
	// 파일 수정
	@ResponseBody
	@RequestMapping("modify.ac")
	public List<Attachment> modifyAttachment(HttpServletRequest req, HttpSession session,@RequestPart(value="file1", required=false) List<MultipartFile> multi,@RequestParam(value = "bno") int bno, Model model)  {
//		String path = "C:\\walkworkFiles"; 
		String path = session.getServletContext().getRealPath("/resources/coFile/");
		List<Attachment> result = new ArrayList<Attachment>();
		
		try {
			if(multi.size() > 0) {
				System.out.println("multi > 0");
				for(MultipartFile mf : multi) {
					String uploadpath = path;
					String originFileName = mf.getOriginalFilename();
					String extName = originFileName.substring(originFileName.lastIndexOf("."), originFileName.length());
					long size = mf.getSize();
					String saveFileName =  genSaveFileName(extName);
					File dir = new File(path);
					if(!dir.exists()) {
						dir.mkdir();
					}
					if(!multi.isEmpty()) {
						System.out.println("bno>>"+bno);
						File file = new File(uploadpath, saveFileName);
						
		                mf.transferTo(file);
		                
		                Attachment attaachment = new Attachment();
		                attaachment.setFileName(saveFileName);
			            attaachment.setBoardNo(bno);
		                attaachment.setFileOriginName(originFileName);
		                attaachment.setFilePath(path);
		                attaachment.setFileSize(Long.toString(size));
		                
		                result.add(attaachment);
					}
				}
			}
			
			
		} catch (Exception e) {
			System.err.println("그림 없을때 catch");
			e.printStackTrace();
		} 
		return result;
	}
	
	// 파일 이름 설정 
	private String genSaveFileName(String extName) {
        String fileName = "";
        
        Calendar calendar = Calendar.getInstance();
        fileName += calendar.get(Calendar.YEAR);
        fileName += calendar.get(Calendar.MONTH);
        fileName += calendar.get(Calendar.DATE);
        fileName += calendar.get(Calendar.HOUR);
        fileName += calendar.get(Calendar.MINUTE);
        fileName += calendar.get(Calendar.SECOND);
        fileName += calendar.get(Calendar.MILLISECOND);
        fileName += extName;
        
        return fileName;
    }
	
	// 댓글 입력
	@RequestMapping("insertReply.co")
	public String insertReply(Reply r, Model model, HttpSession session) {
		String result = "redirect:detail.co?pageNo=" + r.getBoardNo();
		r.setBoardNo(r.getBoardNo());
		Member userInfo = (Member) session.getAttribute("loginUser");
		r.setMemberNo(userInfo.getMemberNo());
		int num = communityService.insertReply(r);
		
		
		return result;
	}
	
	// 댓글 삭제
	@RequestMapping("deleteReply.co")
	public String deleteReply(Reply r, Model model ) {
		String result = "redirect:detail.co?pageNo=" + r.getBoardNo();
		
		int num = communityService.deleteReply(r);
		
		return result;
	}
	
	// 추천 증가 
	@ResponseBody
	@RequestMapping("thumbsPlus.co")
	public String thumbsPlus(Model model, Community c ) {
		String result = "";
		// select시 값이 존재하면 insert하지 않음
		BoardGood bg = new BoardGood();
		bg.setBoardNo(c.getBoardNo());
		bg.setMemberNo(c.getBoardWriter());
		
		int good = communityService.selectThumbsGood(bg);
		if(good == 0) { // 사용자가 이 게시물에 공감한적이 없음
			
			c.setCommunityGood(c.getCommunityGood()+1);
			int num = communityService.thumbsPlus(c);
			communityService.insertThumbsGood(bg);
			if(num == 1) { // 추가 성공
				result = "s";
			}else { // 추가 중 의문의 사고
				result = "f";
			}
			
		}else { // 사용자가 이 게시물에 공감한적이 있음
			result = "d";
		}
		
		
		return result;
	}
	
	// 추천 취소
	@ResponseBody
	@RequestMapping("thumbsMinus.co")
	public String thumbsMinus(Model model, Community c ) {
		String result = "";
		// delete
		BoardGood bg = new BoardGood();
		bg.setBoardNo(c.getBoardNo());
		bg.setMemberNo(c.getBoardWriter());
		communityService.deleteThumbsGood(bg);
		c.setCommunityGood(c.getCommunityGood());
		int num = communityService.thumbsMinus(c);
		if(num == 1) {
			result = "s";
		}else {
			result = "f";
		}
		
		return result;
	}
	
	
}
