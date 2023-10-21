package com.example.demo.reopsitory;

import com.example.demo.domain.Member;
import com.example.demo.domain.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.domain.QMember.*;

@Repository
@RequiredArgsConstructor
public class DslMemberRepository {
    private final JPAQueryFactory query;

    public Page<Member> getMembers(Pageable pageable, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if (name != null) {
            builder.and(member.name.contains(name));
        }
        List<Member> members = query.select(member)
                .from(member)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct().fetch();
        Long count = query.select(member.count())
                .from(member)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(members, pageable, count);
    }
}
