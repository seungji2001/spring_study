package org.zerock.apiserver.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")//연관관계 있을 경우 빼준다
public class Member {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    //권한 삭제 처리 여기서 추가
    public void addRole(MemberRole memberRole){ memberRoleList.add(memberRole); }
    public void clearRole(){ memberRoleList.clear(); }
    public void changeNickname(String nickname) { this.nickname = nickname; }
    public void changePw(String pw){ this.pw = pw; }
    public void changeSocial(boolean social) { this.social = social; }
}
