package com.study.funnymovie.domain.member.memberroll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MemberRollController {

	@GetMapping("/memberroll")
	private String memberRoll() {
		
		return "member_roll";
	}
}
