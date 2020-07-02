package com.example.board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.board.model.User;
import com.example.board.repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	HttpSession session;
	

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping("/signup")
	public String signupPost(
			@ModelAttribute User user,
			Model model) {

		/* 중복 아이디 가입 불가를 위해서 가입여부 확인 */
		User findUser = userRepository.findByEmail(user.getEmail());
		System.out.println("@@@@@ " + findUser);

		/* 2번 입력된 파라미터 활용 */
		if (findUser == null) {
			userRepository.save(user);
		} else {
			model.addAttribute("email", user.getEmail());
			model.addAttribute("name", user.getName());
			return "signup_error2";
		}
		
		/* 1번 history.back() 활용 */
//		if (findUser == null) {
//			userRepository.save(user);
//		} else {
//			return "signup_error";
//		}
		

		
		return "redirect:/";
		
		
	}

	@GetMapping("/signout")
	public String signout() {
		session.removeAttribute("user_info"); // 지정된 세션값만 삭제
		session.invalidate(); // 세션의 모든 정보 삭제
		return "redirect:/";
	}
	
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}

	@PostMapping("/signin")
	public String signinPost(@ModelAttribute User user) {
		User dbUser = userRepository.findByEmailAndPwd(user.getEmail(), user.getPwd());
		if (dbUser != null) {
			session.setAttribute("user_info", dbUser);
		}
		return "redirect:/";
	}

}
