package org.zerock.apiserver.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.repository.MemberRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
//로그인을 처리할때 동작
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //user name의 경우 id에 해당된다
    //메소드 내에서 MemberRepository를 사용하여 사용자 정보를 데이터베이스에서 조회하고, 이를 UserDetails 객체로 변환하여 반환합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("---------------------loadUserByUserName--------------------" + username);

        Member member = memberRepository.getWithRoles(username);
        if(member == null){
            throw new UsernameNotFoundException("Not found");
        }

        //시큐리티 처리
        //인증처리 됨
        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream()
                        .map(memberRole -> memberRole.name()).collect(Collectors.toList()));

        log.info(memberDTO);

        return memberDTO;
    }
}
