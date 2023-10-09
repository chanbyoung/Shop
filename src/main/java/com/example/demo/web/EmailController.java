package com.example.demo.web;

import com.example.demo.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class EmailController {
    private final MailService mailService;

    @GetMapping("/mailCheck")
    public String mailConfirm(@RequestParam("email") String email) throws Exception {
        log.info("email={} ", email);
        String authCode = mailService.sendEmail(email);
        log.info("인증코드 = {} ", authCode);
        return authCode;
    }

}
