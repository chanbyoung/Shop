package com.example.demo.reopsitory;

import com.example.demo.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(value = "select m from Member m",
            countQuery = "select count(m.id) from Member m")
    Page<Member> findByMembers(Pageable pageable);

    Optional<Member> findByLoginId(String loginId);
}
