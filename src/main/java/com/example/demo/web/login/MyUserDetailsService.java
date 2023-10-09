package com.example.demo.web.login;

import com.example.demo.domain.Member;
import com.example.demo.reopsitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> findMember = memberRepository.findByLoginId(username);
        Member member = findMember.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다"));

        return User.builder()
                .username(member.getLoginId())
                .password(member.getPassword())
                .authorities(String.valueOf(member.getRole()))
                .build();
    }
}
