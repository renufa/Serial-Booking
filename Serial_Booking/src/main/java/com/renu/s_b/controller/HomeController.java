package com.renu.s_b.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	private static final Logger LOGGER=LoggerFactory.getLogger(HomeController.class);
	@RequestMapping(value="/")
	public String home() {
		LOGGER.info(" From class : HomeController,Method : home()");
		
		return "home";
	}
	
}