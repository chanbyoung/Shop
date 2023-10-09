package com.example.demo.web.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginForm(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "members/loginForm";
    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";

    }
}
