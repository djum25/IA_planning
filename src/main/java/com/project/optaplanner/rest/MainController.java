package com.project.optaplanner.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "index";
	}
	
	@RequestMapping(value = "/templates/import.html", method = RequestMethod.GET)
	public String template() {
		return "import";
	}
	
	@RequestMapping(value = "/graphe", method = RequestMethod.GET)
	public String graphe() {
		return "graphe";
	}
}
