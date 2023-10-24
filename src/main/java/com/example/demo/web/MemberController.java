package com.example.demo.web;

import com.example.demo.domain.Gender;
import com.example.demo.domain.Member;
import com.example.demo.dto.member.MemberAddDto;
import com.example.demo.dto.member.MemberGetDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public String members(Model model, @PageableDefault(size = 5) Pageable pageable, @RequestParam(value = "name",required = false) String name) {
        Page<MemberGetDto> members = memberService.getMembers(pageable, name);
        model.addAttribute("members", members);
        return "/members/members";
    }

    @GetMapping("/{memberId}")
    public String member(@PathVariable Long memberId, Model model) {
        MemberGetDto member = memberService.getMember(memberId);
        model.addAttribute("member", member);
        return "/members/member";
    }

    @GetMapping("/add")
    public String addMember(Model model) {
        model.addAttribute("memberAddDto", new MemberAddDto());
        return "/members/add";
    }
    @ModelAttribute("genders")
    public Gender[] genders() {
        return Gender.values();
    }

    @PostMapping("/add")
    public String add(@Validated @ModelAttribute MemberAddDto memberAddDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/members/add";
        }
        Member saveMember = memberService.save(memberAddDto);
        log.info("saveMember(Controller)= {} ", saveMember);
        return "redirect:/";
    }

    @GetMapping("/{memberId}/edit")
    public String editMember(@PathVariable Long memberId, Authentication authentication,RedirectAttributes redirectAttributes,Model model) {
        MemberGetDto member = memberService.getMember(memberId);
        if (!Objects.equals(member.getLoginId(), authentication.getName())) {
            redirectAttributes.addFlashAttribute("flag", true);
            return "redirect:/members/{memberId}";
        }
        model.addAttribute("member", member);
        return "/members/edit";
    }

    @PostMapping("/{memberId}/edit")
    public String edit(@PathVariable Long memberId,@Validated @ModelAttribute("member") MemberUpdateDto memberUpdateDto,BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/members/edit";
        }
        memberService.updateMember(memberId, memberUpdateDto);
        redirectAttributes.addAttribute("memberId", memberId);
        return "redirect:/members/{memberId}";
    }

    @PostMapping("/{memberId}/delete")
    public String delete(@PathVariable Long memberId, Authentication authentication,RedirectAttributes redirectAttributes) {
        MemberGetDto member = memberService.getMember(memberId);
        if (!Objects.equals(member.getLoginId(), authentication.getName())) {
            redirectAttributes.addFlashAttribute("flag", true);
            return "redirect:/members/{memberId}";
        }
        memberService.deleteMember(memberId);
        return "redirect:/members";
    }
}
