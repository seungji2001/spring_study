package org.zerock.apiserver.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.domain.MemberRole;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    //패스워드 처리 인코딩해서 처리해야한다
    @Autowired
    private  MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void testInsertMember(){


        for(int i = 0; i<10; i++){
            Member member = Member.builder()
                    .email("user" + i + "@aaa.com")
                    .pw(passwordEncoder.encode("111"))
                    .nickname("USER" + i)
                    .build();
            //모든 멤버는 유저라는 권한을 가지고 있다

            member.addRole(MemberRole.USER);

            if(i>5){
                member.addRole(MemberRole.MANAGER);
            }

            if(i>=8){
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }//end
    }


    //권한 읽어오기
    @Test
    public void testRead() {
        String email = "user9@aaa.com";
        Member member = memberRepository.getWithRoles(email);
        log.info("-----------------");
        log.info(member);
        log.info(member.getMemberRoleList());
    }
}
