package com.example.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication == null) {
            model.addAttribute("flag", true);
        } else {
            model.addAttribute("flag2", true);
        }
        log.info("home controller");
        return "home";
    }
}
