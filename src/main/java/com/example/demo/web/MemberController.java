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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String add(@ModelAttribute MemberAddDto memberAddDto) {
        Member saveMember = memberService.save(memberAddDto);
        log.info("saveMember(Controller)= {} ", saveMember);
        return "redirect:/";
    }

    @GetMapping("/{memberId}/edit")
    public String editMember(@PathVariable Long memberId, Model model) {
        MemberGetDto member = memberService.getMember(memberId);
        model.addAttribute("member", member);
        return "/members/edit";
    }

    @PostMapping("/{memberId}/edit")
    public String edit(@PathVariable Long memberId, @ModelAttribute("member") MemberUpdateDto memberUpdateDto, RedirectAttributes redirectAttributes) {
        memberService.updateMember(memberId, memberUpdateDto);
        redirectAttributes.addAttribute("memberId", memberId);
        return "redirect:/members/{memberId}";
    }

    @PostMapping("/{memberId}/delete")
    public String delete(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/members";
    }
}
