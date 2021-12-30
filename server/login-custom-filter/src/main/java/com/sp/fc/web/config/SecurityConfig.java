package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentManager;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentManager studentManager;

    public SecurityConfig(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    // auth_provider -> auth_manager에 등록
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentManager); // authenticationProvider를 studentManger로 지정
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(request->
                        request.antMatchers("/").permitAll()
                                .anyRequest().authenticated()
                )
                // manager 동작을 위해 userNamePasswordFilter 동작 필요
                .formLogin(
                        login->login.loginPage("/login")
                                .permitAll()
                )
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                ;
    }
}
