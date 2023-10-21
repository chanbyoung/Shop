package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.Role;
import com.example.demo.dto.member.MemberAddDto;
import com.example.demo.dto.member. MemberGetDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.reopsitory.DslMemberRepository;
import com.example.demo.reopsitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final DslMemberRepository dslMemberRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Member save(MemberAddDto member) {
        log.info("memberAddDto(Service)={}", member);
        Member saveMember = Member.builder()
                .loginId(member.getLoginId())
                .password(passwordEncoder.encode(member.getPassword()))
                .name(member.getName())
                .email(member.getEmail())
                .birth(member.getBirth())
                .gender(member.getGender())
                .role(Role.USER)
                .address(member.getAddress()).build();
        return memberRepository.save(saveMember);
    }

    @Override
    public MemberGetDto getMember(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        return convertToDTO(findMember.get());
    }

    @Override
    public Page<MemberGetDto> getMembers(Pageable pageable,String name) {
        return dslMemberRepository.getMembers(pageable, name).map(this::convertToDTO);
        // 리포지토리 계층에서 조회된 Page<Member>을 Page<MemberGetDto>로 변환해서 반환
    }

    private MemberGetDto convertToDTO(Member member) {
        return MemberGetDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .email(member.getEmail())
                .birth(member.getBirth())
                .gender(member.getGender())
                .address(member.getAddress())
                .build();
    }

    @Override
    @Transactional
    public void updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (!findMember.isEmpty()) {
            Member updateMember = findMember.get();
            updateMember.updateMember(memberUpdateDto);
        }
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (!findMember.isEmpty()) {
            memberRepository.delete(findMember.get());
        }
    }
}
