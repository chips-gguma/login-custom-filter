package com.sp.fc.web.teacher;

import com.sp.fc.web.student.Student;
import com.sp.fc.web.student.StudentAuthenticationToken;
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
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    // 원칙적으로는 student에 대한 리스트를 db를 핸들링 하거나 가져와야 하지만 테스트 차원으로 HashMap으로 함
    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Authentication 토큰을 StudentAuthentication 토큰으로 발행하기 위해 형변환
//        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
        if(teacherDB.containsKey(token.getCredentials())) {    // ex) hong이라는 id의 사용자가 온다면 student를 담은 StudentAuthentication 인증 토큰(통행증)을 발행함
            Teacher teacher = teacherDB.get(token.getCredentials());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUser_name())
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
                new Teacher("choi", " 최선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s->   // 빈이 초기화될 때마다
                        teacherDB.put(s.getId(), s)
                );
    }
}
