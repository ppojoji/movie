package com.study.funnymovie.domain.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActorPageController {
	@Autowired
	ActorService actorService;
	
	@GetMapping(value = "/actor")
	public String PageActorDetail(@RequestParam Integer actorSeq,Model model) {
		System.out.println("[actor] " + actorSeq);
		
		Actor actor = actorService.findActor(actorSeq);
		System.out.println("[actors]" + actor);
		model.addAttribute("actor",actor);
		return "person/main";
	}
}
