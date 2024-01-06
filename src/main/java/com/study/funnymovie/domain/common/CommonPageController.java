package com.study.funnymovie.domain.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonPageController {
	
	@GetMapping("/top")
	public String topPage() {
		return "top";
	}
	
	@GetMapping("/infoPolicy")
	public String infoPolicyPage() {
		return "info_Policy";
	}
	
	@GetMapping("/memberAgreement")
	public String memberAgreementPage() {
		return "member_agreement";
	}
	
	@GetMapping("/memberRull")
	public String memberRullPage() {
		return "member_rull";
	}
}
