package com.exposition.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
import com.exposition.dto.IdeaDto;
import com.exposition.entity.Announcement;
import com.exposition.entity.Company;
import com.exposition.entity.FreeBoard;
import com.exposition.entity.Idea;
import com.exposition.entity.Member;
import com.exposition.entity.Review;
import com.exposition.entity.Survey;
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
	public String reviewNew(@RequestParam(value = "files", required = false) List<MultipartFile> files, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Principal principal, Model model) {
		if(bindingResult.hasErrors()) {
			return "board/reviewWrite";
		}
		try {
			Member member = memberService.findByMid(principal.getName());
			if(member==null) {
				Company company = companyService.findByCom(principal.getName());
				Review review = Review.createReview(freeBoardDto);
				boardService.reviewSave(review, company, files);
				model.addAttribute("succMessage", "새 글 작성이 되었습니다.");
			} else {
				Review review = Review.createReview(freeBoardDto);
				boardService.reviewSave(review, member, files);
				model.addAttribute("succMessage", "새 글 작성이 되었습니다.");
			}
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "글 작성 중 에러가 발생했습니다.");
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
	public String reviewModify(@PathVariable("id") Long id, Model model, Principal principal) {
		Review review = boardService.reviewFindById(id);
		Member member = memberService.findByMid(principal.getName());
		FreeBoardDto freeBoardDto = boardService.reviewAndFileFindById(id);
		if(member==null) {
			Company company = companyService.findByCom(principal.getName());
			if(!review.getCompany().getCom().equals(company.getCom())){
				if(principal.getName().equals("admin")) {
					model.addAttribute("freeBoardDto", freeBoardDto);
				} else {
					model.addAttribute("errorMessage", "글 작성자가 아니면 수정 할 수 없습니다.");
					model.addAttribute("freeBoardDto", freeBoardDto);
					return "board/reviewView";
				}
			}
		} else {
			if(!review.getMember().getMid().equals(principal.getName())){
				if(principal.getName().equals("admin")) {
					model.addAttribute("freeBoardDto", freeBoardDto);
				} else {
					model.addAttribute("errorMessage", "글 작성자가 아니면 수정 할 수 없습니다.");
					model.addAttribute("freeBoardDto", freeBoardDto);
					return "board/reviewView";
				}
			} else {
				model.addAttribute("freeBoardDto", freeBoardDto);
			}
		}
		return "board/reviewUpdateWrite";
	}
	
	//관람후기 글 수정 등록
	@PutMapping(value="/reviewUpdateNew")
	public String reviewUpdateNew(@RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목과 내용은 필수 입니다.");
			return "board/reviewUpdateWrite";
		}
		Member member = memberService.findByMid(principal.getName());
		Review review = Review.createReview(freeBoardDto);
		System.out.println(member);
		if(member==null) {
			Company company = companyService.findByCom(principal.getName());
			try {
				boardService.reviewUpdate(review, files, company);
				model.addAttribute("succMessage", "글 수정이 되었습니다");
			} catch(Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "글 수정 중 에러가 발생했습니다");
				return "board/reviewUpdateWrite";
			}
		} else {
			try {
				boardService.reviewUpdate(review, files, member);
				model.addAttribute("succMessage", "글 수정이 되었습니다");
			} catch(Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "글 수정 중 에러가 발생했습니다");
				return "board/reviewUpdateWrite";
			}
		}
		return "redirect:/board/review";
	}
	
	//관람후기 글 삭제
	@DeleteMapping(value="/reviewDelete/{id}")
	public String deleteBoard(@PathVariable Long id, Principal principal, Model model) {
		Review review = boardService.reviewFindById(id);
		Member member = memberService.findByMid(principal.getName());
		if(member==null) {
			Company company = companyService.findByCom(principal.getName());
			if(!review.getCompany().getCom().equals(company.getCom())) {
				model.addAttribute("errorMessage", "글 작성자가 아니면 삭제 할 수 없습니다.");
				return "board/reviewView";
			} else {
				try {
					boardService.deleteReview(id);
				} catch(Exception e) {
					model.addAttribute("errorMessage", "글 삭제중 에러가 발생했습니다.");
				}
			}
		} else {
			if(!review.getMember().getMid().equals(member.getMid())) {
				model.addAttribute("errorMessage", "글 작성자가 아니면 삭제 할 수 없습니다.");
				return "board/reviewView";
			} else {
				try {
					boardService.deleteReview(id);
				} catch(Exception e) {
					model.addAttribute("errorMessage", "글 삭제중 에러가 발생했습니다.");
				}
			}
		}
		return "redirect:/board/review";
	}
	
	//국민아이디어게시판
    @GetMapping(value="/idea")
    public String ideaList(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
       
       Page<Idea> list = ideaService.boardList(pageable);

         model.addAttribute("idea",ideaService.boardList(pageable));

         //페이징           
         int nowPage = list.getPageable().getPageNumber() + 1;           
         int startPage =  Math.max(nowPage - 4, 1);
         int endPage = Math.min(nowPage+9, list.getTotalPages());

         model.addAttribute("list", list);
         model.addAttribute("nowPage",nowPage);
         model.addAttribute("startPage", startPage);
         model.addAttribute("endPage", endPage);

         return "board/idea";
     }
    
    // 국민아이디어 글쓰기 페이지로 이동
    @GetMapping(value="/ideawrite")
    public String ideawrite(Model model) {
       model.addAttribute("ideaDto", new IdeaDto());
       return "board/ideawrite";
    }
    
    // 국민아이디어 글쓰기
    @PostMapping(value="/ideanew")
    public String ideawrite(IdeaDto ideaDto, Model model) {
       Idea idea = Idea.createIdea(ideaDto);
       ideaService.saveBoard(idea);
       return "redirect:/board/idea";
       }   
    
    // 국민아이디어 게시글 상세보기
    @GetMapping(value="/ideaview/{id}")
    public String ideaView(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
       Optional<Idea> view = ideaService.findBoard(id);
       HttpSession session = request.getSession();
       session.setAttribute("title", view.get().getTitle());
       session.setAttribute("content", view.get().getContent());
       session.setAttribute("id", view.get().getId());
       session.setAttribute("created", view.get().getCreatedBy());
       model.addAttribute("title", view.get().getTitle());
       model.addAttribute("content", view.get().getContent());
       model.addAttribute("created", view.get().getCreatedBy());
       model.addAttribute("session",session);
       return "board/ideaview";
    }
    
    //국민아이디어 게시글 수정창으로 이동
    @GetMapping(value="/ideamodify")
    public String ideamodifyView(Model model) {
       model.addAttribute("ideaDto", new IdeaDto());
       return "board/ideaupdatewrite";
    }
    
    //국민아이디어 게시글 수정등록
    @PutMapping(value="/ideamodcomplete/{id}")
    public String ideamodComplete(@PathVariable("id") Long id, @RequestParam("title") String title, @RequestParam("content") String content, IdeaDto ideaDto, Model model) {
       Idea idea = ideaService.updateBoard(id);
       ideaDto.setTitle(title);
       ideaDto.setContent(content);
       ideaDto.setId(id);
       idea = Idea.createIdea(ideaDto);
       ideaService.saveBoard(idea);
       // model.addAttribute("freeboard",boardService.boardList()));
       return "redirect:/board/idea";
    }
    
    //국민 아이디어 게시글 삭제(DeleteMapping을 사용하기 위해서 view.html에 form을 추가해서 사용해야 함)
  	@DeleteMapping(value="/idea/delete/{id}")
  	public String deleteIdeaBoard(@PathVariable Long id) {
  		ideaService.deleteBoard(id);
  		return "redirect:/board/idea";
  	}
  	
    //설문조사게시판
  	@GetMapping(value="/survey")
  	public String survey(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
  		
  		Page<Survey> list = boardService.surveyBoardList(pageable);

        model.addAttribute("survey",boardService.surveyBoardList(pageable));

        //페이징	        
        int nowPage = list.getPageable().getPageNumber() + 1;	        
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

  		return "board/survey";
  	}
  	
  	//설문조사 게시판 작성 페이지 이동
  	@GetMapping(value="/surveywrite")
  	public String surveyWrite(Model model) {
  		model.addAttribute("surveyboard", new FreeBoardDto());
  		return "board/surveywrite";
  	}
  			
  	//설문조사 작성 글 저장
  	@PostMapping(value="/survey/new")
  	public String surveyNew(FreeBoardDto freeBoardDto) {
  		Survey survey = Survey.createSurvey(freeBoardDto);
  		boardService.surveyBoardSave(survey);
  		return "redirect:/board/survey";
  	}
  	//설문조사 상세 페이지 이동
  	@GetMapping(value="/survey/view/{id}")
  	public String surveyView(@PathVariable("id") Long id, Model model) {
  		Survey survey = boardService.findSurveyBoard(id);
  		FreeBoardDto surveyFormDto = FreeBoardDto.of(survey);
  		model.addAttribute("surveyboard", surveyFormDto);
  		return "board/surveyview";
  	}
  	//설문조사 완료
  	@PutMapping(value="/survey/complete")
  	public String surveyComplete(@AuthenticationPrincipal User user) {
  		Member member = memberService.findByMid(user.getUsername());
  		member.setSurvey("Y");
  		memberService.updateMember(member);
  		return "redirect:/board/survey";
  	}
  	
  	
}
