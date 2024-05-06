package org.zerock.apiserver.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.zerock.apiserver.domain.MemberRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//spring seucrity가 사용하는 DTO Type으로 만들어 주어야함
public class MemberDTO extends User {

    private String email, pw, nickname;
    private boolean social;

    //본래 MemberRole type list로 들어가야하나 화면쪽에서 사용하기 편하도록
    private List<String> roleNames = new ArrayList<>();

    public MemberDTO( String email, String pw, String nickname, boolean social,
                      List<String> roleNames) {
        super(email,pw, roleNames.stream().map(str ->
                //spring security가 사용하는 권한으로 사용해야한다
                //권한을 만들어주는 type이 있다
                new SimpleGrantedAuthority("ROLE_"+str))
                .collect(Collectors.toList()));
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    //jwt문자열을 만들어서 주고 받는다
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("pw",pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);
        return dataMap;
    }
}
