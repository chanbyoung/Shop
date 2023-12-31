package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.member.MemberAddDto;
import com.example.demo.dto.member.MemberGetDto;
import com.example.demo.dto.member.MemberUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    Member save(MemberAddDto member);

    MemberGetDto getMember(Long memberId);

    Page<MemberGetDto> getMembers(Pageable pageable,String name);


    void updateMember(Long memberId, MemberUpdateDto memberUpdateDto);

    void deleteMember(Long memberId);


}
