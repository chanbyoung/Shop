package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.dto.member.MemberAddDto;
import com.example.demo.dto.member.MemberGetDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.reopsitory.DslMemberRepository;
import com.example.demo.reopsitory.DslOrderRepository;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.reopsitory.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @InjectMocks MemberServiceImpl memberService;
    @Mock DslMemberRepository dslMemberRepository;
    @Mock OrderRepository orderRepository;
    @Mock MemberRepository memberRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Test
    void save() {
        // Given
        MemberAddDto memberAddDto = new MemberAddDto();
        memberAddDto.setLoginId("exampleUser");
        memberAddDto.setPassword("pass123");
        memberAddDto.setName("John Doe");
        memberAddDto.setEmail("john@example.com");
        memberAddDto.setBirth("1990-01-01");
        memberAddDto.setGender(Gender.MALE);

        Member savedMember = Member.builder()
                .id(1L)
                .loginId(memberAddDto.getLoginId())
                .password("encodedPassword")
                .name(memberAddDto.getName())
                .email(memberAddDto.getEmail())
                .birth(memberAddDto.getBirth())
                .gender(memberAddDto.getGender())
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(memberAddDto.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        Member result = memberService.save(memberAddDto);

        // Then
        assertEquals(savedMember.getId(), result.getId());
        assertEquals(savedMember.getLoginId(), result.getLoginId());

    }

    @Test
    void getMember() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("박")
                .email("pcb7893@naver.com")
                .loginId("pcb")
                .password("123")
                .gender(Gender.MALE)
                .role(Role.USER)
                .birth("20000728")
                .build();

        MemberGetDto memberGetDto = MemberGetDto.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .gender(member.getGender())
                .birth(member.getBirth())
                .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when
        MemberGetDto result = memberService.getMember(member.getId());

        //then
        Assertions.assertThat(memberGetDto.getId()).isEqualTo(result.getId());
        Assertions.assertThat(memberGetDto.getName()).isEqualTo(result.getName());
        Assertions.assertThat(memberGetDto.getLoginId()).isEqualTo(result.getLoginId());
    }

    @Test
    void getMembers() {
        //given
        String name = "";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size); // 실제 Pageable 객체 생성

        Member member = Member.builder()
                .id(1L)
                .name("박")
                .email("pcb7893@naver.com")
                .loginId("pcb")
                .password("123")
                .gender(Gender.MALE)
                .role(Role.USER)
                .birth("20000728")
                .build();
        List<Member> membersList = Collections.singletonList(member);
        Page<Member> membersPage = new PageImpl<>(membersList, pageable, membersList.size());

        // when - Mockito 모킹 설정 시, eq 메서드를 사용하여 일치시킴
        when(dslMemberRepository.getMembers(eq(pageable), eq(name))).thenReturn(membersPage);

        //when
        Page<MemberGetDto> result = memberService.getMembers(pageable, name);

        //then
        Assertions.assertThat(result.getTotalElements()).isEqualTo(membersPage.getTotalElements());
    }

    @Test
    void updateMember() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("박")
                .email("pcb7893@naver.com")
                .loginId("pcb")
                .password("123")
                .gender(Gender.MALE)
                .role(Role.USER)
                .birth("20000728")
                .build();
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
        memberUpdateDto.setName("찬");
        memberUpdateDto.setBirth("21110728");

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when
        memberService.updateMember(member.getId(), memberUpdateDto);

        //then
        Assertions.assertThat(member.getName()).isEqualTo(memberUpdateDto.getName());
        Assertions.assertThat(member.getBirth()).isEqualTo(memberUpdateDto.getBirth());
    }

    @Test
    void deleteMember() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("박")
                .email("pcb7893@naver.com")
                .loginId("pcb")
                .password("123")
                .gender(Gender.MALE)
                .role(Role.USER)
                .birth("20000728")
                .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when
        memberService.deleteMember(member.getId());

        //then
        verify(memberRepository, times(1)).delete(member);
    }
}