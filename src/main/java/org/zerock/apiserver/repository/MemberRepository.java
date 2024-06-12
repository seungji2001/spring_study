package org.zerock.apiserver.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.apiserver.domain.Member;

public interface MemberRepository extends JpaRepository<Member,String> {

    //이 경우 role은 가지고 오지 않음
    @Query("select m from Member m where m.email = :email")
    //role도 가지고 오게 하는 코드  여러개 가져오면 {} 배열로 처리
    @EntityGraph(attributePaths = {"memberRoleList"})
    Member getWithRoles(@Param("email") String email);

}
