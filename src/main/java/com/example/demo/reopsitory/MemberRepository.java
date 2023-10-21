package com.example.demo.reopsitory;

import com.example.demo.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(value = "select m from Member m where m.name like %:name%",
            countQuery = "select count(m.id) from Member m")
    Page<Member> findByMembers(Pageable pageable, @Param("name") String name);

    Optional<Member> findByLoginId(String loginId);
}
