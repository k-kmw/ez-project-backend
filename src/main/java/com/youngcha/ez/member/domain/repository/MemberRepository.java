package com.youngcha.ez.member.domain.repository;

import com.youngcha.ez.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Boolean existsByUserId(String userId);
}
