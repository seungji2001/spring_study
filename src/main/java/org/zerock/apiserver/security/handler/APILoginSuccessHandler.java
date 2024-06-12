package org.zerock.apiserver.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.apiserver.dto.MemberDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

//인증 성공시 핸들링
@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        log.info("----------------------------");
        log.info(authentication);
        log.info("----------------------------");

        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        // 여기에 Access랑 refresh token을 넣는다
        Map<String, Object> claims = memberDTO.getClaims();
        claims.put("accessToken", "");
        claims.put("refreshToken", "");

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");

        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
        /*
        {
        "social": false,
        "pw": "$2a$10$E7jxJrrefF5s9QUXhidOg..cEUdEhY105VFQVkiaDU4RVnNTzqRhu", -> cookie에 저장을 하던 한다
        "nickname": "USER9",
        "accessToken": "",
        "roleNames": [
            "USER",
            "MANAGER",
            "ADMIN"
        ],
        "email": "user9@aaa.com",
        "refreshToken": ""
    }
         */
    }
}
