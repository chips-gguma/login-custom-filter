package com.sp.fc.web.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
// 통행증을 발급할 provider
public class StudentManager implements AuthenticationProvider, InitializingBean {

    // 원칙적으로는 student에 대한 리스트를 db를 핸들링 하거나 가져와야 하지만 테스트 차원으로 HashMap으로 함
    private HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Authentication 토큰을 StudentAuthentication 토큰으로 발행하기 위해 형변환
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        if(studentDB.containsKey(token.getName())) {    // ex) hong이라는 id의 사용자가 온다면 student를 담은 StudentAuthentication 인증 토큰(통행증)을 발행함
            Student student = studentDB.get(token.getName());
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUser_name())
                    .authenticated(true)
                    .build();
            // authenticationProvider를 athenticationManager에 등록해야 함
        }
        return null;    // 처리할 수 없는 authentication은 null로 넘김
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // UsernamePasswordAuthenticationToken 형태로 토큰을 받으면 검증을 해주는 provider로 동작을 하겠다.
        return authentication == UsernamePasswordAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Set.of(
                new Student("hong", " 홍길동", Set.of(new SimpleGrantedAuthority("ROLE_USER"))),
                new Student("kang", " 강아지", Set.of(new SimpleGrantedAuthority("ROLE_USER"))),
                new Student("range", " 호랑이", Set.of(new SimpleGrantedAuthority("ROLE_USER")))
        ).forEach(s->   // 빈이 초기화될 때마다
                        studentDB.put(s.getId(), s)
                );
    }
}
