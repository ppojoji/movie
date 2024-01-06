package com.study.funnymovie.domain.member.infoPolicy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoPolicyController {
	@GetMapping("/infopolicy")
	private String infoPolicy() {
		
		return "info_policy";
	}
}
