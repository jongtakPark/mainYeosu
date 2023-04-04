package com.exposition.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.FreeBoardDto;
import com.exposition.entity.Idea;
import com.exposition.entity.Member;
import com.exposition.entity.Review;
import com.exposition.entity.Survey;
import com.exposition.entity.Volunteer;
import com.exposition.service.BoardService;
import com.exposition.service.CompanyService;
import com.exposition.service.IdeaService;
import com.exposition.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	private final IdeaService ideaService;
	private final MemberService memberService;
	private final CompanyService companyService;
	
	//관람후기
	@GetMapping(value="/review")
	public String reviewBoardList(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
		Page<Review> list = boardService.reviewBoardList(pageable);
		model.addAttribute("review",list);
		//페이징	        
		int nowPage = list.getPageable().getPageNumber() + 1;	        
		int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, list.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    return "board/review";
	   	}
	
	//관람후기 글쓰기 페이지로 이동
	@GetMapping(value="/reviewWrite")
	public String reviewBoardwrite(Model model) {
		model.addAttribute("freeBoardDto", new FreeBoardDto());
		return "board/reviewWrite";
	}	
			
	//관람후기 글저장
	@PostMapping(value="/reviewNew")
	public String reviewNew(Principal principal, @RequestParam(value = "files", required = false) List<MultipartFile> files, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목과 내용을 입력해주세요.");
			model.addAttribute("freeBoardDto", freeBoardDto);
			return "board/reviewWrite";
		}
		try {
			Review review = Review.createReview(freeBoardDto);
			boardService.reviewSave(review, principal.getName(), files);
			model.addAttribute("succMessage", "새 글 작성이 되었습니다.");
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "글 작성중 에러가 발생했습니다.");
			return "board/reviewWrite";
		}
		return "redirect:/board/review";
	}
	
	//관람후기 상세보기
	@GetMapping(value="/reviewView/{id}")
	public String reviewView(@PathVariable("id") Long id, Model model) {
		FreeBoardDto freeBoardDto = boardService.reviewAndFileFindById(id);
		model.addAttribute("freeBoardDto", freeBoardDto);
		return "board/reviewView";
	}
	
	//관람후기 수정창으로 이동
	@GetMapping(value="/reviewModify/{id}")
	public String reviewModify(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
		FreeBoardDto freeBoardDto = boardService.reviewAndFileFindById(id);
			try {
				if(!freeBoardDto.getCreatedBy().equals(user.getUsername())){
					if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
						model.addAttribute("freeBoardDto", freeBoardDto);
					} else {
						model.addAttribute("errorMessage", "글 작성자가 아니면 수정 할 수 없습니다.");
						model.addAttribute("freeBoardDto", freeBoardDto);
						return "board/reviewView";
					}
				} else {
					model.addAttribute("freeBoardDto", freeBoardDto);
				}
			} catch(Exception e) {
				model.addAttribute("errorMessage", "에러가 발생했습니다.");
			}
			return "board/reviewUpdateWrite";
	}
		
	//관람후기 글 수정 등록
	@PutMapping(value="/reviewUpdateNew")
	public String reviewUpdateNew(@RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Principal principal) throws Exception{
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목과 내용은 필수 입니다.");
			return "board/reviewUpdateWrite";
		}
		Review review = Review.createReview(freeBoardDto);
		try {
			boardService.reviewUpdate(review, files, principal.getName());
			model.addAttribute("succMessage", "글 수정이 되었습니다");
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "글 수정중 에러가 발생했습니다.");
			return "board/reviewUpdateWrite";
		}
		return "redirect:/board/review";	
	}
		
	//관람후기 글 삭제
	@DeleteMapping(value="/reviewDelete/{id}")
	public String deleteBoard(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
		FreeBoardDto freeBoardDto = boardService.reviewAndFileFindById(id);
			try {
				if(!freeBoardDto.getCreatedBy().equals(user.getUsername())) {
					if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
						boardService.deleteReview(id);
					} else {
						model.addAttribute("freeBoardDto", freeBoardDto);
						model.addAttribute("errorMessage", "글 작성자가 아니면 삭제 할 수 없습니다.");
						return "board/reviewView";
					}
				} else {
					boardService.deleteReview(id);
				} 
			}catch(Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "글 삭제중 에러가 발생했습니다.");
				return "board/reviewView";
			}
		return "redirect:/board/review";	
	}
		
	
	//국민아이디어게시판
    @GetMapping(value="/idea")
    public String ideaList(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
    	Page<Idea> list = boardService.ideaBoardList(pageable);
    	model.addAttribute("idea",list);

         //페이징           
        int nowPage = list.getPageable().getPageNumber() + 1;           
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, list.getTotalPages());

        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/idea";
    }
    
    //국민아이디어 글쓰기 페이지로 이동
  	@GetMapping(value="/ideaWrite")
  	public String ideaBoardwrite(Model model) {
  		model.addAttribute("freeBoardDto", new FreeBoardDto());
  		return "board/ideaWrite";
  	}	
    
  	//국민아이디어 글저장
  	@PostMapping(value="/ideaNew")
  	public String ideaNew(Principal principal, @RequestParam(value = "files", required = false) List<MultipartFile> files, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Model model) {
  		if(bindingResult.hasErrors()) {
  			model.addAttribute("errorMessage", "제목과 내용을 입력해주세요.");
			model.addAttribute("freeBoardDto", freeBoardDto);
  			return "board/ideaWrite";
  		}
  		try {
  			Idea idea = Idea.createIdea(freeBoardDto);
  			boardService.ideaSave(idea, principal.getName(), files);
  			model.addAttribute("succMessage", "새 글 작성이 되었습니다.");
  		} catch(Exception e) {
  			model.addAttribute("errorMessage", "글 작성중 에러가 발생했습니다.");
  			return "board/ideaWrite";
  		}
  		return "redirect:/board/idea";
  	}
    
  	//국민아이디어 상세보기
  	@GetMapping(value="/ideaView/{id}")
  	public String ideaView(@PathVariable("id") Long id, Model model) {
  		FreeBoardDto freeBoardDto = boardService.ideaAndFileFindById(id);
  		model.addAttribute("freeBoardDto", freeBoardDto);
  		return "board/ideaView";
  	}
  	
  	//국민아이디어 수정창으로 이동
  	@GetMapping(value="/ideaModify/{id}")
  	public String ideaModify(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
  		FreeBoardDto freeBoardDto = boardService.ideaAndFileFindById(id);
  			try {
  				if(!freeBoardDto.getCreatedBy().equals(user.getUsername())){
  					if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
  						model.addAttribute("freeBoardDto", freeBoardDto);
  					} else {
  						model.addAttribute("errorMessage", "글 작성자가 아니면 수정 할 수 없습니다.");
  						model.addAttribute("freeBoardDto", freeBoardDto);
  						return "board/ideaView";
  					}
  				} else {
  					model.addAttribute("freeBoardDto", freeBoardDto);
  				}
  			} catch(Exception e) {
  				model.addAttribute("errorMessage", "에러가 발생했습니다.");
  			}
  			return "board/ideaUpdateWrite";
  		
  	}
  	
  	//국민아이디어 글 수정 등록
  	@PutMapping(value="/ideaUpdateNew")
  	public String ideaUpdateNew(@RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Principal principal) throws Exception{
  		if(bindingResult.hasErrors()) {
  			model.addAttribute("errorMessage", "제목과 내용은 필수 입니다.");
  			model.addAttribute("freeBoardDto", freeBoardDto);
  			return "board/ideaUpdateWrite";
  		}
  		Idea idea = Idea.createIdea(freeBoardDto);
  		try {
  			boardService.ideaUpdate(idea, files, principal.getName());
  			model.addAttribute("succMessage", "글 수정이 되었습니다");
  		} catch(Exception e) {
  			e.printStackTrace();
  			model.addAttribute("errorMessage", "글 수정중 에러가 발생했습니다.");
  			return "board/ideaUpdateWrite";
  		}
  		return "redirect:/board/idea";	
  	}
  	
  	//국민아이디어 글 삭제
  	@DeleteMapping(value="/ideaDelete/{id}")
  	public String ideaDeleteBoard(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
  		FreeBoardDto freeBoardDto = boardService.ideaAndFileFindById(id);
  			try {
  				if(!freeBoardDto.getCreatedBy().equals(user.getUsername())) {
  					if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
  						boardService.deleteIdea(id);
  					} else {
  						model.addAttribute("freeBoardDto", freeBoardDto);
  						model.addAttribute("errorMessage", "글 작성자가 아니면 삭제 할 수 없습니다.");
  						return "board/ideaView";
  					}
  				} else {
  					boardService.deleteIdea(id);
  				} 
  			}catch(Exception e) {
  				model.addAttribute("errorMessage", "글 삭제중 에러가 발생했습니다.");
  			}
  		return "redirect:/board/idea";
  		
  	}
  	
  	
  	//자원봉사 게시판
    @GetMapping(value="/volunteer")
    public String volunteerList(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
    	Page<Volunteer> list = boardService.volunteerBoardList(pageable);
    	model.addAttribute("volunteer",list);

         //페이징           
        int nowPage = list.getPageable().getPageNumber() + 1;           
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, list.getTotalPages());

        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/volunteer";
    }
  	
    //자원봉사게시판 글쓰기 페이지로 이동
  	@GetMapping(value="/volunteerWrite")
  	public String volunteerBoardwrite(Model model) {
  		model.addAttribute("freeBoardDto", new FreeBoardDto());
  		return "board/volunteerWrite";
  	}	
    
    
    //자원봉사 게시판 글저장
  	@PostMapping(value="/volunteerNew")
  	public String volunteerNew(Principal principal, @RequestParam(value = "files", required = false) List<MultipartFile> files, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Model model) {
  		if(bindingResult.hasErrors()) {
  			model.addAttribute("errorMessage", "제목과 내용을 입력해주세요.");
			model.addAttribute("freeBoardDto", freeBoardDto);
  			return "board/volunteerWrite";
  		}
  		try {
  			Volunteer volunteer = Volunteer.createVolunteer(freeBoardDto);
  			boardService.volunteerSave(volunteer, principal.getName(), files);
  			model.addAttribute("succMessage", "새 글 작성이 되었습니다.");
  		} catch(Exception e) {
  			e.printStackTrace();
  			model.addAttribute("errorMessage", "글 작성중 에러가 발생했습니다.");
  			model.addAttribute("surveyboard", freeBoardDto);
  			return "board/volunteerWrite";
  		}
  		return "redirect:/board/volunteer";
  	}
  	
  	//자원봉사게시판 상세보기
  	@GetMapping(value="/volunteerView/{id}")
  	public String volunteerView(@PathVariable("id") Long id, Model model) {
  		FreeBoardDto freeBoardDto = boardService.volunteerAndFileFindById(id);
  		model.addAttribute("freeBoardDto", freeBoardDto);
  		return "board/volunteerView";
  	}
  	
  	//자원봉사게시판 수정창으로 이동
  	@GetMapping(value="/volunteerModify/{id}")
  	public String volunteerModify(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
  		FreeBoardDto freeBoardDto = boardService.volunteerAndFileFindById(id);
  		try {
  			if(!freeBoardDto.getCreatedBy().equals(user.getUsername())){
  				if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
  					model.addAttribute("freeBoardDto", freeBoardDto);
  				} else {
  					model.addAttribute("errorMessage", "글 작성자가 아니면 수정 할 수 없습니다.");
  					model.addAttribute("freeBoardDto", freeBoardDto);
  					return "board/volunteerView";
  				}
  			} else {
  				model.addAttribute("freeBoardDto", freeBoardDto);
  			}
  		} catch(Exception e) {
  			model.addAttribute("errorMessage", "글 작성자가 아니면 수정 할 수 없습니다.");
  			return "board/volunteerView";
  		}
  		return "board/volunteerUpdateWrite";
  	}
  	
  	//자원봉사게시판 글 수정 등록
  	@PutMapping(value="/volunteerUpdateNew")
  	public String volunteerUpdateNew(@RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Principal principal) throws Exception{
  		if(bindingResult.hasErrors()) {
  			model.addAttribute("errorMessage", "제목과 내용은 필수 입니다.");
  			return "board/volunteerUpdateWrite";
  		}
  		Volunteer volunteer = Volunteer.createVolunteer(freeBoardDto);
  		try {
  			boardService.volunteerUpdate(volunteer, files, principal.getName());
  			model.addAttribute("succMessage", "글 수정이 되었습니다");
  		} catch(Exception e) {
  			e.printStackTrace();
  			model.addAttribute("errorMessage", "글 수정중 에러가 발생했습니다.");
  			return "board/volunteerUpdateWrite";
  		}
  		return "redirect:/board/volunteer";	
  	}
  	
  	//자원봉사게시판 글 삭제
  	@DeleteMapping(value="/volunteerDelete/{id}")
  	public String volunteerDeleteBoard(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
  		FreeBoardDto freeBoardDto = boardService.volunteerAndFileFindById(id);
  			try {
  				if(!freeBoardDto.getCreatedBy().equals(user.getUsername())) {
  					if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
  						boardService.deleteVolunteer(id);
  					} else {
  						model.addAttribute("freeBoardDto", freeBoardDto);
  						model.addAttribute("errorMessage", "글 작성자가 아니면 삭제 할 수 없습니다.");
  						return "board/volunteerView";
  					}
  				} else {
  					boardService.deleteVolunteer(id);
  				} 
  		} catch(Exception e) {
  			model.addAttribute("errorMessage", "작성자만 삭제 할 수 있습니다.");
  			return "board/volunteerView";
  		}
  			return "redirect:/board/volunteer";
  	}
  	
}
