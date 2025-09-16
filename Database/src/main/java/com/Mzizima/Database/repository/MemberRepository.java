package com.Mzizima.Database.repository;

import com.Mzizima.Database.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMembershipNumber(String membershipNumber);
    List<Member> findByRole(Member.Role role);

    @Query("SELECT COUNT(m) FROM Member m")
    long countTotalMembers();

    @Query("SELECT COUNT(m) FROM Member m WHERE m.memberType = ?1")
    long countByMemberType(Member.MemberType memberType);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.role = ?1")
    long countByRole(Member.Role role);
}
