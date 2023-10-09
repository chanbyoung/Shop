package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.member.MemberAddDto;
import com.example.demo.dto.member.MemberGetDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.reopsitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    @Transactional
    @Override
    public Long save(MemberAddDto member) {
        Member saveMember = Member.builder()
                .name(member.getName())
                .address(member.getAddress()).build();
        return saveMember.getId();
    }

    @Override
    public Member getMember(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        return findMember.get();
    }

    @Override
    public Page<MemberGetDto> getMembers(Pageable pageable) {
        return memberRepository.findByMembers(pageable).map(this::convertToDTO);
        // 리포지토리 계층에서 조회된 Page<Member>을 Page<MemberGetDto>로 변환해서 반환
    }

    private MemberGetDto convertToDTO(Member member) {
        return MemberGetDto.builder()
                .id(member.getId())
                .name(member.getName())
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
