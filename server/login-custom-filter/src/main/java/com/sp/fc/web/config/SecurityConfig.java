package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.TeacherManager;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // 선생이 학생 페이지로 접근하지 못하도록 함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentManager studentManager;
    private final TeacherManager teacherManager;

    public SecurityConfig(StudentManager studentManager, TeacherManager teacherManager) {
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
    }

    // auth_provider -> auth_manager에 등록
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentManager); // authenticationProvider를 studentManger로 지정
        auth.authenticationProvider(teacherManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        http
                .authorizeRequests(request->
                        request.antMatchers("/", "/login").permitAll()
                                .anyRequest().authenticated()
                )
                // manager 동작을 위해 userNamePasswordFilter 동작 필요
//                .formLogin(
//                        login->login.loginPage("/login")
//                                .permitAll()
//                                .defaultSuccessUrl("/", false)
//                                .failureUrl("/login-error")
//                )
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class) // filter를 UsernamePassword... 자리에 꽂아 줌
                .logout(logout-> logout.logoutSuccessUrl("/"))
                .exceptionHandling(e->e.accessDeniedPage("/access-denied"))
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                ;
    }
}
