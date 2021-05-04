package com.tinystacks.sample;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloWorldController {

	@RequestMapping("/")
	public String index() {
		return "Hello World!";
	}

	@RequestMapping("/ping")
	public String ping() {
		return "";
	}

}